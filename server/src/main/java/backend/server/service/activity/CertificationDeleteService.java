package backend.server.service.activity;

import backend.server.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CertificationDeleteService {

    private final CertificationRepository certificationRepository;

    public long deleteCertification(long activityId) {
        if (certificationRepository.findById(activityId).isPresent()) {
            certificationRepository.deleteById(activityId);
        }
        return activityId;
    }
}
