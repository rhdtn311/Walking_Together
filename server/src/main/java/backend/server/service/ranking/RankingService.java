package backend.server.service.ranking;

import backend.server.DTO.ranking.RankingDTO;
import backend.server.entity.Member;
import backend.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;

    public List<RankingDTO> getRanking() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("distance").descending());

        Page<Member> memberPage = userRepository.findAll(pageable);

        return memberToRankingDTOList(memberPage);
    }

    public List<RankingDTO> memberToRankingDTOList(Page<Member> memberPage) {
        return memberPage.stream().map(Member::toRankingDTO).collect(Collectors.toList());
    }
}







