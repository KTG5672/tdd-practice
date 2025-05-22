package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.database.PointHistoryTable;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointHistoryIntegrationTest {

    PointHistoryTable pointHistoryTable;
    PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        pointHistoryTable = new PointHistoryTable();
        pointHistoryService = new PointHistoryService(pointHistoryTable);
    }

    /**
     * 실제 포인트 내역 조회를 위하여 실객체를 이용하여 통합 테스트 진행
     * 충전/사용 내역을 저장하고 전체내역을 정상적으로 조회, 반환하는지 검증한다.
     */
    @Test
    @DisplayName("포인트 충전/사용 내역을 저장하고 전체내역을 조회한다.")
    void 포인트_충전_사용_내역을_저장하고_전체내역을_조회한다() {
        // given
        long userId = 1L;
        pointHistoryService.saveChargeHistory(userId, 3000);
        pointHistoryService.saveChargeHistory(userId, 4000);
        pointHistoryService.saveChargeHistory(userId, 5000);
        pointHistoryService.saveUseHistory(userId, 2000);
        pointHistoryService.saveUseHistory(userId, 3000);
        pointHistoryService.saveUseHistory(userId, 4000);

        // when
        List<PointHistory> histories = pointHistoryService.getHistories(userId);

        // then
        assertThat(histories).size().isEqualTo(6);
        assertThat(histories).filteredOn((history) -> history.type().equals(TransactionType.USE)).size().isEqualTo(3);
        assertThat(histories).filteredOn((history) -> history.type().equals(TransactionType.CHARGE)).size().isEqualTo(3);
        assertThat(histories.get(0).type()).isEqualTo(TransactionType.CHARGE);
        assertThat(histories.get(0).amount()).isEqualTo(3000);
        assertThat(histories.get(3).type()).isEqualTo(TransactionType.USE);
        assertThat(histories.get(3).amount()).isEqualTo(2000);

    }

}
