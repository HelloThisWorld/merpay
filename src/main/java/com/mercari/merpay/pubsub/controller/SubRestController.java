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

@RestController
@RequestMapping("/")
public class SubRestController {
    @Autowired
    private SubService subService;

    @GetMapping("topic/subscribe")
    public boolean subTopic(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client) {
        return subService.subscribeTopic(topic, client);
    }

    @GetMapping("message/get")
    public Message getMessage(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client) {
        return subService.getMessage(topic, client);
    }

    @PostMapping(value = "message/ack", consumes = "application/json", produces = "application/json")
    public boolean ackMessage(@RequestBody Message body) {
        return subService.ackMessage(body);
    }
}
