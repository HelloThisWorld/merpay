package com.mercari.merpay.pubsub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TopicRegistry {
    private String client;
    private String queue;
    private int queueSize;
}
