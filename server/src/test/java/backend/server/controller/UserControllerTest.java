package backend.server.controller;

import backend.server.DTO.user.UserDTO;
import backend.server.exception.GlobalExceptionAdvisor;
import backend.server.exception.userService.StdIdDuplicationException;
import backend.server.service.user.PasswordFindService;
import backend.server.service.user.SignUpService;
import backend.server.service.user.VerificationNumberSendService;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.BaseMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private SignUpService signUpService;

    @Mock
    private VerificationNumberSendService verificationNumberSendService;

    @Mock
    private PasswordFindService passwordFindService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DisplayName("회원 가입 성공")
    @Test
    void signUpSuccess() throws Exception {
        // given
        UserDTO.SignUpReqDTO request = signUpReqDTO();
        String response = request.getStdId();

        doReturn(response).when(signUpService)
                .signup(any(signUpReqDTO().getClass()));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data").value(response))
                .andExpect(jsonPath("message").value("회원가입이 성공적으로 완료되었습니다."))
                .andExpect(jsonPath("status").value(200));

        // {
        //    "message": "회원가입이 성공적으로 완료되었습니다.",
        //    "data": "newStdId",
        //    "status": 200
        //}
    }

    @Test
    @DisplayName("회원가입 시 인증코드 전송 성공")
    void sendVerifyCodeSuccess() throws Exception {
        // given
        String request = "test@test.com";
        UserDTO.VerificationNumberSendResDTO response = verificationNumberSendResDTO();

        doReturn(response).when(verificationNumberSendService)
                .sendVerificationNumber(any(String.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/signup/authNum")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", request)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.authNum").value("abcd123"))
                .andExpect(jsonPath("message").value("메일이 성공적으로 발송되었습니다."))
                .andExpect(jsonPath("status").value(200))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("비밀번호 찾기 성공")
    void findPasswordSuccess() throws Exception {
        // given
        final UserDTO.PasswordFindReqDTO request = passwordFindReqDTO();
        final UserDTO.PasswordFindResDTO response = passwordFindResDTO();

        doReturn(response).when(passwordFindService)
                .findPassword(any(UserDTO.PasswordFindReqDTO.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/findpassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions
                .andExpect(jsonPath("data.email").value(response.getEmail()))
                .andExpect(jsonPath("message").value("임시 비밀번호가 입력하신 이메일로 발송 되었습니다."))
                .andExpect(jsonPath("status").value("200"));
    }

    private UserDTO.PasswordFindResDTO passwordFindResDTO() {
        return UserDTO.PasswordFindResDTO.builder()
                .email("test@test.com")
                .build();
    }

    private UserDTO.PasswordFindReqDTO passwordFindReqDTO() {
        return UserDTO.PasswordFindReqDTO.builder()
                .stdId("testStdId")
                .birth("001122")
                .name("testName")
                .build();
    }


    private UserDTO.VerificationNumberSendResDTO verificationNumberSendResDTO() {
        return UserDTO.VerificationNumberSendResDTO.builder()
                .authNum("abcd123")
                .build();
    }


    private UserDTO.SignUpReqDTO signUpReqDTO() {
        return UserDTO.SignUpReqDTO.builder()
                .stdId("newStdId")
                .password("test")
                .email("test@test.com")
                .name("test_name")
                .department("test_department")
                .phoneNumber("01000001111")
                .birth("001231")
                .build();

    }

}