package com.learning.msblue.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "downstream2Next")
public interface Downstream2NexClient {
    @GetMapping("greet")
    ResponseEntity<String> sayHello(@RequestHeader("x-call-target") String callTarget);
}
