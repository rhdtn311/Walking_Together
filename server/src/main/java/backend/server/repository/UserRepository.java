package backend.server.repository;

import backend.server.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    // 쿼리가 수행될 때 Eager 조회로 authorities 정보를 가져오게 된다.
    @EntityGraph(attributePaths = "authorities")
    // stdId를 기준으로 User 정보를 가져올 때 권한 정보도 같이 가져옴
    Optional<Member> findOneWithAuthoritiesByStdId(String stdId);

    // 학번으로 회원 찾기
    Optional<Member> findMemberByStdIdAndNameAndBirth(String stdId, String name, String birth);

    // 이메일로 회원 찾기
    Optional<Member> findMemberByEmail(String email);

    // 휴대폰 번호로 회원 찾기
    Optional<Member> findMemberByPhoneNumber(String phoneNumber);

    Optional<Member> findMemberByStdId(String stdId);

    @Query("SELECT p.partnerName, p.partnerDetail, p.partnerBirth, p.partnerId FROM Partner p LEFT JOIN p.member WHERE p.member.stdId = :stdId")
    List<List<Object>> getPartnerList(@Param("stdId") String stdId);
}
