package backend.server.controller;

import backend.server.DTO.admin.AdminDTO;
import backend.server.DTO.common.MapCaptureDTO;
import backend.server.service.admin.ActivityInfoService;
import backend.server.service.admin.MemberInfoService;
import backend.server.service.admin.PartnerInfoService;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private MemberInfoService memberInfoService;

    @Mock
    private ActivityInfoService activityInfoService;

    @Mock
    private PartnerInfoService partnerInfoService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("학생 정보 조회 성공")
    void findMemberInfoSuccess() throws Exception {
        // given
        String request = "keyword";
        List<AdminDTO.MemberInfoResDTO> response = memberInfoResDTO();

        doReturn(response).when(memberInfoService)
                .findMemberInfo(any(String.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/userinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", request)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("message").value("조회 완료"))
                .andExpect(jsonPath("status").value("200"))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < 2; i++) {
            String name = (String) data.getJSONObject(i).get("name");
            String stdId = (String) data.getJSONObject(i).get("stdId");
            String department = (String) data.getJSONObject(i).get("department");
            String email = (String) data.getJSONObject(i).get("email");
            String birth = (String) data.getJSONObject(i).get("birth");
            String phoneNumber = (String) data.getJSONObject(i).get("phoneNumber");

            assertThat(name).isEqualTo(response.get(i).getName());
            assertThat(stdId).isEqualTo(response.get(i).getStdId());
            assertThat(department).isEqualTo(response.get(i).getDepartment());
            assertThat(email).isEqualTo(response.get(i).getEmail());
            assertThat(birth).isEqualTo(response.get(i).getBirth());
            assertThat(phoneNumber).isEqualTo(response.get(i).getPhoneNumber());
        }
    }

    @Test
    @DisplayName("활동 정보 조회 성공")
    void findActivityInfoSuccess() throws Exception {
        // given
        AdminDTO.ActivityInfoReqDTO request = activityInfoReqDTO();
        List<AdminDTO.ActivityInfoResDTO> response = activityInfoResDTO();

        doReturn(response).when(activityInfoService)
                .findActivityInfo(any(AdminDTO.ActivityInfoReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/activityInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
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
            Long activityId = data.getJSONObject(i).getLong("activityId");
            String stdId = (String) data.getJSONObject(i).get("stdId");
            String partnerName = (String) data.getJSONObject(i).get("partnerName");
            String stdName = (String)data.getJSONObject(i).get("stdName");
            String department = (String)data.getJSONObject(i).get("department");
            String activityDate = (String)data.getJSONObject(i).get("activityDate");
            String startTime = (String)data.getJSONObject(i).get("startTime");
            String endTime = (String)data.getJSONObject(i).get("endTime");
            Long totalDistance = data.getJSONObject(i).getLong("totalDistance");


            assertThat(activityId).isEqualTo(response.get(i).getActivityId());
            assertThat(stdId).isEqualTo(response.get(i).getStdId());
            assertThat(partnerName).isEqualTo(response.get(i).getPartnerName());
            assertThat(stdName).isEqualTo(response.get(i).getStdName());
            assertThat(activityDate).isEqualTo(response.get(i).getActivityDate());
            assertThat(startTime).isEqualTo(response.get(i).getStartTime());
            assertThat(endTime).isEqualTo(response.get(i).getEndTime());
            assertThat(department).isEqualTo(response.get(i).getDepartment());
            assertThat(totalDistance).isEqualTo(response.get(i).getTotalDistance());
        }
    }

    @Test
    @DisplayName("특별 활동 상세 조회 성공")
    void findActivityInfoDetailSuccess() throws Exception {
        // given
        Long request = 1L;
        AdminDTO.ActivityDetailInfoResDTO response = activityDetailInfoResDTO();

        doReturn(response).when(activityInfoService)
                .findActivityDetailInfo(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/activityInfo/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("activityId", String.valueOf(request))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.stdName").value(response.getStdName()))
                .andExpect(jsonPath("data.department").value(response.getDepartment()))
                .andExpect(jsonPath("data.stdId").value(response.getStdId()))
                .andExpect(jsonPath("data.partnerName").value(response.getPartnerName()))
                .andExpect(jsonPath("data.review").value(response.getReview()))
                .andExpect(jsonPath("data.totalDistance").value(response.getTotalDistance()))
                .andExpect(jsonPath("message").value("조회 완료"))
                .andExpect(jsonPath("status").value("200"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String resultJson = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(resultJson);
        JSONArray mapPictureJsonArray = jsonObject.getJSONObject("data").getJSONArray("mapPicture");
        for (int i = 0; i < 3; i++) {
            JSONObject mapPictureJsonObject = mapPictureJsonArray.getJSONObject(i);
            assertThat(mapPictureJsonObject.getLong("mapCaptureId")).isEqualTo(response.getMapPicture().get(i).getActivityId());
            assertThat(mapPictureJsonObject.getString("lat")).isEqualTo(response.getMapPicture().get(i).getLat());
            assertThat(mapPictureJsonObject.getString("lon")).isEqualTo(response.getMapPicture().get(i).getLon());
            assertThat(mapPictureJsonObject.getString("timestamp")).isEqualTo(response.getMapPicture().get(i).getTimestamp());
            assertThat(mapPictureJsonObject.getLong("activityId")).isEqualTo(response.getMapPicture().get(i).getActivityId());
        }
    }

    @Test
    @DisplayName("파트너 정보 조회 성공")
    void partnerInfoSuccess() throws Exception {
        // given
        String keywordParam = "keyword";
        String partnerDetailParam = "partnerDetail";

        List<AdminDTO.PartnerInfoResDTO> response = partnerInfoResDTO();

        doReturn(response).when(partnerInfoService)
                .getPartnerInfo(keywordParam, partnerDetailParam);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/partnerInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", keywordParam)
                        .param("partnerDetail", partnerDetailParam)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("message").value("조회 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        JSONArray resultJsonArray = new JSONObject(mvcResult.getResponse().getContentAsString())
                .getJSONArray("data");

        for (int i = 0; i < 2; i++) {
            JSONObject partnerInfoJsonObject = resultJsonArray.getJSONObject(i);
            assertThat(partnerInfoJsonObject.getString("stdId")).isEqualTo(response.get(i).getStdId());
            assertThat(partnerInfoJsonObject.getString("stdName")).isEqualTo(response.get(i).getStdName());
            assertThat(partnerInfoJsonObject.getString("department")).isEqualTo(response.get(i).getDepartment());
            assertThat(partnerInfoJsonObject.getString("partnerName")).isEqualTo(response.get(i).getPartnerName());
            assertThat(partnerInfoJsonObject.getString("partnerBirth")).isEqualTo(response.get(i).getPartnerBirth());
            assertThat(partnerInfoJsonObject.getString("gender")).isEqualTo(response.get(i).getGender());
            assertThat(partnerInfoJsonObject.getString("relation")).isEqualTo(response.get(i).getRelation());
            assertThat(partnerInfoJsonObject.getInt("partnerDivision")).isEqualTo(response.get(i).getPartnerDivision());
        }
    }

    private List<AdminDTO.PartnerInfoResDTO> partnerInfoResDTO() {
        List<AdminDTO.PartnerInfoResDTO> partnerInfoResDTOS = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            partnerInfoResDTOS.add(
                AdminDTO.PartnerInfoResDTO.builder()
                    .stdId("testStdId" + i)
                    .stdName("testStdName" + i)
                    .department("testDepartment" + i)
                    .partnerName("testPartnerName" + i)
                    .partnerBirth("2022/01/1" + i)
                    .gender("testGender" + i)
                    .relation("testRelation" + i)
                    .partnerDivision(i % 2 == 0 ? 0 : 1)
                    .build());
        }
        return partnerInfoResDTOS;
    }

    private AdminDTO.ActivityDetailInfoResDTO activityDetailInfoResDTO() {
        AdminDTO.ActivityDetailInfoResDTO response = new AdminDTO.ActivityDetailInfoResDTO("testStdId", "testDepartment", "testStdId", "testPartnerName",
                "testReview", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), 1000L);
        response.setTotalTime(LocalTime.now().plusHours(1));
        response.setMapPicture(mapCaptureResDTOS());
        return response;
    }

    private List<MapCaptureDTO.MapCaptureResDTO> mapCaptureResDTOS() {
        List<MapCaptureDTO.MapCaptureResDTO> mapCaptureResDTOS = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mapCaptureResDTOS.add(
                    MapCaptureDTO.MapCaptureResDTO.builder()
                            .activityId((long) i)
                            .mapCaptureId((long) i)
                            .lat(String.valueOf(i * 1234))
                            .lon(String.valueOf(i * 5678))
                            .timestamp(String.valueOf(i * 9876))
                            .build()
            );
        }
        return mapCaptureResDTOS;
    }

    private List<AdminDTO.ActivityInfoResDTO> activityInfoResDTO() {
        List<AdminDTO.ActivityInfoResDTO> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            list.add(AdminDTO.ActivityInfoResDTO.builder()
                    .activityId((long) i)
                    .stdId("testStdId" + i)
                    .partnerName("testPartnerName" + i)
                    .stdName("testStdName" + i)
                    .department("testDepartment" + i)
                    .activityDate(LocalDate.now().plusDays(i))
                    .startTime(LocalDateTime.now().plusDays(i))
                    .endTime(LocalDateTime.now().plusDays(i))
                    .totalDistance(1000L + i)
                    .build());
        }
        return list;
    }

    private AdminDTO.ActivityInfoReqDTO activityInfoReqDTO() {
        return AdminDTO.ActivityInfoReqDTO.builder()
                .keyword("test keyword")
                .from("2021/01/01")
                .to("2022/01/01")
                .activityDivision(1)
                .build();
    }

    private List<AdminDTO.MemberInfoResDTO> memberInfoResDTO() {
        List<AdminDTO.MemberInfoResDTO> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(AdminDTO.MemberInfoResDTO.builder()
                    .stdId("testStdId" + i)
                    .name("testName" + i)
                    .department("testDepart" + i)
                    .birth("001122" + i)
                    .email("test@test.com" + i)
                    .phoneNumber("01011112222" + i)
                    .build());
        }

        return list;
    }

}