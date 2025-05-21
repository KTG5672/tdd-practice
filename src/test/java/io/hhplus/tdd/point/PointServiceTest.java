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

    PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService(userPointTable);
    }

    /**
     * 처음 포인트를 충전하여 충전 정보를 반환받는다.
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
     * 충전 정보가 있는 사용자가 포인트를 충전하여 충전된 정보를 반환 한다.
     * 충전 이력이 있는 사용자에 대하여 정상적으로 충전 수행되고 기존 포인트와 충전 포인트가 정확히
     * 합산되어 반환되는지 검증한다.
     */
    @Test
    @DisplayName("충전 정보가 있는 사용자가 포인트를 충전하여 누적된 정보를 반환 한다.")
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

}
