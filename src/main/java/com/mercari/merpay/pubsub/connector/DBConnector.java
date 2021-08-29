package com.mercari.merpay.pubsub.connector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import com.mercari.merpay.pubsub.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DBConnector {
    private static final Logger LOOGER = LoggerFactory.getLogger(DBConnector.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${queue.properties.table.schema}")
    private String queueTableSchema;

    @Value("${queue.properties.sub_registry.table.schema}")
    private String subRegistrySchema;

    @Value("${queue.properties.receipt.table.schema}")
    private String receiptSchema;

    @Value("${topic.properties.table.schema}")
    private String topicTableSchema;

    @Value("${topic.properties.defaultsize}")
    private Integer defaultTopicSize;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute(queueTableSchema);
        jdbcTemplate.execute(subRegistrySchema);
        jdbcTemplate.execute(receiptSchema);
    }

    public boolean createTable(String name, String client, int topicSize) {
        if (isTopicExisted(name)) {
            return false;
        }

        String sql = String.format(topicTableSchema, name);
        jdbcTemplate.execute(sql);
        jdbcTemplate.update("INSERT INTO topic_registry(client, queue, queueSize) VALUES (?,?,?)", client, name,
                topicSize == 0 ? defaultTopicSize : topicSize);
        return true;
    }

    public boolean insertMessage(Message message) {
        if (!isClientLegalToWrite(message.getTopic(), message.getClient())) {
            return false;
        }
        String sql = String.format("INSERT INTO %s(content, client, createTime) VALUES(?, ?, ?)", message.getTopic());
        return jdbcTemplate.update(sql, message.getContent(), message.getClient(), message.getCreateTime()) > 0;
    }

    public boolean updateSubscription(String topic, String client) {
        if (!isClientLegalToSubscribe(topic, client)) {
            return false;
        }
        String sql = "INSERT INTO topic_subscribe(queue, client) VALUES(?, ?)";
        return jdbcTemplate.update(sql, topic, client) > 0;
    }

    public Message getMessageFromQueue(String topic, String client) {
        String subTimeSql = "SELECT subscribeTime FROM topic_subscribe WHERE queue = ? AND client = ?";
        Timestamp subTime = jdbcTemplate.queryForObject(subTimeSql, Timestamp.class, topic, client);

        String messageSql = String.format(
                "SELECT * FROM %s WHERE msgId NOT IN (SELECT msgId FROM receipt WHERE queue = ? AND client = ? ) AND createTime > ? ORDER BY msgId ASC LIMIT 1",
                topic);

        try {
            return jdbcTemplate.queryForObject(messageSql, getRowMapper(topic), topic, client, subTime);
        } catch (EmptyResultDataAccessException e) {
            LOOGER.warn("Queue '{}'' is empty", topic);
            return new Message(topic, client, "Queue is empty", -1, null);
        }

    }

    public boolean insertToAck(Message message) {
        if (isAcked(message.getTopic(), message.getMsgId())) {
            return true;
        }

        String sql = "INSERT INTO receipt(client, queue, msgId) VALUES(?, ?, ?)";
        return jdbcTemplate.update(sql, message.getClient(), message.getTopic(), message.getMsgId()) > 0;
    }

    private boolean isTopicExisted(String topic) {
        return isTopicExisted(topic, null);
    }

    private boolean isTopicExisted(String topic, String client) {
        boolean result = false;
        if (client == null) {
            String sql = String.format("SELECT COUNT(*) AS count FROM %s", topic);
            try {
                int count = jdbcTemplate.queryForObject(sql, Integer.class);
                return count >= 0;
            } catch (Exception e) {
                LOOGER.info("Topic '{}' is not existed!", topic);
                return false;
            }
        }

        return result;
    }

    private boolean isClientLegalToWrite(String topic, String client) {
        String sql = "SELECT COUNT(*) FROM topic_registry WHERE queue = ? AND client = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, topic, client) > 0;
    }

    private boolean isClientLegalToSubscribe(String topic, String client) {
        String sql = "SELECT COUNT(*) FROM topic_subscribe WHERE queue = ? AND client = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, topic, client) <= 0;
    }

    private boolean isAcked(String topic, long msgId) {
        String sql = "SELECT COUNT(*) FROM receipt WHERE queue = ? AND msgId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, topic, msgId) > 0;
    }

    private RowMapper<Message> getRowMapper(final String topic) {
        return new RowMapper<Message>() {
            @Override
            public Message mapRow(ResultSet resultSet, int i) throws SQLException {
                Message msg = new Message();
                msg.setTopic(topic);
                msg.setMsgId(resultSet.getLong("msgId"));
                msg.setClient(resultSet.getString("client"));
                msg.setContent(resultSet.getString("content"));
                msg.setCreateTime(resultSet.getTimestamp("createTime"));

                return msg;
            }
        };
    }
}
