package com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.service.PubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api")
public class PubRestController {
    @Autowired
    private PubService pubservcie;

    @GetMapping("/reg-topic")
    public boolean registerTopic(@RequestParam(name = "topic", required = true) String topic,
            @RequestParam(name = "client", required = true) String client,
            @RequestParam(name = "size", required = false, defaultValue = "0") int size) {
        return pubservcie.registerTopic(topic, client, size);
    }
}
