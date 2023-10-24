Feature: AuthController Endpoints Test

  Scenario: User able to create account and login
    Given The User tries to log in with a username and password combo that exists in the database
    Then The User is logged in and receives a JWT
    When A new User registers for an account
    Then The new User is saved to the database
    Then The new User is able to log in and receives a JWT