package com.mercari.merpay.pubsub.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api")
public class SubRestController {
    @GetMapping("/reg-topic")
    public String hello(HttpServletRequest request) {
        return "Hello wala";
    }
}
