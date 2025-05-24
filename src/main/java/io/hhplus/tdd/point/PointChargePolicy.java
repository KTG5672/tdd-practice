package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

/**
 * 포인트 충전 정책 검증 클래스
 * - validate 메서드를 이용하여 충전 요청이 유효한지 검증한다.
 */
@Service
public class PointChargePolicy {

    private static final long MAX_POINT = 1_000_000L;
    private static final long MIN_CHARGE_POINT = 1L;

    /**
     * 보유 포인트와 충전 포인트를 받아 충전 정책 검증한다.
     * - 충전 포인트가 1미만이면 IllegalArgumentException 발생 시킨다.
     * - 보유 포인트와 충전 포인트 합산이 최대 한도를 초과하면 IllegalStateException 발생 시킨다.
     * @param existingPoint 보유 포인트
     * @param chargePoint 충전 포인트
     */
    public void validate(long existingPoint, long chargePoint) {
        if (chargePoint < MIN_CHARGE_POINT) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }

        if (existingPoint + chargePoint > MAX_POINT) {
            throw new IllegalStateException("포인트 최대 한도를 초과 하였습니다.");
        }
    }

}
