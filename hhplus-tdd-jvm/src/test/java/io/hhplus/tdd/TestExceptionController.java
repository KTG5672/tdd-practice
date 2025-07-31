package io.hhplus.tdd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class TestExceptionController {

    @GetMapping("/illegal-argument")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("잘못된 값입니다.");
    }

    @GetMapping("/illegal-state")
    public void throwIllegalState() {
        throw new IllegalStateException("상태가 잘못되었습니다.");
    }

    @GetMapping("/exception")
    public void throwException() throws Exception {
        throw new Exception("에러가 발생했습니다.");
    }

}