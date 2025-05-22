package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointServiceIntegrationTest {

    private PointService pointService;

    @BeforeEach
    void setUp() {
        UserPointTable userPointTable = new UserPointTable();
        PointChargePolicy chargePolicy = new PointChargePolicy();
        PointUsePolicy usePolicy = new PointUsePolicy();
        PointHistoryService historyService = new PointHistoryService(new PointHistoryTable());
        pointService = new PointService(userPointTable, chargePolicy, usePolicy, historyService);
    }

    /**
     * 실제 포인트 충전 처리의 동시성 보장을 위하여 실객체를 이용한 통합 테스트
     * 진행 여러 요청이 동시에 들어와도 충전 내역이 정확히 누적되는지 검증한다.
     */
    @Test
    @DisplayName("여러 요청이 동시에 들어와 포인트를 충전해도 정확하게 누적된다.")
    void 여러_요청이_동시에_들어와_포인트를_충전해도_정확하게_누적된다() throws Exception {

        // given
        int threadCount = 10;
        long userId = 1L;
        long chargeAmount = 100L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < threadCount; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    latch.await();
                    pointService.charge(userId, chargeAmount);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executor));
        }

        latch.countDown();
        for (CompletableFuture<Void> future : futures) {
            future.get();
        }

        // then
        UserPoint result = pointService.getUserPoint(userId);
        assertThat(result.point()).isEqualTo(threadCount * chargeAmount);
        executor.shutdown();
    }

    /**
     * 실제 포인트 사용의 동시성 보장을 위하여 실객체를 이용한 통합 테스트 진행
     * 여러 요청이 동시에 들어와도 포인트가 정확히 차감되는지 검증한다.
     */
    @Test
    @DisplayName("여러 요청이 동시에 들어와 포인트를 사용해도 정확하게 차감된다.")
    void 여러_요청이_동시에_들어와_포인트를_사용해도_정확하게_차감된다() throws Exception {

        // given
        int threadCount = 10;
        long userId = 1L;
        long usePoint = 100L;
        pointService.charge(userId, threadCount * usePoint);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < threadCount; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    latch.await();
                    pointService.use(userId, usePoint);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executor));
        }

        latch.countDown();
        for (CompletableFuture<Void> future : futures) {
            future.get();
        }

        // then
        UserPoint result = pointService.getUserPoint(userId);
        assertThat(result.point()).isEqualTo(0);
        executor.shutdown();
    }

}
