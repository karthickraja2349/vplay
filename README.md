# VPlay - video Calling Web App

VPlay is a Java-based web application designed to provide secure, interactive, real-time communication between users through a friend-based system. It includes user registration, login, friend requests, notifications, and one-to-one call functionality—all managed with secure practices and a built-in chatbot powered by DJL and BERT for project-related queries.

---

## 🌐 Features

### 🔐 User Registration & Login
- Users can register using a **unique username**, **email ID**, and a **strong password** (minimum 8 characters, must include letters, digits, and special characters).
- After successful registration, an **email notification** is sent to the user.
- Secure password storage is ensured via **password hashing**.
- Users can log in using their **username and password**.

### 🧑‍🤝‍🧑 Friend System
- View your current list of friends.
- Send friend requests to existing users.
- Receive and respond to friend requests via the **notification system** (Accept or Reject).
- Friendship is **mutual**—both users must agree.

### 📞 Call Feature
- Users can initiate a **video call** by entering their friend’s **registered email ID**.
- If they are mutual friends, an **invitation link** is sent to the friend via email.
- Once both users open the link, they are connected in **real-time**.
- **Calls cannot be initiated with non-friends**, ensuring secure communication.

### 🔔 Notifications
- Displays incoming **friend requests** and **call invitations**.
- Notifications are accessible only to **logged-in users**.

### 🤖 Chatbot
- An embedded chatbot answers user questions about the project.
- Powered by **DJL (Deep Java Library)** and **BERT model**.
- Queries are matched against a set of internal documents describing the project flow and features(not stable).

---


 playCode/
   ├── css/
   ├── js/
   ├── html/
   ├── servlets/
        └── *.java
   ├── database/
   ├── user_submissions/
   ├── utils/
   ├── WEB-INF/
       └── classes
   └── README.md

---

## 🛡️ Security

- **Session-based access control** ensures only logged-in users can access protected features (friends, calls, notifications).
- **Password hashing** is used to securely store login credentials.
- **Call functionality** is restricted to mutual friends only.

---

## 🚀 Technologies Used

- Java (Servlets)
- Apache Tomcat 10
- HTML/CSS/JavaScript
- WebSockets (for real-time communication)
- WebRTC
- DJL (Deep Java Library) + BERT (for chatbot)
- MySQL (for data persistence)

---

## 📬 Setup Instructions

1. Clone the repository and place it under the Tomcat `webapps/` directory.
2. Ensure your database is configured and credentials are set.
3. Build the project using `mvn package` or `mvn clean install`.
4. Start the Tomcat server.
5. Access the app via `http://localhost:8080/vplay/index.html`.

---

## 🙋‍♂️ Example Questions You Can Ask the Bot

- Is my profile secure?
- Can I call someone who is not my friend?
- Where do I see my friend requests?
- What notifications do I receive?
- Can I see my friends list?

---


## 🔒 **Current Limitations**

- The video calling system currently supports only **2 users** at a time.
- No **camera on/off** or **microphone mute/unmute** buttons available.
- The chatbot is limited to answering only basic project-related questions.

---

## 🚀 **Planned Features**
- Support for **group video calls** (more than 2 users).
- Add **camera toggle** functionality.
- Add **microphone toggle** functionality.
- Display **call status** (e.g., Ringing, Connected, Ended).
- Enhance chatbot to handle **advanced questions** about setup, errors, and usage.


 ---
 
## 📧 Contact

Developed by **Karthick Raja K.**  
gmail  -> karthickraja182356@gmail.com

---


