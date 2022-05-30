package backend.server.controller;

import backend.server.DTO.myPage.MyPageDTO;
import backend.server.service.mypage.*;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class MyPageControllerTest {

    @InjectMocks
    private MyPageController myPageController;

    @Mock
    private MyPageMainService myPageMainService;
    @Mock
    private MyPageChangeService myPageChangeService;
    @Mock
    private MyPagePartnerInfoService myPagePartnerInfoService;
    @Mock
    private PartnerCreationService partnerCreationService;
    @Mock
    private PartnerInfoChangeService partnerInfoChangeService;
    @Mock
    private PartnerDeleteService partnerDeleteService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(myPageController).build();
    }

    @Test
    @DisplayName("마이페이지 메인 조회 성공")
    void myPageMainSuccess() throws Exception {
        // given
        String request = "stdId";
        MyPageDTO.MyPageMainResDTO response = myPageMainResDTO();

        doReturn(response).when(myPageMainService).getMyPageMain(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mypage")
                        .param("stdId", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name").value(response.getName()))
                .andExpect(jsonPath("data.department").value(response.getDepartment()))
                .andExpect(jsonPath("data.totalTime").value(response.getTotalTime()))
                .andExpect(jsonPath("data.profilePicture").value(response.getProfilePicture()))
                .andExpect(jsonPath("message").value("조회 성공"))
                .andExpect(jsonPath("status").value(200));

    }

    @Test
    @DisplayName("마이페이지 변경 성공")
    void updateMemberInfoSuccess() throws Exception {
        // given
        MyPageDTO.MyPageChangeReqDTO request = myPageChangeReqDTO();
        String response = request.getStdId();

        doReturn(response).when(myPageChangeService).updateMemberInfo(any(MyPageDTO.MyPageChangeReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/mypage/change")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("회원 정보 수정 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("마이페이지 파트너 정보 조회 성공")
    void findPartnersInfoSuccess() throws Exception {
        // given
        String request = "stdId";
        List<MyPageDTO.MyPagePartnerListResDTO> response = myPagePartnerListResDTOs();

        doReturn(response).when(myPagePartnerInfoService).findPartnersInfo(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mypage/partnerInfo")
                        .param("stdId", request)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isNotEmpty())
                .andExpect(jsonPath("message").value("파트너 리스트 출력 완료"))
                .andExpect(jsonPath("status").value(200))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < 2; i++) {
            JSONObject myPagePartnerListResDTOJson = data.getJSONObject(i);
            assertThat(myPagePartnerListResDTOJson.getString("partnerName")).isEqualTo(response.get(i).getPartnerName());
            assertThat(myPagePartnerListResDTOJson.getLong("partnerId")).isEqualTo(response.get(i).getPartnerId());
            assertThat(myPagePartnerListResDTOJson.getString("partnerDetail")).isEqualTo(response.get(i).getPartnerDetail());
            assertThat(myPagePartnerListResDTOJson.getString("partnerBirth")).isEqualTo(response.get(i).getPartnerBirth());
        }
    }

    @Test
    @DisplayName("마이페이지 파트너 세부 정보 조회 성공")
    void findPartnerDetailSuccess() throws Exception {
        // given
        Long request = 1L;
        MyPageDTO.MyPagePartnerDetailResDTO response = myPagePartnerDetailResDTO();

        doReturn(response).when(myPagePartnerInfoService).findPartnerDetail(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mypage/partnerInfo/detail")
                        .param("partnerId", String.valueOf(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.partnerName").value(response.getPartnerName()))
                .andExpect(jsonPath("data.partnerDetail").value(response.getPartnerDetail()))
                .andExpect(jsonPath("data.partnerBirth").value(response.getPartnerBirth()))
                .andExpect(jsonPath("data.gender").value(response.getGender()))
                .andExpect(jsonPath("data.selectionReason").value(response.getSelectionReason()))
                .andExpect(jsonPath("data.relationship").value(response.getRelationship()))
                .andExpect(jsonPath("message").value("파트너 세부 조회 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("파트너 저장 성공")
    void createPartnerSuccess() throws Exception {
        // given
        MyPageDTO.PartnerCreationReqDTO request = partnerCreationReqDTO();
        Long response = 1L;

        doReturn(response).when(partnerCreationService).createPartner(any(MyPageDTO.PartnerCreationReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/partner/create")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("partnerCreationReqDTO", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("파트너 저장 완료"))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("파트너 정보 수정 성공")
    void updatePartnerInfoSuccess() throws Exception {

        // given
        MyPageDTO.PartnerInfoChangeReqDTO request = partnerInfoChangeReqDTO();
        Long response = 1L;

        doReturn(response).when(partnerInfoChangeService).updatePartnerInfo(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/partner/change")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("partnerInfoChangeReqDTO", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("파트너 정보가 성공적으로 변경되었습니다."))
                .andExpect(jsonPath("status").value(200));
    }

    @Test
    @DisplayName("파트너 삭제 성공")
    void deletePartnerSuccess() throws Exception {
        // given
        Long request = 1L;
        Long response = 1L;

        doReturn(response).when(partnerDeleteService).deletePartner(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/partner/delete")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("partnerId", String.valueOf(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("파트너를 성공적으로 삭제했습니다."))
                .andExpect(jsonPath("status").value(200));
    }

    private MyPageDTO.PartnerInfoChangeReqDTO partnerInfoChangeReqDTO() throws IOException {
        return MyPageDTO.PartnerInfoChangeReqDTO.builder()
                .partnerId("1")
                .partnerName("partnerName")
                .partnerDetail("partnerDetail")
                .selectionReason("selectionReason")
                .relationship("relationship")
                .gender("gender")
                .partnerBirth("partnerBirth")
                .partnerPhoto(TestConst.getMockMultipartFile())
                .build();
    }

    private MyPageDTO.PartnerCreationReqDTO partnerCreationReqDTO() throws IOException {

        return MyPageDTO.PartnerCreationReqDTO.builder()
                .stdId("stdId")
                .partnerName("partnerName")
                .partnerDetail("partnerDetail")
                .partnerPhoto(TestConst.getMockMultipartFile())
                .selectionReason("selectionReason")
                .relationship("relationship")
                .gender("gender")
                .partnerBirth("1996/03/11")
                .build();
    }

    private MyPageDTO.MyPagePartnerDetailResDTO myPagePartnerDetailResDTO() {
        return MyPageDTO.MyPagePartnerDetailResDTO.builder()
                .partnerName("partnerName")
                .partnerDetail("partnerDetail")
                .partnerBirth("1996/03/11")
                .gender("gender")
                .selectionReason("selectionReason")
                .relationship("relationShip")
                .build();
    }

    private List<MyPageDTO.MyPagePartnerListResDTO> myPagePartnerListResDTOs() {

        List<MyPageDTO.MyPagePartnerListResDTO> myPagePartnerListResDTOs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            myPagePartnerListResDTOs.add(MyPageDTO.MyPagePartnerListResDTO.builder()
                    .partnerName("partnerName" + i)
                    .partnerId((long) i)
                    .partnerDetail("partnerDetail" + i)
                    .partnerBirth("partnerBirth" + i)
                    .build());
        }
        return myPagePartnerListResDTOs;
    }

    private MyPageDTO.MyPageChangeReqDTO myPageChangeReqDTO() throws IOException {

        return MyPageDTO.MyPageChangeReqDTO.builder()
                .stdId("stdId")
                .password("password")
                .department("department")
                .profilePicture(TestConst.getMockMultipartFile())
                .build();
    }

    private MyPageDTO.MyPageMainResDTO myPageMainResDTO() {
        return MyPageDTO.MyPageMainResDTO.builder()
                .name("name")
                .department("department")
                .totalTime(1000)
                .profilePicture("profilePicture")
                .build();
    }

}