package com.example.demoApplication.Exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public  UserAlreadyExistsException(String message) {
        super(message);
    }
}
