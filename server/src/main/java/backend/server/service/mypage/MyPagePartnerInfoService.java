package backend.server.service.mypage;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.repository.querydsl.MyPageQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPagePartnerInfoService {

    private final MyPageQueryRepository myPageQueryRepository;

    @Transactional(readOnly = true)
    public List<MyPageDTO.MyPageListResDTO> getPartnerList(String stdId) {
        return myPageQueryRepository.findPartnerList(stdId);
    }
}
