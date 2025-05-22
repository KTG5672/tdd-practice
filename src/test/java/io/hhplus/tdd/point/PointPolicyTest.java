package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointPolicyTest {

    PointPolicy pointPolicy;

    @BeforeEach
    void setUp() {
        pointPolicy = new PointPolicy();
    }

    /**
     * 충전시 합산 포인트가 최대한도를 넘으면 예외를 발생시킨다.
     * 기존 포인트와 충전 포인트를 받아 합친 포인트가 최대한도를 넘을 경우 예외를 정상적으로 발생시키는지 검증한다.
     */
    @Test
    @DisplayName("충전시 합산 포인트가 최대한도를 넘으면 예외를 발생시킨다.")
    void 충전시_합산_포인트가_최대한도를_넘으면_예외를_발생시킨다() {
        // given
        long existingPoint = 800_000L;
        long chargePoint = 200_001L;

        // when
        var result = assertThatThrownBy(
            () -> pointPolicy.chargeValidate(existingPoint, chargePoint));

        // then
        result.isInstanceOf(IllegalStateException.class);
    }

    /**
     * 충전시 합산 포인트가 최대한도 이하면 통과한다.
     * 기존 포인트와 충전 포인트를 받아 합산 포인트가 최대한도 이하일 경우 정상적으로 통과하는지 검증한다.
     * 이때 합산 포인트가 최대한도와 같을 경우를 검증한다.
     */
    @Test
    @DisplayName("충전시 합산 포인트가 최대한도 이하면 통과한다.(경계 값 검증)")
    void 충전시_합산_포인트가_최대한도_이하면_통과한다() {
        // given
        long existingPoint = 800_000L;
        long chargePoint = 200_000L;

        // when
        var result = assertThatCode(
            () -> pointPolicy.chargeValidate(existingPoint, chargePoint));

        // then
        result.doesNotThrowAnyException();
    }
    /**
     * 충전시 충전 포인트가 1점 미만(0점 이거나 음수)일 경우 예외를 발생시킨다.
     * 충전 포인트의 값이 0점 미만일 경우 유효한 충전이 아니므로 예외가 발생 되는지 검증한다.
     */
    @Test
    @DisplayName("충전시 충전 포인트가 1점 미만이면 예외를 발생시킨다.")
    void 충전시_충전_포인트가_1점_미만이면_예외를_발생시킨다() {
        // given
        long existingPoint = 800_000L;
        long chargePoint = -1L;

        // when
        var result = assertThatThrownBy(
            () -> pointPolicy.chargeValidate(existingPoint, chargePoint));

        // then
        result.isInstanceOf(IllegalArgumentException.class);
    }
    /**
     * 충전시 충전 포인트가 1점 이상일 경우 통과한다.
     * 충전 포인트의 값이 1점 이상일 경우 유효한 충전이므로 정상적으로 통과되는지 검증한다.
     * 이때 충전 포인트를 최소인 1점으로 하여 검증한다.
     */
    @Test
    @DisplayName("충전시 충전 포인트가 1점 이상이면 통과한다.(경계값 검증)")
    void 충전시_충전할_포인트가_1점_이상이면_통과한다() {
        // given
        long existingPoint = 800_000L;
        long chargePoint = 1L;

        // when
        var result = assertThatCode(
            () -> pointPolicy.chargeValidate(existingPoint, chargePoint));

        // then
        result.doesNotThrowAnyException();
    }

}
