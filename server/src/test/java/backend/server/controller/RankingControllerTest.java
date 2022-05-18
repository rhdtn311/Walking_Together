package backend.server.controller;

import backend.server.DTO.ranking.RankingDTO;
import backend.server.service.ranking.RankingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RankingControllerTest {

    @InjectMocks
    private RankingController rankingController;

    @Mock
    private RankingService rankingService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(rankingController).build();
    }

    @Test
    @DisplayName("랭킹 불러오기 성공")
    void rankingSuccess() throws Exception {
        // given
        List<RankingDTO> response = rankingDTO();

        doReturn(response).when(rankingService)
                .getRanking();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/ranking")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("message").value("랭킹 불러오기 성공"))
                .andExpect(jsonPath("status").value("200"))
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < 5; i++) {
            String name = (String) data.getJSONObject(i).get("name");
            String stdId = (String) data.getJSONObject(i).get("stdId");
            String department = (String) data.getJSONObject(i).get("department");
            Long totalDistance = data.getJSONObject(i).getLong("totalDistance");

            assertThat(name).isEqualTo(response.get(i).getName());
            assertThat(stdId).isEqualTo(response.get(i).getStdId());
            assertThat(department).isEqualTo(response.get(i).getDepartment());
            assertThat(totalDistance).isEqualTo(response.get(i).getTotalDistance());
        }
    }

    private List<RankingDTO> rankingDTO() {

        List<RankingDTO> rankingDTOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rankingDTOs.add(
                    RankingDTO.builder()
                    .stdId("test " + i)
                    .name("test " + i)
                    .department("test " + i)
                    .totalDistance(1000L * i)
                    .build()
            );
        }

        return rankingDTOs;
    }

}