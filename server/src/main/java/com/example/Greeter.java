package com.example;

/**
 * This is a class.
 *
 * This constructor is currently empty. You should either add initialization logic or provide a comment explaining why it's empty.
 */
public class Greeter {

  /**
   * This is a constructor.
   */
  public Greeter() {
 // You can add initialization logic here if needed
  }
/**
   * Greets the specified person.
   * This method takes the name of a person and returns a greeting message.
   *
   * @param someone The name of the person to greet.
   * @return A greeting message in the format "Hello, [someone]!.
   */
 * //TODO: Add javadoc comment
  public String greet(String someone) {
    return String.format("Hello, %s!", someone);
  }
}
