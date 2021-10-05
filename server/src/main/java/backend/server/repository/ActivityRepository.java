package backend.server.repository;

import backend.server.entity.Activity;
import backend.server.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("select p from Partner p left join p.member where p.member.stdId = :stdId")
    List<Partner> getPartnerList(@Param("stdId") String stdId);


    boolean existsActivitiesByPartner_PartnerId(Long partnerId);
}
