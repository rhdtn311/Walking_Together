package backend.server.repository;

import backend.server.entity.MemberProfilePictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberProfilePicturesRepository extends JpaRepository<MemberProfilePictures, Long> {

    public Optional<MemberProfilePictures> findMemberProfilePicturesByStdId(String stdId);

    public boolean existsMemberProfilePicturesByStdId(String stdId);
}
