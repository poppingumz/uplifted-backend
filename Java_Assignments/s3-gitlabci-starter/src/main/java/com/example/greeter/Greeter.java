package com.example.greeter;

public class Greeter {
    public String hello(String name) {
        if (name == null || name.isBlank()) {
            return "Hello dear! What's your name?";
        }

        return "Hello " + name + "!";
    }
}
