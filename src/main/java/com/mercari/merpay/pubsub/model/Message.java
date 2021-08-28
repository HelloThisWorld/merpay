package com.mercari.merpay.pubsub.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {
    private String topic;
    private String client;
    private String content;
    private long msgId;
    private Timestamp createTime;
}
