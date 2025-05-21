package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint charge(Long id, Long point) {
        UserPoint userPoint = userPointTable.selectById(id);
        long pointSum = userPoint.point() + point;
        return userPointTable.insertOrUpdate(id, pointSum);
    }
}
