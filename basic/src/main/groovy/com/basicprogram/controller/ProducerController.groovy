
package com.basicprogram.controller

import com.basicprogram.entity.User
import com.basicprogram.entity.UserRequest
import com.basicprogram.service.MessageProducer
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpRequest

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
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
    @Client("http://localhost:9093") // URL of the second microservice
    HttpClient httpClient


    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    @Status(HttpStatus.CREATED)
    def sendToConsumer(@Body UserRequest userRequest) {
        try {
            HttpResponse<User> response = httpClient.toBlocking().exchange(
                    HttpRequest.POST("/users", userRequest),
                    User
            )
            println response.body()
            if (response.status == HttpStatus.CREATED && response.body()) {
                User savedUser = response.body()

                // Send the saved user object via Kafka
                if (messageProducer.sendMessage(savedUser)) {
                    return HttpResponse.ok(savedUser)
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
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/login")
        @Status(HttpStatus.OK)
        def loginUser(@Body UserRequest userRequest) {
            try{
                HttpResponse<User> response = httpClient.toBlocking().exchange(
                        HttpRequest.POST("/users/login", userRequest),
                        User
                )
                println response.body()
                if (response.status == HttpStatus.OK && response.body()) {
                    User savedUser = response.body()

                    // Send the saved user object via Kafka
                    if (messageProducer.sendMessage(savedUser)) {
                        return HttpResponse.ok(savedUser)
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
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Put("/update/{id}")
    @Status(HttpStatus.OK)
    def updateUser(@Body UserRequest userRequest, Long id) {
        try {
            HttpResponse<User> response = httpClient.toBlocking().exchange(
                    HttpRequest.PUT("/users/update/${id}", userRequest),
                    User
            )
            println response.body()
            if (response.status == HttpStatus.OK && response.body()) {
                User updatedUser = response.body()

                // Send the updated user object via Kafka
                if (messageProducer.sendMessage(updatedUser)) {
                    return HttpResponse.ok(updatedUser)
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
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Delete("/delete/{id}")
    @Status(HttpStatus.NO_CONTENT)
    def deleteUser(@PathVariable Long id) {
        try {

            HttpResponse<?> response = httpClient.toBlocking().exchange(
                    HttpRequest.DELETE("/users/${id}")
            )
            if (response.status == HttpStatus.NO_CONTENT) {
                    return HttpResponse.ok("deleted successfully")
                }  else {
                return HttpResponse.status(response.status).body("Failed to process user request in the other microservice")
            }
        } catch (Exception e) {
            return HttpResponse.serverError("An error occurred: ${e.message}")
        }
    }







}
