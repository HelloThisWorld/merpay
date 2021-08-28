package com.mercari.merpay.pubsub.connector;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBConnector {
    private static final Logger LOOGER = LoggerFactory.getLogger(DBConnector.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${queue.properties.table.schema}")
    private String queueTableSchema;

    @Value("${topic.properties.table.schema}")
    private String topicTableSchema;

    @Value("${topic.properties.defaultsize}")
    private Integer defaultTopicSize;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute(queueTableSchema);
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
}
