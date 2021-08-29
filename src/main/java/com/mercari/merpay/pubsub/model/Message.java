package com.mercari.merpay.pubsub.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String topic;
    private String client;
    private String content;
    private long msgId;
    private Timestamp createTime;
}
