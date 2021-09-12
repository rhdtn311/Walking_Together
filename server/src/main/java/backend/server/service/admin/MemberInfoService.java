package backend.server.service.admin;

import backend.server.DTO.admin.AdminDTO;
import backend.server.repository.querydsl.AdminQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberInfoService {

    private final AdminQueryRepository adminQueryRepository;
    // 학생정보조회
    public List<AdminDTO.MemberResDTO> getMemberInfo(String keyword) {
        return adminQueryRepository.findMemberInfo(keyword);
    }
}
