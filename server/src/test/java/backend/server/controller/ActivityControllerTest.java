package backend.server.controller;

import backend.server.DTO.activity.ActivityDTO;
import backend.server.DTO.activity.PartnerDTO;
import backend.server.DTO.auth.TokenDTO;
import backend.server.exception.activityService.ActivityAbnormalDoneWithoutMinimumDistanceException;
import backend.server.exception.activityService.ActivityAbnormalDoneWithoutMinimumTimeException;
import backend.server.service.activity.ActivityCreationService;
import backend.server.service.activity.ActivityDeleteService;
import backend.server.service.activity.ActivityEndService;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    @InjectMocks
    private ActivityController activityController;

    @Mock
    private ActivityCreationService activityCreationService;

    @Mock
    private ActivityEndService activityEndService;

    @Mock
    private ActivityDeleteService activityDeleteService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
    }

    @Test
    @DisplayName("활동 생성 화면 성공 (파트너 불러오기)")
    void createActivityViewSuccess() throws Exception {
        // given
        String request = "stdId";
        List<PartnerDTO.PartnerListResDTO> response = partnerListResDTOList();

        doReturn(response).when(activityCreationService)
                .createActivity(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/activity/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("stdId", request)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("message").value("파트너 리스트 불러오기 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < 2; i++) {
            long partnerId = data.getJSONObject(i).getLong("partnerId");
            String partnerName = data.getJSONObject(i).getString("partnerName");
            String partnerDetail = data.getJSONObject(i).getString("partnerDetail");
            String partnerBirth = data.getJSONObject(i).getString("partnerBirth");
            int partnerDivision = data.getJSONObject(i).getInt("partnerDivision");

            PartnerDTO.PartnerListResDTO partnerListResDTO = response.get(i);
            Assertions.assertThat(partnerId).isEqualTo(partnerListResDTO.getPartnerId());
            Assertions.assertThat(partnerName).isEqualTo(partnerListResDTO.getPartnerName());
            Assertions.assertThat(partnerDetail).isEqualTo(partnerListResDTO.getPartnerDetail());
            Assertions.assertThat(partnerBirth).isEqualTo(partnerListResDTO.getPartnerBirth());
            Assertions.assertThat(partnerDivision).isEqualTo(partnerListResDTO.getPartnerDivision());
        }
    }

    @Test
    @DisplayName("활동 생성 완료 성공")
    void createActivitySuccess() throws Exception {
        // given
        ActivityDTO.ActivityCreationReqDTO request = activityCreationReqDTO();
        Long response = request.getPartnerId();

        doReturn(response).when(activityCreationService)
                .createActivityDone(any(ActivityDTO.ActivityCreationReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/activity/createActivity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("활동 생성 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("활동 종료 성공")
    void endActivitySuccess() throws Exception {
        // given
        ActivityDTO.ActivityEndReqDTO request = activityEndReqDTO();
        Long response = request.getActivityId();

        doReturn(response).when(activityEndService)
                .endActivity(any(ActivityDTO.ActivityEndReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/activity/end")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("저장이 완료 되었습니다."))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("활동 삭제 성공")
    void deleteActivitySuccess() throws Exception {
        // given
        Long request = 1L;
        Long response = 1L;

        doReturn(response).when(activityDeleteService)
                .deleteActivity(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/activity/delete")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("activityId", String.valueOf(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("성공적으로 삭제 되었습니다."))
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("활동 비정상 종료 시 학번 리턴 성공")
    void getStdIdFromTokenSuccess() throws Exception {
        // given
        TokenDTO request = tokenDTO();
        String response = "response";

        doReturn(response).when(activityEndService)
                .getStdIdFromToken(any(TokenDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/returnId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    private TokenDTO tokenDTO() {
        return TokenDTO.builder()
                .token("tokenId")
                .build();
    }

    private ActivityDTO.ActivityEndReqDTO activityEndReqDTO() throws IOException {

        String fileName = "ActivityEndImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        return ActivityDTO.ActivityEndReqDTO.builder()
                .activityId(1L)
                .distance(5000L)
                .map("{lat: 37.6440559, lon: 127.11006579999999, timestamp: 1621825328595}," +
                        "{lat: 37.6441113, lon: 127.11005929999997, timestamp: 1621825538522}")
                .endPhoto(mockMultipartFile)
                .endTime("20220519040000")
                .checkNormalQuit(0)
                .build();
    }

    private ActivityDTO.ActivityCreationReqDTO activityCreationReqDTO() throws IOException {

        String fileName = "ActivityStartImage";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityEndImage.png";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        return ActivityDTO.ActivityCreationReqDTO.builder()
                .stdId("stdId")
                .partnerId(1L)
                .startPhoto(mockMultipartFile)
                .build();
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    private List<PartnerDTO.PartnerListResDTO> partnerListResDTOList() {

        List<PartnerDTO.PartnerListResDTO> partnerListResDTOList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            partnerListResDTOList.add(PartnerDTO.PartnerListResDTO.builder()
                    .partnerId((long) i)
                    .partnerName("partnerName" + i)
                    .partnerDetail("partnerDetail" + i)
                    .partnerBirth("1996/01/01")
                    .partnerDivision(i)
                    .build());
        }

        return partnerListResDTOList;
    }




}