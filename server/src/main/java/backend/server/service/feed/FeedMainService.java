package backend.server.service.feed;

import backend.server.DTO.feed.FeedDTO;
import backend.server.repository.querydsl.FeedQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedMainService {

    private final FeedQueryRepository feedQueryRepository;

    // 피드
    public List<FeedDTO.FeedMainResDTO> getFeedMain(String stdId, String sort) {
        return feedQueryRepository.findFeedMainInfo(stdId, sort);
    }
}
