package backend.server.s3;

import backend.server.entity.*;
import backend.server.repository.*;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final NoticeImagesRepository noticeImagesRepository;
    private final NoticeAttachedFilesRepository noticeAttachedFilesRepository;
    private final MemberProfilePicturesRepository memberProfilePicturesRepository;
    private final PartnerPhotosRepository partnerPhotosRepository;
    private final ActivityCheckImagesRepository activityCheckImagesRepository;
    private final S3Service s3Service;

    // 공지사항 이미지 파일을 DB에 저장
    public void saveImage(String fileName, Long noticeId) {

        String fileUrl = s3Service.getFileUrl(fileName);

        NoticeImages entity = NoticeImages.builder()
                .noticeId(noticeId)
                .noticeImageUrl(fileUrl)
                .noticeImageName(fileName)
                .build();

        noticeImagesRepository.save(entity);
    }

    // 공지사항 이미지 파일을 S3서버에 업로드
    public String uploadImage(MultipartFile file, Long noticeId) {

        // 파일 이름을 유니크하게 변경
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
            saveImage(fileName, noticeId);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",
                    file.getOriginalFilename()));
        } return s3Service.getFileUrl(fileName);
    }

    // 첨부파일을 DB에 저장
    public void saveAttachedFiles(String fileName, Long noticeId) {

        String fileUrl = s3Service.getFileUrl(fileName);

        NoticeAttachedFiles entity = NoticeAttachedFiles.builder()
                .noticeId(noticeId)
                .noticeAttachedFilesUrl(fileUrl)
                .noticeAttachedFileName(fileName)
                .build();

        noticeAttachedFilesRepository.save(entity);
    }

    // 첨부파일 S3서버에 업로드
    public String uploadAttached(MultipartFile file, Long noticeId) {
        // 파일 이름을 유니크하게 변경
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
            saveAttachedFiles(fileName, noticeId);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",
                    file.getOriginalFilename()));
        } return s3Service.getFileUrl(fileName);
    }

    // 파일이름을 + 시간으로 유니크하게 생성
    private String createFileName(String originalFileName) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = originalFileName + "-" + date.format(new Date());
        return fileName;
    }

    // 이미지 파일 삭제
    public void deleteImageFile(Long noticeId) {

        List<NoticeImages> noticeImages = noticeImagesRepository.findNoticeImagesByNoticeId(noticeId);

        noticeImages.forEach(i -> {
            s3Service.deleteFile(i.getNoticeImageName());
        });
    }

    // 첨부 파일 삭제
    public void deleteAttachedFile(Long noticeId) {

        List<NoticeAttachedFiles> attachedFiles = noticeAttachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);

        attachedFiles.forEach(f -> {
            s3Service.deleteFile(f.getNoticeAttachedFileName());
        });
    }

//    // 파일 수정
//    public void updateFile(Long noticeId) {
//
//        List<NoticeImages> images = noticeImagesRepository.findNoticeImagesByNoticeId(noticeId);
//        List<NoticeAttachedFiles> files = noticeAttachedFilesRepository.findNoticeAttachedFilesByNoticeId(noticeId);
//    }

    // 프로필 사진을 DB에 저장
    public void saveProfilePictures(String fileName, String stdId) {

        String fileUrl = s3Service.getFileUrl(fileName);

        MemberProfilePictures entity = MemberProfilePictures.builder()
                .profilePictureName(fileName)
                .profilePictureUrl(fileUrl)
                .stdId(stdId)
                .build();

        memberProfilePicturesRepository.save(entity);
    }

    // 프로필 사진을 S3서버에 업로드
    public String uploadProfilePictures(MultipartFile file, String stdId) {
        // 파일 이름을 유니크하게 변경
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
            saveProfilePictures(fileName, stdId);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",
                    file.getOriginalFilename()));
        } return s3Service.getFileUrl(fileName);
    }

    // 프로필 사진 삭제
    public void deleteProfilePictures(String stdId) {

        MemberProfilePictures profilePictures = memberProfilePicturesRepository.findMemberProfilePicturesByStdId(stdId);

        s3Service.deleteFile(profilePictures.getProfilePictureName());
    }

    // 파트너 사진 파일을 DB에 저장
    public void savePartnerPhoto(String fileName, Long partnerId) {

        String fileUrl = s3Service.getFileUrl(fileName);

        PartnerPhotos entity = PartnerPhotos.builder()
                .partnerId(partnerId)
                .partnerPhotoUrl(fileUrl)
                .partnerPhotoName(fileName)
                .build();

        partnerPhotosRepository.save(entity);
    }

    // 파트너 사진 파일을 S3서버에 업로드
    public String uploadPartnerPhoto(MultipartFile file, Long partnerId) {

        // 파일 이름을 유니크하게 변경
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
            savePartnerPhoto(fileName, partnerId);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",
                    file.getOriginalFilename()));
        } return s3Service.getFileUrl(fileName);
    }

    // 파트너 사진 삭제
    public void deletePartnerPhoto(Long partnerId) {

        PartnerPhotos partnerPhoto = partnerPhotosRepository.findPartnerPhotosByPartnerId(partnerId);

        s3Service.deleteFile(partnerPhoto.getPartnerPhotoName());
    }

    // 활동 이미지 사진을 DB에 저장
    public void saveActivityCheckImage(String fileName, Long activityId) {

        String fileUrl = s3Service.getFileUrl(fileName);

        ActivityCheckImages entity = ActivityCheckImages.builder()
                .activityId(activityId)
                .imageUrl(fileUrl)
                .imageName(fileName)
                .build();

        activityCheckImagesRepository.save(entity);
    }

    // 활동 이미지 파일을 S3서버에 업로드
    public String uploadMapImages(MultipartFile file, Long activityId, String checkFile) {

        String fileName = createFileName(file.getOriginalFilename());
        if (checkFile.equals("start")) {
            fileName = "start " + fileName;
        } else if (checkFile.equals("end")) {
            fileName = "end " + fileName;
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
            saveActivityCheckImage(fileName, activityId);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)",
                    file.getOriginalFilename()));
        } return s3Service.getFileUrl(fileName);
    }
}

