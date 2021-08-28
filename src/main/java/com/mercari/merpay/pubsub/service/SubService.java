package com.mercari.merpay.pubsub.service;

import com.mercari.merpay.pubsub.connector.DBConnector;
import com.mercari.merpay.pubsub.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubService {
    @Autowired
    private DBConnector dbConnector;

    public boolean subscribeTopic(String topic, String client) {
        return dbConnector.updateSubscription(topic, client);
    }

    public Message getMessage(String topic, String client) {
        return dbConnector.getMessageFromQueue(topic, client);
    }

    public boolean ackMessage(Message jsonBody) {
        Message message = new Message();
        message.setMsgId(jsonBody.getMsgId());
        message.setTopic(jsonBody.getTopic());
        message.setClient(jsonBody.getClient());

        return dbConnector.insertToAck(message);
    }
}
