package com.learning.msblue.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "downstream2Curr")
public interface Downstream2CurrClient {
    @GetMapping("greet")
    ResponseEntity<String> sayHello(@RequestHeader("x-call-target") String callTarget);
}
