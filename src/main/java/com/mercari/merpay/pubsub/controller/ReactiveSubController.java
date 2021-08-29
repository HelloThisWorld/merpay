package com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.model.Message;
import com.mercari.merpay.pubsub.service.SubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class ReactiveSubController {
    @Autowired
    private SubService subService;

    @GetMapping("topic/subscribe")
    public Mono<Boolean> subTopic(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client) {
        return subService.reactiveSubscribeTopic(topic, client);
    }

    @GetMapping("message/get")
    public Mono<Message> getMessage(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client) {
        return subService.reactiveGetMessage(topic, client);
    }

    @PostMapping(value = "message/ack", consumes = "application/json", produces = "application/json")
    public Mono<Boolean> ackMessage(@RequestBody Message body) {
        return subService.reactiveAckMessage(body);
    }
}
