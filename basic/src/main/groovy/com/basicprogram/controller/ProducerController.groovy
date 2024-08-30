package com.basicprogram.controller

import com.basicprogram.entity.User
import com.basicprogram.entity.UserRequest
import com.basicprogram.service.MessageProducer
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpRequest

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.inject.Inject

@Controller("/send")
class ProducerController {
    @Inject
    MessageProducer messageProducer
//    @Post
//    def sendUser(@Body User user){
//        return messageProducer.sendMessage(user)
//    }

    @Inject
    @Client("http://localhost:9090") // URL of the second microservice
    HttpClient httpClient


    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    @Status(HttpStatus.CREATED)
    def sendToConsumer(@Body UserRequest userRequest) {
        try{
            HttpResponse<User> response = httpClient.toBlocking().exchange(
                    HttpRequest.POST("/users", userRequest),
                    User
            )
            println response.body()
            if (response.status == HttpStatus.CREATED && response.body()) {
                User savedUser = response.body()

                // Send the saved user object via Kafka
                if (messageProducer.sendMessage(savedUser)) {
                    return HttpResponse.ok("Sent user object successfully through Kafka")
                } else {
                    return HttpResponse.serverError("Unable to send user object through Kafka")
                }
            } else {
                return HttpResponse.status(response.status).body("Failed to process user request in the other microservice")
            }
        } catch (Exception e) {
            return HttpResponse.serverError("An error occurred: ${e.message}")
        }

        }
    }

