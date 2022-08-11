package backend.server.repository;

import backend.server.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리가 수행될 때 Eager 조회로 authorities 정보를 가져오게 된다.
    @EntityGraph(attributePaths = "authorities")

    Optional<Member> findMemberByStdIdAndNameAndBirth(String stdId, String name, String birth);

    Optional<Member> findMemberByStdId(String stdId);

    boolean existsMemberByStdId(String stdId);

    boolean existsMemberByEmail(String email);

    boolean existsMemberByPhoneNumber(String phoneNumber);
}
