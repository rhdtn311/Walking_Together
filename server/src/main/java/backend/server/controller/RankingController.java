package backend.server.controller;

import backend.server.DTO.RankingDTO;
import backend.server.DTO.response.ResponseDTO;
import backend.server.service.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/ranking")
    public ResponseEntity<ResponseDTO> getRanking() {
        List<RankingDTO> rankers = rankingService.getRanking();

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("랭킹 불러오기 성공")
                .data(rankers)
                .build());
    }
}
