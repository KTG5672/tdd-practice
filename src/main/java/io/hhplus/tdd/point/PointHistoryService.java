package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 포인트 사용 내역을 저장하고 조회하는 서비스 클래스
 */
@Service
public class PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    public PointHistoryService(PointHistoryTable pointHistoryTable) {
        this.pointHistoryTable = pointHistoryTable;
    }

    /**
     * 충전 내역을 포인트 내역 테이블에 저장한다.
     * @param userId 사용자 ID
     * @param chargePoint 충전 포인트
     */
    public void saveChargeHistory(long userId, long chargePoint) {
        pointHistoryTable.insert(userId, chargePoint, TransactionType.CHARGE, System.currentTimeMillis());
    }

    /**
     * 사용 내역을 포인트 내역 테이블에 저장한다.
     * @param userId 사용자 ID
     * @param usePoint 사용 포인트
     */
    public void saveUseHistory(long userId, long usePoint) {
        pointHistoryTable.insert(userId, usePoint, TransactionType.USE, System.currentTimeMillis());
    }

    /**
     * 사용자 ID 로 각 사용자의 충전/사용 내역을 조회한다.
     * @param userId 사용자 ID
     * @return List<PointHistory> 사용자 별 내역 리스트
     */
    public List<PointHistory> getHistories(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

}
