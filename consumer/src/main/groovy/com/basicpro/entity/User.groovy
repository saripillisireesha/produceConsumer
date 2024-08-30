package com.basicpro.entity

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable
class User {
    Long id
    String firstName
    String lastName
    String email
    String password
    Long mobile
    User(Long id,String firstName, String lastName,String email, String password, Long mobile){
        this.id=id
        this.firstName=firstName
        this.lastName=lastName
        this.email=email
        this.password=password
        this.mobile=mobile
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobile=" + mobile +
                '}';
    }
}
