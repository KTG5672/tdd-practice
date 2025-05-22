package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointPolicy pointPolicy;

    public PointService(UserPointTable userPointTable, PointPolicy pointPolicy) {
        this.userPointTable = userPointTable;
        this.pointPolicy = pointPolicy;
    }

    public UserPoint charge(Long id, Long chargePoint) {
        UserPoint userPoint = userPointTable.selectById(id);
        long existingPoint = userPoint.point();

        pointPolicy.chargeValidate(existingPoint, chargePoint);

        long pointSum = existingPoint + chargePoint;
        return userPointTable.insertOrUpdate(id, pointSum);
    }
}
