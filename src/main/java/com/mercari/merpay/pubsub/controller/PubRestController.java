package com.mercari.merpay.pubsub.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/rest/api")
public class PubRestController {
    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        return "Hello wala";
    }
}
