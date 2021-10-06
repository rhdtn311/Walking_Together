package backend.server.repository;

import backend.server.DTO.s3.fileDelete.FileDeleteDTO;
import backend.server.entity.MemberProfilePictures;
import backend.server.repository.s3.FileDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberProfilePicturesRepository extends JpaRepository<MemberProfilePictures, Long>, FileDeleteRepository {

    public Optional<MemberProfilePictures> findMemberProfilePicturesByStdId(String stdId);

    public boolean existsMemberProfilePicturesByStdId(String stdId);

    @Override
    default MemberProfilePictures find(FileDeleteDTO fileDeleteDTO) {
        return findMemberProfilePicturesByStdId(fileDeleteDTO.getStdId()).get();
    }
}
