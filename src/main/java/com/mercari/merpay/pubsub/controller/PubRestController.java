package com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.model.Message;
import com.mercari.merpay.pubsub.service.PubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync/")
public class PubRestController {
    @Autowired
    private PubService pubservcie;

    @GetMapping("topic/register")
    public boolean registerTopic(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client,
            @RequestParam(name = "size", required = false, defaultValue = "0") int size) {
        return pubservcie.registerTopic(topic, client, size);
    }

    @PostMapping(value = "message/publish", consumes = "application/json", produces = "application/json")
    public boolean pubMessage(@RequestBody Message body) {
        return pubservcie.publishMessage(body);
    }
}
