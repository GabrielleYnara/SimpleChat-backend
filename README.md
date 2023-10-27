# SimpleChat-backend

## Repository for Frontend located at https://github.com/merrillhuang/SimpleChat-frontend
### A chatroom application. Users are able to create an account authenticated with Spring Security and JWT (or use a guest account), join/create chat rooms and chat with other users. Frontend display of rooms and chats dynamically updated periodically through HTTP requests to backend database.

## Tools and Technologies
### Wireframe.cc - wireframes
### Visual Paradigm - ERD diagram
### SpringBoot: Backend Framework
### Maven: for dependency management in project
### Cucumber and Rest Assured for testing

## Dependencies
### Maven - follow instructions to download and install at https://maven.apache.org/install.html

## How to Run Program
### Download Repository
### In command line: Navigate to project directory /simplechat
### Run “mvn spring-boot:run” in command line to start backend

## [User Stories](https://github.com/merrillhuang/SimpleChat-backend/blob/main/SimpleChat%20User%20Stories.pdf)

## ERD Diagram
![ERD](https://github.com/merrillhuang/SimpleChat-backend/blob/main/SimpleChat-erd.jpg)


## Endpoints
| Request Type | URL | Functionality | Access |
| ------------ | --- | ------------- | ------ |
| POST | http://localhost:8081/auth/register | Create a new User account | public |
| POST | http://localhost:8081/auth/login | Login a User and return a JWT | public |
| GET | http://localhost:8081/rooms | Return a list of all Rooms in the database | private |
| POST | http://localhost:8081/rooms | Create a new Room in the database | private |
| GET | http://localhost:8081/rooms/{roomId} | Returns the Room with roomId | private |
| POST | http://localhost:8081/rooms/{roomId} | Creates a new Chat in Room with roomId | private |
| PUT | http://localhost:8081/rooms/{roomId}/chats/{chatId} | Updates Chat with chatId in database | private |
| DELETE | http://localhost:8081/rooms/{roomId}/chats/{chatId} | Deletes Chat with chatId from database | private |
| GET | http://localhost:8081/{roomId}/chats | Returns a list of the usernames associated with each Chat in Room with roomId | private |

