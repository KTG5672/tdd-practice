package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

@Service
public class PointUsePolicy {

    public void validate(long existingPoint, long usePoint) {
        if (existingPoint < usePoint) {
            throw new IllegalStateException("보유 포인트가 부족합니다.");
        }
    }

}
