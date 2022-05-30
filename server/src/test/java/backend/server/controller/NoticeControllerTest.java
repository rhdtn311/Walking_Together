package backend.server.controller;

import backend.server.DTO.notice.NoticeDTO;
import backend.server.DTO.page.PageRequestDTO;
import backend.server.DTO.page.PageResultDTO;
import backend.server.entity.Notice;
import backend.server.service.notice.*;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoticeControllerTest {

    @InjectMocks
    private NoticeController noticeController;

    @Mock
    private NoticeListService noticeListService;
    @Mock
    private NoticeDetailService noticeDetailService;
    @Mock
    private NoticeCreationService noticeCreationService;
    @Mock
    private NoticeModifyService noticeModifyService;
    @Mock
    private NoticeDeleteService noticeDeleteService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController).build();
    }

    @Test
    @DisplayName("공지사항 게시물 출력 성공")
    void findNoticesSuccess() throws Exception {
        // given
        PageRequestDTO request = pageRequestDTO();
        PageResultDTO<Object, Object> response = pageResultDTO();

        doReturn(response).when(noticeListService).findNotices(any(PageRequestDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/noticeList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isNotEmpty())
                .andExpect(jsonPath("data.totalPage").value(response.getTotalPage()))
                .andExpect(jsonPath("data.page").value(response.getPage()))
                .andExpect(jsonPath("data.size").value(response.getSize()))
                .andExpect(jsonPath("data.start").value(response.getStart()))
                .andExpect(jsonPath("data.end").value(response.getEnd()))
                .andExpect(jsonPath("data.prev").value(response.isPrev()))
                .andExpect(jsonPath("data.next").value(response.isNext()))
                .andExpect(jsonPath("message").value("게시글 조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray pageDataList = data.getJSONArray("pageDataList");
        for (int i = 0; i < 2; i++) {
            List<NoticeDTO.NoticeListResDTO> noticeListResDTOs = response.getPageDataList().stream().map(n -> {
                return (NoticeDTO.NoticeListResDTO) n;
            }).collect(Collectors.toList());

            assertThat(pageDataList.getJSONObject(i).getLong("noticeId")).isEqualTo(noticeListResDTOs.get(i).getNoticeId());
            assertThat(pageDataList.getJSONObject(i).getString("title")).isEqualTo(noticeListResDTOs.get(i).getTitle());
            assertThat(pageDataList.getJSONObject(i).getString("content")).isEqualTo(noticeListResDTOs.get(i).getContent());
            assertThat(pageDataList.getJSONObject(i).getString("date")).isEqualTo(TestConst.dateTimeToString(noticeListResDTOs.get(i).getDate()));
        }
        JSONArray pageList = data.getJSONArray("pageList");
        for (int i = 1; i <= 5; i++) {
            assertThat(pageList.getInt(i - 1)).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("공지사항 게시물 상세 조회 성공")
    void getNoticeDetailSuccess() throws Exception {

        // given
        Long request = 1L;
        NoticeDTO.NoticeDetailResDTO response = noticeDetailResDTO();

        doReturn(response).when(noticeDetailService).getNoticeDetail(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/notice")
                        .param("noticeId", String.valueOf(request))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.noticeId").value(response.getNoticeId()))
                .andExpect(jsonPath("data.title").value(response.getTitle()))
                .andExpect(jsonPath("data.content").value(response.getContent()))
                .andExpect(jsonPath("data.createTime").value(response.getCreateTime()))
                .andExpect(jsonPath("message").value("공지사항 게시물 조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        JSONObject data = new JSONObject(mvcResult.getResponse().getContentAsString()).getJSONObject("data");
        JSONArray attachedFiles = data.getJSONArray("attachedFiles");
        JSONArray imageFiles = data.getJSONArray("imageFiles");
        for (int i = 0; i < 2; i++) {
            assertThat(attachedFiles.getString(i)).isEqualTo(response.getAttachedFiles().get(i));
            assertThat(imageFiles.getString(i)).isEqualTo(response.getImageFiles().get(i));
        }
    }

    @Test
    @DisplayName("공지사항 등록 성공")
    void createNoticeSuccess() throws Exception {
        // given
        NoticeDTO.NoticeCreationReqDTO request = noticeCreationReqDTO();
        Long response = 1L;

        doReturn(response).when(noticeCreationService).saveNotice(any(NoticeDTO.NoticeCreationReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/createpost")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("noticeCreationReqDTO", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("게시글 업로드 완료"))
                .andExpect(jsonPath("status").value(200));

    }

    @Test
    @DisplayName("공지사항 게시물 수정")
    void updateNoticeViewSuccess() throws Exception {

        // given
        Long request = 1L;
        NoticeDTO.NoticeModifyResDTO response = noticeModifyResDTO();

        doReturn(response).when(noticeModifyService).findNoticeInfo(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/update")
                        .param("noticeId", String.valueOf(request))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(response.getTitle()))
                .andExpect(jsonPath("data.content").value(response.getContent()))
                .andExpect(jsonPath("message").value("게시글 조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        JSONObject data = new JSONObject(mvcResult.getResponse().getContentAsString()).getJSONObject("data");
        JSONArray attachedFiles = data.getJSONArray("attachedFiles");
        JSONArray imageFiles = data.getJSONArray("imageFiles");
        for (int i = 0; i < 2; i++) {
            assertThat(attachedFiles.getString(i)).isEqualTo(response.getAttachedFiles().get(i));
            assertThat(imageFiles.getString(i)).isEqualTo(response.getImageFiles().get(i));
        }
    }

    @Test
    @DisplayName("게시글 수정 완료 성공")
    void updateNoticeInfoSuccess() throws Exception {

        // given
        NoticeDTO.NoticeModifyReqDTO request = noticeModifyReqDTO();
        Long response = 1L;

        doReturn(response).when(noticeModifyService).updateNoticeInfo(any(NoticeDTO.NoticeModifyReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/update")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("noticeModifyReqDTO", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("게시글 수정 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("공지사항 게시물 삭제 성공")
    void deleteNoticeSuccess() throws Exception {

        // given
        Long request = 1L;
        Long response = 1L;

        doReturn(response).when(noticeDeleteService).deleteNotice(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/delete")
                        .param("noticeId", String.valueOf(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("게시물 삭제 성공"))
                .andExpect(jsonPath("status").value(200));
    }

    private NoticeDTO.NoticeModifyReqDTO noticeModifyReqDTO() throws IOException {
        return NoticeDTO.NoticeModifyReqDTO.builder()
                .noticeId(1L)
                .title("title")
                .content("content")
                .imageFiles(List.of(TestConst.getMockMultipartFile()))
                .attachedFiles(List.of(TestConst.getMockMultipartFile()))
                .build();
    }

    private NoticeDTO.NoticeModifyResDTO noticeModifyResDTO() {

        NoticeDTO.NoticeModifyResDTO noticeModifyResDTO = NoticeDTO.NoticeModifyResDTO.builder()
                .title("title")
                .content("content")
                .build();
        noticeModifyResDTO.setAttachedFiles(attachedFiles());
        noticeModifyResDTO.setImageFiles(imageFiles());

        return noticeModifyResDTO;
    }

    private NoticeDTO.NoticeCreationReqDTO noticeCreationReqDTO() throws IOException {
        return NoticeDTO.NoticeCreationReqDTO.builder()
                .title("title")
                .content("content")
                .attachedFiles(List.of(TestConst.getMockMultipartFile()))
                .imageFiles(List.of(TestConst.getMockMultipartFile()))
                .build();
    }

    private NoticeDTO.NoticeDetailResDTO noticeDetailResDTO() {
        NoticeDTO.NoticeDetailResDTO noticeDetailResDTOs = NoticeDTO.NoticeDetailResDTO.builder()
                .noticeId(1L)
                .title("title")
                .content("content")
                .createTime(LocalDateTime.now())
                .build();
        noticeDetailResDTOs.setAttachedFiles(attachedFiles());
        noticeDetailResDTOs.setImageFiles(imageFiles());

        return noticeDetailResDTOs;
    }

    private List<String> attachedFiles() {
        List<String> attachedFiles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            attachedFiles.add("attachFile " + i);
        }

        return attachedFiles;
    }

    private List<String> imageFiles() {
        List<String> imageFiles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            imageFiles.add("imageFiles " + i);
        }
        return imageFiles;
    }

    private PageResultDTO<Object, Object> pageResultDTO() {
        return PageResultDTO.builder()
                .pageDataList(noticeListResDTOs())
                .totalPage(10)
                .page(1)
                .size(5)
                .start(2)
                .end(3)
                .prev(true)
                .next(false)
                .pageList(List.of(1,2,3,4,5))
                .build();

    }

    private List<Object> noticeListResDTOs() {
        List<Object> noticeListResDTOs = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            noticeListResDTOs.add(
                    NoticeDTO.NoticeListResDTO.builder()
                            .noticeId((long) i)
                            .title("title" + i)
                            .content("content" + i)
                            .date(LocalDateTime.now().plusDays(i))
                            .build()
            );
        }
        return noticeListResDTOs;
    }


    private PageRequestDTO pageRequestDTO() {
        return PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("keyword")
                .build();

    }


}