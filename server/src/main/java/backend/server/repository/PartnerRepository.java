package backend.server.repository;

import backend.server.entity.Member;
import backend.server.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findPartnerByPartnerId(Long partnerId);

    Optional<Partner> findPartnerByMember(Member member);

    boolean existsPartnerByPartnerId(Long partnerId);
}

