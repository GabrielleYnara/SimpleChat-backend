Feature: UserController Endpoints Test

  Scenario: User is able to retrieve a list of all Rooms in the database and create Rooms in the database
    Given The User is logged in
    When The User sends a request for all the Rooms in the database
    Then The User receives a list of Rooms in the database
    When The User tries to create a new Room
    Then The New Room is created

  Scenario: User is able to retrieve a list of all Chats for a Room and create, update, delete Chats for a Room
    Given The User is inside a specific Room
    Then The User should be able to get a list of all Chats for that Room
    When The User tries to create a new Chat
    Then The New Chat is created
    When The User tries to update a Chat that belongs to them
    Then The Chat is updated
    When The User tries to delete a Chat that belongs to them
    Then The Chat is deleted