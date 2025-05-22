package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    public PointHistoryService(PointHistoryTable pointHistoryTable) {
        this.pointHistoryTable = pointHistoryTable;
    }

    public void saveChargeHistory(long userId, long chargePoint) {
        pointHistoryTable.insert(userId, chargePoint, TransactionType.CHARGE, System.currentTimeMillis());
    }

    public void saveUseHistory(long userId, long usePoint) {
        pointHistoryTable.insert(userId, usePoint, TransactionType.USE, System.currentTimeMillis());
    }

}
