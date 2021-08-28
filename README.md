[![Tech](https://img.shields.io/static/v1?label=Java&message=11&color=009933)]()
[![Serverless declare](https://img.shields.io/static/v1?label=docker&message=mysql&color=ff69b4)]()
# Java coding test

A Java coding skill test for Merpay backend's/DataPlatform.

[日本語](SKILL_TEST.ja.md)

[English](SKILL_TEST.en.md)


# Phase 1
## Usage
### Hot to run
- Step 1. run `$ ./gradlew build` to build project
- Step 2. run `$ docker-compose up --build`

### API usage
#### Pub
- Step 1. Register topic
`GET http://localhost:8090/topic/register?topic=<TOPIC_NAME>&client=<PUB_CLIENT_NAME>`

- Step 2. Publish message
`POST http://localhost:8090/message/publish`
```JSON
# Body
{
    "topic": "<TOPIC_NAME>",
    "client": "<PUB_CLIENT_NAME>",
    "content": "<ANY_CONTENT>"
}
```

#### Sub
- Step 1. Subscribe
`GET http://localhost:8090/topic/subscribe?topic=<TOPIC_NAME>&client=<SUB_CLIENT_NAME>`
- Step 2. Get message
`GET http://localhost:8090/message/get?topic=<TOPIC_NAME>&client=<SUB_CLIENT_NAME>`
- Step 3. Ack message
`POST http://localhost:8090/message/ack`
```JSON
# Body
{
    "topic": "<TOPIC_NAME>",
    "client": "<SUB_CLIENT_NAME>",
    "msgId": long number id
}
```
#### Check in DB
`http://localhost:8888/`

server is your local router ip e.x. `192.168.x.x`

user name is `root`

pass word is `root`

db is `merpay`

## Architecture
<img src="./image/phase1.jpg" width="400px">

- Web application is based in Spring boot
- DB is using MySql
- Each topic will create a dedicated table
- Client use RESTful http endpoint to access the server API
