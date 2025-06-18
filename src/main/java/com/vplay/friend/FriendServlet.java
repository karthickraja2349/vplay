package com.vplay.friend;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import com.vplay.database.DatabaseConnection;
import com.vplay.database.SQLQueries;

@WebServlet("/friends")
public class FriendServlet extends HttpServlet {

         public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException{
         
                  response.setContentType("application/json");
                  PrintWriter writer = response.getWriter();
                  
                  HttpSession session = request.getSession(false);
                  
                  if (session == null || session.getAttribute("user_id") == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
                       return;
                 }
                  
                  int user_id = (Integer) session.getAttribute("user_id");
                  String userMailId = (String) session.getAttribute("emailID");
                  
                  StringBuilder json = new StringBuilder();
                  json.append("[");
                  boolean first = true;
                  
                  try{
                       String getFriends = SQLQueries.GET_FRIENDS;
                       PreparedStatement getFriendsPreparedStatement = DatabaseConnection.getPreparedStatement(getFriends);
                       getFriendsPreparedStatement.setInt(1, user_id);
                       ResultSet getFriendsResultSet = getFriendsPreparedStatement.executeQuery();
                       
                      while(getFriendsResultSet.next()){
                            if (!first)
                                json.append(",");
                            first = false;
                            json.append("{");
                            json.append("\"userName\":\"").append(getFriendsResultSet.getString("friend_name")).append("\",");
                            json.append("\"emailId\":\"").append(getFriendsResultSet.getString("friend_email")).append("\"");
                            json.append("}");
                        }
                       json.append("]");
                       System.out.println("DEBUG JSON: " + json.toString());
                      writer.write(json.toString());
                  }
                  catch (SQLException e) {
                        e.printStackTrace();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                   "Failed due to DB error: " + e.getMessage());
        }
                  
         }
}
