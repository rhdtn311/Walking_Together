package backend.server.controller;

import backend.server.DTO.common.CertificationDTO;
import backend.server.DTO.common.MapCaptureDTO;
import backend.server.DTO.feed.FeedDTO;
import backend.server.service.feed.FeedCertificationService;
import backend.server.service.feed.FeedDetailService;
import backend.server.service.feed.FeedMainService;
import backend.server.service.feed.FeedReviewService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {

    @InjectMocks
    private FeedController feedController;

    @Mock
    private FeedMainService feedMainService;
    @Mock
    private FeedDetailService feedDetailService;
    @Mock
    private FeedReviewService feedReviewService;
    @Mock
    private FeedCertificationService feedCertificationService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build();
    }

    @Test
    @DisplayName("피드 메인 조회 성공")
    void feedMainSuccess() throws Exception {
        // given
        String request1 = "stdId";
        String request2 = "sort";
        List<FeedDTO.FeedMainResDTO> response = feedMainResDTO();

        doReturn(response).when(feedMainService)
                .getFeedMain(request1, request2);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/feed")
                        .param("stdId", request1)
                        .param("sort", request2)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("message").value("조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < 2; i++) {

            assertThat(response.get(0).getActivityId()).isEqualTo(data.getJSONObject(0).getLong("activityId"));
            assertThat(response.get(0).getActivityStatus()).isEqualTo(data.getJSONObject(0).getInt("activityStatus"));
            assertThat(response.get(0).getDistance()).isEqualTo(data.getJSONObject(0).getLong("distance"));
            assertThat(TestConst.dateToString(response.get(0).getActivityDate())).isEqualTo(data.getJSONObject(0).getString("activityDate"));
            assertThat(response.get(0).getActivityDivision()).isEqualTo(data.getJSONObject(0).getInt("activityDivision"));
            assertThat(response.get(0).getPartnerName()).isEqualTo(data.getJSONObject(0).getString("partnerName"));

        }

    }

    @Test
    @DisplayName("피드 상세 조회 성공")
    void feedDetailSuccess() throws Exception {
        // given
        Long request = 1L;
        FeedDTO.FeedDetailResDTO response = feedDetailResDTO();

        doReturn(response).when(feedDetailService).getFeedDetail(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/feed/detail")
                        .param("activityId", String.valueOf(request))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isNotEmpty())
                .andExpect(jsonPath("message").value("조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        assertThat(data.getString("activityDate")).isEqualTo(TestConst.dateToString(response.getActivityDate()));
        assertThat(data.getString("partnerName")).isEqualTo(response.getPartnerName());
        assertThat(data.getString("startTime")).isEqualTo(TestConst.dateTimeToString(response.getStartTime()));
        assertThat(data.getString("endTime")).isEqualTo(TestConst.dateTimeToString(response.getEndTime()));
        assertThat(data.getInt("activityDivision")).isEqualTo(response.getActivityDivision());
        assertThat(data.getString("review")).isEqualTo(response.getReview());
    }

    @Test
    @DisplayName("소감문 작성 성공")
    void writeActivityReviewSuccess() throws Exception {
        // given
        Long activityIdRequest = 1L;
        String reviewRequest = "review";
        Long response = 1L;

        doReturn(response).when(feedReviewService).writeActivityReview(activityIdRequest, reviewRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/feed/detail/review")
                        .param("activityId", String.valueOf(activityIdRequest))
                        .param("review", reviewRequest)
        );

        // then
        resultActions
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("소감문 저장 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("인증서 저장 성공")
    void createCertificationSuccess() throws Exception {
        // given
        String stdIdRequest = "stdId";
        String fromRequest = "2022/05/28";
        String toRequest = "2022/05/31";

        FeedDTO.CertificationResDTO response = certificationResDTO();

        doReturn(response).when(feedCertificationService)
                .findCertification(any(LocalDate.class), any(LocalDate.class), any(String.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/feed/certification")
                        .param("stdId", stdIdRequest)
                        .param("from", fromRequest)
                        .param("to", toRequest)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(jsonPath("data").isNotEmpty())
                .andExpect(jsonPath("message").value("발급 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONObject("data");

        assertThat(data.getString("ordinaryTimes")).isEqualTo(response.getOrdinaryTimes());
        assertThat(data.getString("careTimes")).isEqualTo(response.getCareTimes());
        assertThat(data.getString("totalTime")).isEqualTo(response.getTotalTime());

        for (int i = 0; i < 2; i++) {
            JSONArray certificationDTOJsonArray = data.getJSONArray("eachCertificationInfos");
            JSONObject certificationDTOJson = certificationDTOJsonArray.getJSONObject(i);
            CertificationDTO certificationDTO = response.getEachCertificationInfos().get(i);

            assertThat(certificationDTOJson.getLong("certificationId")).isEqualTo(certificationDTO.getCertificationId());
            assertThat(certificationDTOJson.getString("partnerName")).isEqualTo(certificationDTO.getPartnerName());
            assertThat(certificationDTOJson.getString("department")).isEqualTo(certificationDTO.getDepartment());
            assertThat(certificationDTOJson.getString("name")).isEqualTo(certificationDTO.getName());
            assertThat(certificationDTOJson.getLong("distance")).isEqualTo(certificationDTO.getDistance());
            assertThat(certificationDTOJson.getString("stdId")).isEqualTo(certificationDTO.getStdId());
            assertThat(certificationDTOJson.getString("activityDate")).isEqualTo(TestConst.dateToString(certificationDTO.getActivityDate()));
            assertThat(certificationDTOJson.getString("startTime")).isEqualTo(TestConst.dateTimeToString(certificationDTO.getStartTime()));
            assertThat(certificationDTOJson.getString("endTime")).isEqualTo(TestConst.dateTimeToString(certificationDTO.getEndTime()));
            assertThat(certificationDTOJson.getString("ordinaryTime")).isEqualTo(TestConst.timeToString(certificationDTO.getOrdinaryTime()));
            assertThat(certificationDTOJson.getString("careTime")).isEqualTo(TestConst.timeToString(certificationDTO.getCareTime()));
        }

    }


    private FeedDTO.CertificationResDTO certificationResDTO() {
        return FeedDTO.CertificationResDTO.builder()
                .certificationDTOList(certificationDTOs())
                .build();
    }

    private List<CertificationDTO> certificationDTOs() {

        ArrayList<CertificationDTO> certificationDTOs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            certificationDTOs.add(CertificationDTO.builder()
                    .certificationId((long)i)
                    .activityDate(LocalDate.now().plusDays(i))
                    .partnerName("partnerName" + i)
                    .department("department" + i)
                    .name("name" + i)
                    .startTime(LocalDateTime.now().plusHours(i))
                    .endTime(LocalDateTime.now().plusHours(i + 1))
                    .ordinaryTime(LocalTime.now().plusHours(i))
                    .careTime(LocalTime.now().plusHours(i))
                    .distance((long)1000 * i)
                    .stdId("stdId" + i)
                    .build());
        }

        return certificationDTOs;
    }

    private FeedDTO.FeedDetailResDTO feedDetailResDTO() {
        FeedDTO.FeedDetailResDTO feedDetailResDTO = FeedDTO.FeedDetailResDTO.builder()
                .activityDate(LocalDate.now())
                .partnerName("partnerName")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .activityDivision(0)
                .review("review")
                .build();

        feedDetailResDTO.setMapPicture(mapCaptureResDTO());

        return feedDetailResDTO;
    }

    private List<MapCaptureDTO.MapCaptureResDTO> mapCaptureResDTO() {
        ArrayList<MapCaptureDTO.MapCaptureResDTO> mapCaptureResDTOs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mapCaptureResDTOs.add(MapCaptureDTO.MapCaptureResDTO.builder()
                    .mapCaptureId((long)i+1)
                    .lat("lat")
                    .lon("lon")
                    .timestamp("timestamp")
                    .activityId(1L)
                    .build()
            );
        }

        return mapCaptureResDTOs;
    }

    private List<FeedDTO.FeedMainResDTO> feedMainResDTO() {
        ArrayList<FeedDTO.FeedMainResDTO> feedMainResDTOs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            feedMainResDTOs.add(
                    FeedDTO.FeedMainResDTO.builder()
                            .activityId((long)i)
                            .activityStatus(i)
                            .distance(1000L * i)
                            .activityDate(LocalDate.now().plusDays(i))
                            .activityDivision(i)
                            .partnerName("partnerName" + i)
                            .build()
            );
        }

        return feedMainResDTOs;
    }

}