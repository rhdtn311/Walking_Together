package backend.server.security.filter.authenticationProvide;

import backend.server.entity.Member;
import backend.server.exception.ErrorResponse;
import backend.server.repository.MemberRepository;
import backend.server.security.exception.LoginUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws LoginUserNotFoundException {

        return memberRepository.findMemberByStdId(id)
                .map(this::createUser)
                .orElseThrow(LoginUserNotFoundException::new);
    }

    private User createUser(Member member) {

        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toList());

        return new User(member.getStdId(), member.getPassword(), grantedAuthorities);
    }
}
