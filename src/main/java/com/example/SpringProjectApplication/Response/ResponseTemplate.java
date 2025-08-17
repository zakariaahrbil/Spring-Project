package com.example.SpringProjectApplication.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplate<T> {
    private String message;
    private T data;
    private boolean success;
}
