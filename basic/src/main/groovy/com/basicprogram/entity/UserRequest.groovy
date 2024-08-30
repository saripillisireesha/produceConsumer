package com.basicprogram.entity

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable
class UserRequest {
        String firstName
        String lastName
        String email
        String password
        Long mobile

        UserRequest(String firstName, String lastName, String password, String email, Long mobile) {
            this.firstName = firstName
            this.lastName = lastName
            this.password = password
            this.email = email
            this.mobile = mobile
        }


        @Override
        public String toString() {
            return "UserModel{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", mobile=" + mobile +
                    '}';
        }
    }



