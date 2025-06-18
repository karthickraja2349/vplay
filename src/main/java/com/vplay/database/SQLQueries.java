package com.vplay.database;

public class SQLQueries{

       public static final String INSERT_USER = "INSERT INTO users (username, password,email,password_salt) VALUES (?, ?, ?, ?)";
    
       public static final String CHECK_USER_EXISTENCE = "SELECT * FROM users WHERE userName = ? OR email = ?";
    
       public static final String VERIFY_USER = "SELECT user_id, password, password_salt , email FROM users WHERE userName = ?";
       
       public static final String GET_USER_ID = "SELECT user_id FROM users WHERE email = ?";
       
       public static final String GET_NOTIFICATION =  
                    "SELECT notification_id, sender_name, sender_email, status FROM notification WHERE receiver_id = ?";
                    
       public static final String  UPDATE_REQUEST=  "UPDATE notification SET status = ? WHERE notification_id = ?";        
       
       public static final String ADD_FRIEND = "INSERT INTO friend(user_id, friend_id) VALUES (?, ?)";
       
      public static final String GET_FRIENDS =  "SELECT u.user_id AS friend_id, " +
                                                                             "u.userName AS friend_name, " +
                                                                              "u.email AS friend_email " +
                                                                              "FROM friend f " +
                                                                              "JOIN users u ON f.friend_id = u.user_id " +
                                                                              "WHERE f.user_id = ?";
      
      public static final String IS_FRIEND = "SELECT friend_id FROM friend WHERE user_id = ? AND friend_id = ?";
      
      public static final String GET_USERID_BY_EMAIL = "SELECT user_id FROM users WHERE email = ?";
      
      public static final String SEND_FRIEND_REQUEST = 
                                                                     "INSERT INTO notification(sender_name, sender_email, receiver_id, status) Values (?,?,?,?)";
      


       

    
}

//type VARCHAR(50), -- e.g., 'friend_request'
//status VARCHAR(20), -- 'pending', 'accepted', 'rejected'

/*
import java.net.*;
String ipAddress = InetAddress.getLocalHost().getHostAddress();

import java.util.UUID;

String roomId = UUID.randomUUID().toString().substring(0, 8); // example: abc123ef
String meetingUrl = "http://" + ipAddress + ":8080/call?room=" + roomId;
http://192.168.159.108:8080/call?room=abc123ef
const urlParams = new URLSearchParams(window.location.search);
const roomId = urlParams.get("room");

if (roomId) {
  // Use roomId as part of your WebSocket or signaling logic
  // e.g., signalingSocket.send({room: roomId})
}

*/
