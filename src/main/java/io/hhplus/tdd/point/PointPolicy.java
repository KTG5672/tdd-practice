package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

@Service
public class PointPolicy {

    private static final long MAX_POINT = 1_000_000L;
    private static final long MIN_CHARGE_POINT = 1L;

    public void chargeValidate(long existingPoint, long chargePoint) {
        if (chargePoint < MIN_CHARGE_POINT) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }

        if (existingPoint + chargePoint > MAX_POINT) {
            throw new IllegalStateException("포인트 최대 한도를 초과 하였습니다.");
        }
    }
}
