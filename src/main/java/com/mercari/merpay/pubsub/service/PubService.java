package com.mercari.merpay.pubsub.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mercari.merpay.pubsub.connector.DBConnector;
import com.mercari.merpay.pubsub.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PubService {
    private static final Logger LOOGER = LoggerFactory.getLogger(PubService.class);

    @Autowired
    private DBConnector dbConnector;

    public boolean registerTopic(String name, String client, int topicSize) {
        try {
            String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8.name());

            if (!validName(name)) {
                LOOGER.error("input name '{}' is invalid!", name);
                return false;
            }

            return dbConnector.createTable(decodedName, client, topicSize);
        } catch (UnsupportedEncodingException e) {
            LOOGER.error("input name error!", e);
            return false;
        }
    }

    public boolean publishMessage(Message jsonBody) {
        Message message = new Message();
        message.setTopic(jsonBody.getTopic());
        message.setClient(jsonBody.getClient());
        message.setContent(jsonBody.getContent());
        message.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return dbConnector.insertMessage(message);
    }

    private boolean validName(String name) {
        Pattern pattern = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_]*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
}
