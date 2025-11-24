package com.example.greeter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreeterTest {

    @Test
    public void helloShouldReturnHelloGreetingWithName_WhenNameIsInformed() {
        // Arrange
        Greeter greeter = new Greeter();

        // Act - Execute the method to be tested
        String actual = greeter.hello("Joe");

        // Assert - Check if the method post-conditions is as expected
        assertEquals("Hello Joe!", actual);
    }

    @Test
    public void helloShouldReturnGenericMessage_WhenNameIsNull() {
        // Arrange
        Greeter greeter = new Greeter();

        // Acti
        String actual = greeter.hello(null);

        // Assert
        assertEquals("Hello dear! What's your name?", actual);
    }

    @Test
    public void helloShouldReturnGenericMessage_WhenNameIsBlankString() {
        // Arrange
        Greeter greeter = new Greeter();

        // Act
        String actual = greeter.hello("   ");

        // Assert
        assertEquals("Hello dear! What's your social security number?", actual);
    }
}
