package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointChargePolicy pointChargePolicy;
    private final PointUsePolicy pointUsePolicy;
    private final PointHistoryService pointHistoryService;

    public PointService(UserPointTable userPointTable, PointChargePolicy pointChargePolicy,
        PointUsePolicy pointUsePolicy, PointHistoryService pointHistoryService) {
        this.userPointTable = userPointTable;
        this.pointChargePolicy = pointChargePolicy;
        this.pointUsePolicy = pointUsePolicy;
        this.pointHistoryService = pointHistoryService;
    }

    public UserPoint charge(Long id, Long chargePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointChargePolicy.validate(existingPoint, chargePoint);

        long pointSum = existingPoint + chargePoint;
        UserPoint result = userPointTable.insertOrUpdate(id, pointSum);
        pointHistoryService.saveChargeHistory(id, chargePoint);
        return result;
    }

    public UserPoint use(Long id, Long usePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointUsePolicy.validate(existingPoint, usePoint);

        long resultPoint = existingPoint - usePoint;
        UserPoint result = userPointTable.insertOrUpdate(id, resultPoint);

        pointHistoryService.saveUseHistory(id, usePoint);
        return result;
    }

    public UserPoint getUserPoint(Long id) {
        return userPointTable.selectById(id);
    }
}
