package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointChargePolicy pointChargePolicy;

    public PointService(UserPointTable userPointTable, PointChargePolicy pointChargePolicy) {
        this.userPointTable = userPointTable;
        this.pointChargePolicy = pointChargePolicy;
    }

    public UserPoint charge(Long id, Long chargePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointChargePolicy.validate(existingPoint, chargePoint);

        long pointSum = existingPoint + chargePoint;
        return userPointTable.insertOrUpdate(id, pointSum);
    }
}
