package backend.server.DTO.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
@Data
public class PageResultDTO<DTO,ENTITY> {

    // DTO 리스트
    private List<DTO> pageDataList;
//
//    // page 리스트
//    private HashMap<String, Object> pageInfo;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<ENTITY> result, Function<ENTITY, DTO> fn) {   // Function<EN, DTO>는 엔티티 객체들을 DTO로 변환

        // 생성된 Page객체를 DTO로 바꾸고 List 형태로 변환
        pageDataList = result.stream().map(fn).collect(Collectors.toList());

        // 전체 페이지
        totalPage = result.getTotalPages();

        // 1~n번 까지의 페이지 리스트를 생성
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        // 현재 페이지 번호
        this.page = pageable.getPageNumber() + 1;
        // 페이지 크기
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = Math.min(totalPage, tempEnd);

        next = totalPage > tempEnd;

        // 시작번호~ 끝번호까지를 리스트로 생성
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
