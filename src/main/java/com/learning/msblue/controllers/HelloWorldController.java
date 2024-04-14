package com.learning.msblue.controllers;


import com.learning.msblue.feign.Downstream2CurrClient;
import com.learning.msblue.feign.Downstream2NexClient;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("hello")
public class HelloWorldController {
    @Autowired
    private Downstream2CurrClient downstream2CurrClient;

    @Autowired
    private Downstream2NexClient downstream2NexClient;

    @Lazy
    @Autowired
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.instance.instanceId}")
    private String eurekaInstanceId;

    @GetMapping("greet")
    public ResponseEntity<String> sayHello(
            @RequestHeader(value = "x-call-target", required = false, defaultValue = "current") String callTarget
    ) {
        String responseBody = String.format("%s:%s says hello", this.applicationName, this.eurekaInstanceId);

        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .header("x-call-target", callTarget)
                .body(responseBody);
    }

    @GetMapping("hey")
    public ResponseEntity<String> sayHey(
            @RequestHeader(value = "x-call-target", required = false, defaultValue = "current") String callTarget
    ) {

        if (callTarget.equals("current")) {
            return this.downstream2CurrClient.sayHello(callTarget);
        }

        Optional<Application> nextApplication = Optional
                .ofNullable(this.eurekaClient
                        .getApplication("ms-blue-downstream2"));

        if (nextApplication.isEmpty()) {
            return this.downstream2CurrClient.sayHello(callTarget);
        }

        Optional<InstanceInfo> nextApplicationInstanceInfo = Optional
                .ofNullable(nextApplication.get().getByInstanceId("next"));

        if (nextApplicationInstanceInfo.isEmpty()) {
            return this.downstream2CurrClient.sayHello(callTarget);
        }

        if (nextApplicationInstanceInfo.get().getStatus().equals(InstanceInfo.InstanceStatus.UP)) {
            return this.downstream2NexClient.sayHello(callTarget);
        }


        return this.downstream2CurrClient.sayHello(callTarget);
    }
}
