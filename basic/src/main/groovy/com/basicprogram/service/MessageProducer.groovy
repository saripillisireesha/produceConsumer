package com.basicprogram.service

import com.basicprogram.entity.User
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface MessageProducer {

    @Topic("sri-topic")
    def sendMessage(def map)

}