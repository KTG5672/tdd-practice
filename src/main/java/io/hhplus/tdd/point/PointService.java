package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointChargePolicy pointChargePolicy;
    private final PointUsePolicy pointUsePolicy;

    public PointService(UserPointTable userPointTable, PointChargePolicy pointChargePolicy,
        PointUsePolicy pointUsePolicy) {
        this.userPointTable = userPointTable;
        this.pointChargePolicy = pointChargePolicy;
        this.pointUsePolicy = pointUsePolicy;
    }

    public UserPoint charge(Long id, Long chargePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointChargePolicy.validate(existingPoint, chargePoint);

        long pointSum = existingPoint + chargePoint;
        return userPointTable.insertOrUpdate(id, pointSum);
    }

    public UserPoint use(Long id, Long usePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointUsePolicy.validate(existingPoint, usePoint);

        long resultPoint = existingPoint - usePoint;
        return userPointTable.insertOrUpdate(id, resultPoint);
    }
}
