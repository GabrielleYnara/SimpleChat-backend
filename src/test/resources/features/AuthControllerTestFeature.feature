Feature: AuthController Endpoints Test

  Scenario: User able to create account and login
    Given There are Users in the database
    When The User logs in with an existing username and password
    Then The User is logged in and receives a JWT
    When A new User registers for an account
    Then The User is saved to the database
    Then The User is able to log in and receives a JWT