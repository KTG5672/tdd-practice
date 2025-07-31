package io.hhplus.tdd.point;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @MockBean
    private PointHistoryService pointHistoryService;

    /**
     * GET /point/{id}
     * 사용자 포인트 조회 시 정상적으로 조회되는지 검증한다.
     */
    @Test
    @DisplayName("GET /point/{id} - 포인트 조회 시 정상 응답을 반환한다.")
    void 포인트_조회_시_정상_응답을_반환한다() throws Exception {
        // given
        given(pointService.getUserPoint(1L))
            .willReturn(new UserPoint(1L, 1000L, 123456789L));

        // when & then
        mockMvc.perform(get("/point/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.point").value(1000))
            .andExpect(jsonPath("$.updateMillis").value(123456789L));
    }

    /**
     * GET /point/{id}/histories
     * 사용자 포인트 히스토리 조회 시 정상적으로 반환되는지 검증한다.
     */
    @Test
    @DisplayName("GET /point/{id}/histories - 포인트 내역 조회 시 정상 응답을 반환한다.")
    void 포인트_내역_조회_시_정상_응답을_반환한다() throws Exception {
        // given
        PointHistory history = new PointHistory(1L, 1L, 500L, TransactionType.CHARGE, 123456789L);
        given(pointHistoryService.getHistories(1L)).willReturn(List.of(history));

        // when & then
        mockMvc.perform(get("/point/1/histories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].userId").value(1))
            .andExpect(jsonPath("$[0].amount").value(500))
            .andExpect(jsonPath("$[0].type").value("CHARGE"))
            .andExpect(jsonPath("$[0].updateMillis").value(123456789L));
    }

    /**
     * PATCH /point/{id}/charge
     * 포인트 충전 요청 시 정상적으로 처리되는지 검증한다.
     */
    @Test
    @DisplayName("PATCH /point/{id}/charge - 포인트 충전 시 정상 응답을 반환한다.")
    void 포인트_충전_시_정상_응답을_반환한다() throws Exception {
        // given
        given(pointService.charge(1L, 1000L))
            .willReturn(new UserPoint(1L, 1000L, 123456789L));

        // when & then
        mockMvc.perform(patch("/point/1/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.point").value(1000))
            .andExpect(jsonPath("$.updateMillis").value(123456789L));
    }


    /**
     * PATCH /point/{id}/charge
     * 포인트 충전 요청 시 예외 발생시 에러코드와 메세지를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("PATCH /point/{id}/charge - 포인트 충전 예외 발생시 에러코드와 메세지를 반환한다.")
    void 포인트_충전_예외_발생시_에러_응답을_반환한다() throws Exception {
        // given
        given(pointService.charge(1L, -1L))
            .willThrow(new IllegalArgumentException("충전 금액은 0보다 커야 합니다."));

        // when & then
        mockMvc.perform(patch("/point/1/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("-1"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청 입니다: 충전 금액은 0보다 커야 합니다."));
    }

    /**
     * PATCH /point/{id}/use
     * 포인트 사용 요청 시 정상적으로 처리되는지 검증한다.
     */
    @Test
    @DisplayName("PATCH /point/{id}/use - 포인트 사용 시 정상 응답을 반환한다.")
    void 포인트_사용_시_정상_응답을_반환한다() throws Exception {
        // given
        given(pointService.use(1L, 500L))
            .willReturn(new UserPoint(1L, 500L, 123456789L));

        // when & then
        mockMvc.perform(patch("/point/1/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content("500"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.point").value(500))
            .andExpect(jsonPath("$.updateMillis").value(123456789L));
    }

    /**
     * PATCH /point/{id}/use
     * 포인트 사용 요청 예외 발생시 에러코드와 메세지를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("PATCH /point/{id}/charge - 포인트 사용 예외 발생시 에러코드와 메세지를 반환한다.")
    void 포인트_사용_예외_발생시_에러_응답을_반환한다() throws Exception {
        // given
        given(pointService.use(1L, 2000L))
            .willThrow(new IllegalStateException("포인트 잔고가 부족 합니다."));

        // when & then
        mockMvc.perform(patch("/point/1/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content("2000"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청 입니다: 포인트 잔고가 부족 합니다."));
    }
}