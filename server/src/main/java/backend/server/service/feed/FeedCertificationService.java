package backend.server.service.feed;

import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.repository.querydsl.FeedQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedCertificationService {
    private final FeedQueryRepository feedQueryRepository;

    @Transactional
    public FeedDTO.CertificationResDTO getCertification(LocalDate from, LocalDate to, String stdId) {

        List<CertificationDTO> certificationDTOList = feedQueryRepository.findCertification(from, to, stdId);
        return new FeedDTO.CertificationResDTO(certificationDTOList);
    }
}
