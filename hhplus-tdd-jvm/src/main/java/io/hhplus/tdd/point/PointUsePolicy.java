package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

/**
 * 포인트 사용 정책 검증 클래스
 * - validate 메서드를 이용하여 사용 요청이 유효한지 검증한다.
 */
@Service
public class PointUsePolicy {

    /**
     * 보유 포인트와 사용 포인트를 받아 사용 정책 검증한다.
     * - 보유 포인트가 부족하면 IllegalStateException을 발생 시킨다.
     * @param existingPoint 보유 포인트
     * @param usePoint 사용 포인트
     */
    public void validate(long existingPoint, long usePoint) {
        if (existingPoint < usePoint) {
            String msg = String.format("보유 포인트가 부족합니다. 보유 포인트 : %d, 사용 포인트 : %d", existingPoint,
                usePoint);
            throw new IllegalStateException(msg);
        }
    }

}
