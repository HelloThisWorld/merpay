package com.mercari.merpay.pubsub.service;

import com.mercari.merpay.pubsub.connector.DBConnector;
import com.mercari.merpay.pubsub.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class SubService {
    @Autowired
    private DBConnector dbConnector;

    public boolean subscribeTopic(String topic, String client) {
        return dbConnector.updateSubscription(topic, client);
    }

    public Mono<Boolean> reactiveSubscribeTopic(String topic, String client) {
        return Mono.just(dbConnector.updateSubscription(topic, client));
    }

    public Message getMessage(String topic, String client) {
        return dbConnector.getMessageFromQueue(topic, client);
    }

    public Mono<Message> reactiveGetMessage(String topic, String client) {
        return Mono.just(dbConnector.getMessageFromQueue(topic, client));
    }

    public boolean ackMessage(Message jsonBody) {
        Message message = new Message();
        message.setMsgId(jsonBody.getMsgId());
        message.setTopic(jsonBody.getTopic());
        message.setClient(jsonBody.getClient());

        return dbConnector.insertToAck(message);
    }

    public Mono<Boolean> reactiveAckMessage(Message jsonBody) {
        Message message = new Message();
        message.setMsgId(jsonBody.getMsgId());
        message.setTopic(jsonBody.getTopic());
        message.setClient(jsonBody.getClient());

        return Mono.just(dbConnector.insertToAck(message));
    }
}
