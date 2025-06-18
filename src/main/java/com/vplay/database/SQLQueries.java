package com.vplay.database;
/*
DB DESIGN
mysql> use vplay;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+-----------------+
| Tables_in_vplay |
+-----------------+
| friend          |
| notification    |
| users           |
+-----------------+
3 rows in set (0.00 sec)

mysql> desc users;
+---------------+--------------+------+-----+---------+----------------+
| Field         | Type         | Null | Key | Default | Extra          |
+---------------+--------------+------+-----+---------+----------------+
| user_id       | int          | NO   | PRI | NULL    | auto_increment |
| userName      | varchar(100) | NO   | UNI | NULL    |                |
| password      | varchar(255) | YES  |     | NULL    |                |
| email         | varchar(100) | NO   | UNI | NULL    |                |
| password_salt | varchar(255) | YES  |     | NULL    |                |
+---------------+--------------+------+-----+---------+----------------+
5 rows in set (0.01 sec)

mysql> desc friend;
+-----------+------+------+-----+---------+-------+
| Field     | Type | Null | Key | Default | Extra |
+-----------+------+------+-----+---------+-------+
| user_id   | int  | NO   | PRI | NULL    |       |
| friend_id | int  | NO   | PRI | NULL    |       |
+-----------+------+------+-----+---------+-------+
2 rows in set (0.00 sec)

mysql> desc notification;
+-----------------+--------------+------+-----+---------+----------------+
| Field           | Type         | Null | Key | Default | Extra          |
+-----------------+--------------+------+-----+---------+----------------+
| notification_id | int          | NO   | PRI | NULL    | auto_increment |
| sender_name     | varchar(100) | YES  |     | NULL    |                |
| sender_email    | varchar(100) | YES  |     | NULL    |                |
| receiver_id     | int          | YES  | MUL | NULL    |                |
| status          | varchar(20)  | YES  |     | NULL    |                |
+-----------------+--------------+------+-----+---------+----------------+

*/
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


