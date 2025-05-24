package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointUsePolicyTest {

    PointUsePolicy pointUsePolicy = new PointUsePolicy();

    /**
     * 포인트 사용시 기존 포인트가 사용 포인트 미만이면 예외를 발생시킨다.
     * 사용 포인트가 기존 남아있는 포인트 보다 많을 경우 사용이 불가능하므로 예외가 발생 되는지 검증한다.
     */
    @Test
    @DisplayName("포인트 사용시 기존 포인트가 사용 포인트 미만이면 예외를 발생시킨다.")
    void 포인트_사용시_기존_포인트가_사용_포인트_미만이면_예외를_발생시킨다() {
        // given
        long existingPoint = 2_000L;
        long usePoint = 3_000L;

        // when
        var result = assertThatThrownBy(
            () -> pointUsePolicy.validate(existingPoint, usePoint));

        // then
        result.isInstanceOf(IllegalStateException.class);
    }


    /**
     * 포인트 사용시 기존 포인트가 사용 포인트 이상이면 통과한다.
     * 사용 포인트가 기존 남아있는 포인트보다 같거나 적을 경우 사용 가능하므로 정상적으로 통과 되는지 검증한다.
     * 이때 사용 포인트를 기존 포인트와 같은 값으로 검증한다.
     */
    @Test
    @DisplayName("포인트 사용시 기존 포인트가 사용 포인트 이상이면 통과한다.(경계값 검증)")
    void 포인트_사용시_기존_포인트가_사용_포인트_이상이면_통과한다() {
        // given
        long existingPoint = 1_000L;
        long usePoint = 1_000L;

        // when
        var result = assertThatCode(
            () -> pointUsePolicy.validate(existingPoint, usePoint));

        // then
        result.doesNotThrowAnyException();
    }

}