Feature: UserController Endpoints Test

  Scenario: User is able to retrieve a list of all Rooms in the database
    Given The User is logged in
    When The User sends a request for all the Rooms in the database
    Then The User receives a list of Rooms in the database

  Scenario: User able to create Rooms
    Given The User is logged in
    When The User tries to create a new Room
    Then The New Room is created