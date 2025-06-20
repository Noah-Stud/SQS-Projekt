# API-Dokumentation

The API of the SQS-Messanger-App allows for the registration / log in of Users, creation, pulling and liking of Messages
and the creation of new Comments.

## 1. Authentication
### 1.1 Registration of a new User
- **Endpoint:** `POST /api/auth/v1/register`
- **Description:** Creation a new User.
- **Parameters:**
    ```json
    {
      "email": "abc@g.com",
      "password": "1234"
    }
    ```
- **Response:**
    - **Status 200**
        ```json
        {
          "status": "success",
          "message": "User registered successfully!",
          "payload": null
        }
        ```
    - **Status 400**
        ```json
        {
          "status": "fail",
          "message": "Error: Email is already in use!",
          "payload": null
        }
        ``` 
          
### 1.2 Login
- **Endpoint:** `POST /api/auth/v1/login`
- **Description:** LogIn as a new User.
- **Parameters:**
    ```json
    {
      "email": "abc@g.com",
      "password": "1234"
    }
    ```
- **Response:**
    - **Status 200**
      ```json
      {
        "status": "success",
        "message": "authenticated",
        "payload": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNAZy5jb20iLCJpYXQiOjE3NTAzNDkyNjksImV4cCI6MTc1MDQzNTY2OX0.jdztjjnbWDeqsBrSSkhWy8Cb_D9o7VDBDuAKs7PWjAI"
      }
      ```
    - **Status 401** (Wrong User-Details)
      ```json
      {
        "status": "fail",
        "message": "unauthenticated",
        "payload": null
      }
      ```

## 2. Users
### 2.1 Getting all Users
- **Endpoint:** `GET /api/user/v1/getAll`
- **Description:** Gets all registered Users.
- **Parameters:** ---
- **Response:**
    - **Status 200**
      ```json
      {
        "status": "success",
        "message": "authenticated",
        "payload": [
          {
            "email": "von@g.com"
          },
          {
            "email": "test@g.de"
          },
          {
            "email": "abc@g.com"
          }
        ]
      }
      ```

## 3. Messages
### 3.1 Inserting new Message
- **Endpoint:** `POST /api/message/v1/insert`
- **Description:** Creates a new Message.
- **Parameters:**
    ```
    Test Message'
    ```
- **Response:**
  - **Status 200**
    ```json
    {
      "status": "success",
      "message": "success",
      "payload": {
        "id": "cd5fb364-5dfc-4fed-9205-252cd80ee42b",
        "userEmail": "abc@g.com",
        "content": "Test Message",
        "comments": [],
        "createdAt": "2025-06-19T16:20:12.214260300Z",
        "quote": "Who looks outside, dreams; who looks inside, awakes. (Lolly Daskal)",
        "likes": []
        }
    }
    ```

### 3.2 Getting a Message by its ID
- **Endpoint:** `GET /api/message/v1/getById`
- **Description:** Gets a specific Message by its ID.
- **Parameters:**
    ```
    cd5fb364-5dfc-4fed-9205-252cd80ee42b
    ```
- **Response:**
    - **Status 200**
      ```json
      {
        "status": "success",
        "message": "success",
        "payload": {
          "id": "cd5fb364-5dfc-4fed-9205-252cd80ee42b",
          "userEmail": "abc@g.com",
          "content": "Test Message",
          "comments": [],
          "createdAt": "2025-06-19T16:20:12.214260300Z",
          "quote": "Who looks outside, dreams; who looks inside, awakes. (Lolly Daskal)",
          "likes": []
          }
      }
      ```
    - **Status 404**
      ```json
      {
        "status": "fail",
        "message": "Message with id: cd5fb364-5dfc-4fed-9205-252cd80ee42b not found",
        "payload": null
      }
      ```

### 3.3 Getting all Messages
- **Endpoint:** `GET /api/message/v1/getAll`
- **Description:** Gets all Messages.
- **Parameters:** --
  - **Response:**
      - **Status 200**
        ```json
        {
          "status": "success",
          "message": "success",
          "payload": [
            {
              "id": "6e24d861-248a-4799-8993-2922c8978ccf",
              "userEmail": "abc@g.com",
              "content": "Test Message 1",
              "comments": [],
              "createdAt": "2025-06-20T17:22:14.480556Z",
              "quote": "When you want to be honored by others, you learn to honor them first. (Sathya Sai Baba)",
              "likes": []
            },
            {
              "id": "72a87d29-d248-4e47-a198-94ff7ccea0cf",
              "userEmail": "abc@g.com",
              "content": "Test Message 2",
              "comments": [],
              "createdAt": "2025-06-20T17:22:09.209029Z",
              "quote": "No matter how much preparation we do, in the real tests of our lives, we'll be in unfamiliar terrain. (Josh Waitzkin)",
              "likes": []
            }
          ]
        }
        ```

### 3.4 Liking a Message
- **Endpoint:** `POST /api/message/v1/like`
- **Description:** Creates a new Message.
- **Parameters:**
    ```
    cd5fb364-5dfc-4fed-9205-252cd80ee42b'
    ```
- **Response:**
    - **Status 200**
      ```json
      {
        "status": "success",
        "message": "update likes to the target post id: cd5fb364-5dfc-4fed-9205-252cd80ee42b",
        "payload": {
          "id": "cd5fb364-5dfc-4fed-9205-252cd80ee42b",
          "userEmail": "abc@g.com",
          "content": "Test Message ",
          "comments": [],
          "createdAt": "2025-06-20T17:22:14.480556Z",
          "quote": "When you want to be honored by others, you learn to honor them first. (Sathya Sai Baba)",
          "likes": [
            "abc@g.com"
          ]
        }
      }
      ```
    - **Status 404**
      ```json
      {
        "status": "fail",
        "message": "Message with id: cd5fb364-5dfc-4fed-9205-252cd80ee42b not found",
        "payload": null
      }
      ```

## 4. Comments
### 4.1 Creating a new Comment
- **Endpoint:** `POST /api/comment/v1/create`
- **Description:** Creates a new Comment under an existing Message.
- **Parameters:**
    ```json
    {
      "email": "cd5fb364-5dfc-4fed-9205-252cd80ee42b",
      "password": "Test comment"
    }
    ```
- **Response:**
    - **Status 200**
        ```json
        {
          "status": "success",
          "message": "Message with id: cd5fb364-5dfc-4fed-9205-252cd80ee42 not found",
          "payload": null
        }
        ```
  - **Status 200**
      ```json
      {
        "status": "fail",
        "message": "Comment has been added to message",
        "payload": null
      }
      ```