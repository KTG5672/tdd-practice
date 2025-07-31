package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.hhplus.tdd.database.PointHistoryTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

    @Mock
    PointHistoryTable pointHistoryTable;

    PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        pointHistoryService = new PointHistoryService(pointHistoryTable);
    }

    /**
     * 포인트 충전 히스토리를 정상적으로 저장한다.
     * 충전 히스토리 저장 메서드 실행 시 충전 내역을 테이블에 저장하는지 검증한다.
     */
    @Test
    @DisplayName("포인트 충전 히스토리를 정상적으로 저장한다.")
    void 포인트_충전_히스토리를_정상적으로_저장한다() {
        // given
        long userId = 1L;
        long chargePoint = 2_000L;
        ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        long historyId = 1L;
        long updateMillis = System.currentTimeMillis();
        given(pointHistoryTable.insert(eq(userId), eq(chargePoint), eq(TransactionType.CHARGE), anyLong())).willReturn(
            new PointHistory(historyId, userId, chargePoint, TransactionType.CHARGE, updateMillis));
        // when
        pointHistoryService.saveChargeHistory(userId, chargePoint);

        // then
        verify(pointHistoryTable).insert(eq(userId), eq(chargePoint), eq(TransactionType.CHARGE), timeCaptor.capture());
        assertThat(timeCaptor.getValue()).isNotNull();
    }

    /**
     * 포인트 사용 히스토리를 정상적으로 저장한다.
     * 사용 히스토리 저장 메서드 실행 시 충전 내역을 테이블에 저장하는지 검증한다.
     */
    @Test
    @DisplayName("포인트 사용 히스토리를 정상적으로 저장한다.")
    void 포인트_사용_히스토리를_정상적으로_저장한다() {
        // given
        long userId = 1L;
        long usePoint = 2_000L;
        ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        long historyId = 1L;
        long updateMillis = System.currentTimeMillis();
        given(pointHistoryTable.insert(eq(userId), eq(usePoint), eq(TransactionType.USE), anyLong())).willReturn(
            new PointHistory(historyId, userId, usePoint, TransactionType.USE, updateMillis));
        // when
        pointHistoryService.saveUseHistory(userId, usePoint);

        // then
        verify(pointHistoryTable).insert(eq(userId), eq(usePoint), eq(TransactionType.USE), timeCaptor.capture());
        assertThat(timeCaptor.getValue()).isNotNull();
    }
}