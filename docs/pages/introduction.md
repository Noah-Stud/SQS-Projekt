# Introduction
This project is a lightweight social-media / chat app, similar to services like Twitter or Mastodon that can be run completely in docker.
It is designed to allow users to post messages in a news feed, like other people's messages, and comment on them.
A distinction is made between logged-in and anonymous users: Users that are not logged in can only read the feed, 
while logged-in users can actively participate. The system also allows new users to register, which only requires an email-address.


**The project should fulfill the following functions:**

- Registration of new Users
- Log In (Including jwt-Tokens)
- Newsfeed with all Messages
- Creation of new Messages
- Possibility to like other Messages
- Possibility to leave comments under existing Messages