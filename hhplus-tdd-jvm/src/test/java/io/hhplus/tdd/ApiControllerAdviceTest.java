package io.hhplus.tdd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TestExceptionController.class)
@Import(ApiControllerAdvice.class)
class ApiControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * ApiControllerAdvice.handleIllegalArgumentException 테스트 IllegalArgumentException 발생시 HTTP Code
     * 400과 에러 메세지를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("IllegalArgumentException 발생시 400 응답과 메시지를 반환한다")
    void IllegalArgumentException_발생시_400_응답과_메시지를_반환한다() throws Exception {
        // given
        String url = "/test/illegal-argument";

        // when
        ResultActions perform = mockMvc.perform(get(url));

        // then
        perform.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청 입니다: 잘못된 값입니다."));
    }

    /**
     * ApiControllerAdvice.handleIllegalStateException 테스트 IllegalStateException 발생시 HTTP Code 400과
     * 에러 메세지를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("IllegalStateException 발생시 400 응답과 메시지를 반환한다.")
    void IllegalStateException_발생시_400_응답과_메시지를_반환한다() throws Exception {
        // given
        String url = "/test/illegal-state";

        // when
        ResultActions perform = mockMvc.perform(get(url));

        // then
        perform.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청 입니다: 상태가 잘못되었습니다."));
    }

    /**
     * ApiControllerAdvice.handleException 테스트
     * 예기치 못한 예외 발생시 HTTP Code 500과 기본 에러 메시지를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("Exception 발생시 500 응답과 기본 메시지를 반환한다.")
    void Exception_발생시_500_응답과_메시지를_반환한다() throws Exception {
        // given
        String url = "/test/exception";

        // when
        ResultActions perform = mockMvc.perform(get(url));

        // then
        perform.andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("500"))
            .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }
}