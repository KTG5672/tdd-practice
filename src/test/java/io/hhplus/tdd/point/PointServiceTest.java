package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    UserPointTable userPointTable;

    @Mock
    PointChargePolicy pointChargePolicy;

    @Mock
    PointUsePolicy pointUsePolicy;

    PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService(userPointTable, pointChargePolicy, pointUsePolicy);
    }

    /**
     * 사용자 포인트 조회시 정상적으로 반환한다.
     * 사용자 포인트 ID 를 사용하여 정보를 조회하고 정상적으로 반환하는지 검증한다.
     */
    @Test
    @DisplayName("사용자 포인트 조회시 정상적으로 반환한다.")
    void 사용자_포인트_조회시_정상적으로_반환한다() {
        // given
        Long id = 1L;
        UserPoint expected = new UserPoint(id, 1_000, System.currentTimeMillis());
        given(userPointTable.selectById(id)).willReturn(expected);

        // when
        UserPoint result = pointService.getUserPoint(id);

        // then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(expected.point());
    }

    /**
     * 포인트 정보가 없는 사용자의 포인트 조회시 0을 반환한다.
     * 포인트 정보가 없는 사용자의 정보를 조회할시 0포인트로 초기화되어 반환하는지 검증한다.
     */
    @Test
    @DisplayName("포인트 정보가 없는 사용자의 포인트 조회시 0을 반환한다.")
    void 포인트_정보가_없는_사용자의_포인트_조회시_0을_반환한다() {
        // given
        Long id = 1L;
        given(userPointTable.selectById(id)).willReturn(UserPoint.empty(1L));

        // when
        UserPoint result = pointService.getUserPoint(id);

        // then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(0);
    }

    /**
     * 처음 포인트를 충전하여 충전 정보를 반환한다.
     * 충전 이력이 없는 사용자에 대하여 정상적으로 충전 수행되고 그 결과가 정확히 반환되는지 검증한다.
     */
    @Test
    @DisplayName("처음 포인트를 충전하여 충전된 정보를 반환한다.")
    void 처음_포인트를_충전하여_충전정보를_반환한다() {

        // given
        Long id = 1L;
        Long point = 1_000L;
        long updateMillis = System.currentTimeMillis();
        given(userPointTable.selectById(id)).willReturn(UserPoint.empty(id));
        given(userPointTable.insertOrUpdate(id, point)).willReturn(new UserPoint(id, point,
            updateMillis));
        // when
        UserPoint result = pointService.charge(id, point);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(point);
    }


    /**
     * 충전 정보가 있는 사용자가 포인트를 충전하여 충전된 정보를 반환한다.
     * 충전 이력이 있는 사용자에 대하여 정상적으로 충전 수행되고 기존 포인트와 충전 포인트가 정확히
     * 합산되어 반환되는지 검증한다.
     */
    @Test
    @DisplayName("충전 정보가 있는 사용자가 포인트를 충전하여 누적된 정보를 반환한다.")
    void 포인트를_충전하여_누적된_정보를_반환한다() {

        // given
        Long id = 1L;
        Long existingPoint = 1_000L;
        Long chargePoint = 2_000L;
        given(userPointTable.selectById(id)).willReturn(new UserPoint(id, existingPoint, System.currentTimeMillis()));
        ArgumentCaptor<Long> pointCaptor = ArgumentCaptor.forClass(Long.class);
        given(userPointTable.insertOrUpdate(id, existingPoint + chargePoint)).willReturn(
            new UserPoint(id, existingPoint + chargePoint, System.currentTimeMillis()));

        // when
        UserPoint result = pointService.charge(id, chargePoint);

        // then
        verify(userPointTable).insertOrUpdate(eq(id), pointCaptor.capture());
        Long expectPoint = existingPoint + chargePoint;
        Long actualPoint = pointCaptor.getValue();
        assertThat(expectPoint).isEqualTo(actualPoint);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(actualPoint);
    }

    /**
     * 포인트를 사용하면 포인트가 감산되어 저장되며 그 정보를 반환한다.
     * 기존 포인트에서 사용 포인트를 차감하여 저장 되고, 정상적으로 잔여 포인트가 반영되는지 검증한다.
     */
    @Test
    @DisplayName("포인트를 사용하면 포인트가 감산되어 저장되며 그 정보를 반환한다.")
    void 포인트를_사용하면_포인트가_감산되며_그_정보를_반환한다() {
        // given
        Long id = 1L;
        Long usePoint = 2_000L;
        Long existingPoint = 3_000L;
        given(userPointTable.selectById(id)).willReturn(new UserPoint(id, existingPoint, System.currentTimeMillis()));
        ArgumentCaptor<Long> pointCaptor = ArgumentCaptor.forClass(Long.class);
        given(userPointTable.insertOrUpdate(id, existingPoint - usePoint)).willReturn(
            new UserPoint(id, existingPoint - usePoint, System.currentTimeMillis()));

        // when
        UserPoint result = pointService.use(id, usePoint);

        // then
        verify(userPointTable).insertOrUpdate(eq(id), pointCaptor.capture());
        Long expectedPoint = existingPoint - usePoint;
        Long actualPoint = pointCaptor.getValue();
        assertThat(expectedPoint).isEqualTo(actualPoint);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(expectedPoint);

    }


    /**
     * 포인트를 전부 사용하면 포인트가 0이 저장되며 그 정보를 반환한다.
     * 기존 포인트와 같은 포인트를 사용하여 잔여 포인트가 0이 되어 저장되고, 그 정보를 반환하는지 검증한다.
     */
    @Test
    @DisplayName("포인트를 전부 사용하면 포인트가 0이 저장되며 그 정보를 반환한다.(경계값 검증)")
    void 포인트를_전부_사용하면_포인트가_0이_저장되며_그_정보를_반환한다() {
        // given
        Long id = 1L;
        Long usePoint = 2_000L;
        long existingPoint = 2_000L;
        given(userPointTable.selectById(id)).willReturn(new UserPoint(id, existingPoint, System.currentTimeMillis()));
        ArgumentCaptor<Long> pointCaptor = ArgumentCaptor.forClass(Long.class);
        given(userPointTable.insertOrUpdate(id, 0)).willReturn(
            new UserPoint(id, 0, System.currentTimeMillis()));

        // when
        UserPoint result = pointService.use(id, usePoint);

        // then
        verify(userPointTable).insertOrUpdate(eq(id), pointCaptor.capture());
        Long expectedPoint = 0L;
        Long actualPoint = pointCaptor.getValue();
        assertThat(expectedPoint).isEqualTo(actualPoint);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(expectedPoint);

    }

}
