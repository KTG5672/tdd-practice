package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

/**
 * 사용자별 포인트 충전, 사용, 조회 기능을 제공하는 서비스 클래스
 *  - 충전, 사용 기능은 순차적으로 처리되어야 하므로 ReentrantLock 을 사용하여 동시성 제어
 *  - 사용자 ID 를 사용하여 개별 락으로 충전, 사용 요청의 동시성 제어
 */
@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointChargePolicy pointChargePolicy;
    private final PointUsePolicy pointUsePolicy;
    private final PointHistoryService pointHistoryService;

    private final ConcurrentHashMap<Long, ReentrantLock> userPointLocks = new ConcurrentHashMap<>();

    public PointService(UserPointTable userPointTable, PointChargePolicy pointChargePolicy,
        PointUsePolicy pointUsePolicy, PointHistoryService pointHistoryService) {
        this.userPointTable = userPointTable;
        this.pointChargePolicy = pointChargePolicy;
        this.pointUsePolicy = pointUsePolicy;
        this.pointHistoryService = pointHistoryService;
    }

    /**
     * 포인트 충전 기능
     * - ReentrantLock 을 이용하여 동시성 제어
     * - 충전 정책을 검증 후 포인트를 합산하여 저장
     * @param id 사용자 ID
     * @param chargePoint 충전 포인트
     * @return UserPoint 충전 후 포인트 상태
     */
    public UserPoint charge(Long id, Long chargePoint) {
        ReentrantLock lock = getUserPointLock(id);
        lock.lock();
        try {
            UserPoint userPoint = userPointTable.selectById(id);
            long existingPoint = userPoint.point();

            pointChargePolicy.validate(existingPoint, chargePoint);

            long pointSum = existingPoint + chargePoint;
            UserPoint result = userPointTable.insertOrUpdate(id, pointSum);
            pointHistoryService.saveChargeHistory(id, chargePoint);
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 포인트 사용 기능
     * - ReentrantLock 을 이용하여 동시성 제어
     * - 사용 정책을 검증 후 포인트를 차감하여 저장
     * @param id 사용자 ID
     * @param usePoint 사용 포인트
     * @return UserPoint 사용 후 포인트 상태
     */
    public UserPoint use(Long id, Long usePoint) {
        ReentrantLock lock = getUserPointLock(id);
        lock.lock();
        try {
            UserPoint userPoint = userPointTable.selectById(id);
            long existingPoint = userPoint.point();

            pointUsePolicy.validate(existingPoint, usePoint);

            long resultPoint = existingPoint - usePoint;
            UserPoint result = userPointTable.insertOrUpdate(id, resultPoint);

            pointHistoryService.saveUseHistory(id, usePoint);
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 사용자 ID 별 포인트 정보 조회 기능
     * @param id 사용자 ID
     * @return UserPoint 해당 사용자 포인트 상태
     */
    public UserPoint getUserPoint(Long id) {
        return userPointTable.selectById(id);
    }

    /**
     * 사용자 ID 를 기반으로 받아 락을 반환
     * - 락 정보가 없으면 생성하여 반환
     * @param id 사용자 ID
     * @return ReentrantLock 사용자 별 락
     */
    private ReentrantLock getUserPointLock(Long id) {
        return userPointLocks.computeIfAbsent(id, k -> new ReentrantLock());
    }
}
