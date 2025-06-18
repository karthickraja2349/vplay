package com.vplay.notification;

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

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("emailID") == null) {
            writer.write("{\"error\": \"Missing emailID\"}");
            return;
        }

        String gmailId = (String) session.getAttribute("emailID");

        try {
            // Get user_id from email
            PreparedStatement getUserIdPrepareStatement = DatabaseConnection.getPreparedStatement(SQLQueries.GET_USER_ID);
            getUserIdPrepareStatement.setString(1, gmailId);
            ResultSet getUserIdResultSet = getUserIdPrepareStatement.executeQuery();

            int userId = -1;
            if (getUserIdResultSet.next()) {
                userId = getUserIdResultSet.getInt("user_id");
            } 
            else {
                writer.write("{\"error\": \"User not found\"}");
                return;
            }

            // Get notifications for user_id
            PreparedStatement getNotificationPreparedStatement = DatabaseConnection.getPreparedStatement(SQLQueries.GET_NOTIFICATION);
            getNotificationPreparedStatement.setInt(1, userId);
            ResultSet getNotificationResultSet = getNotificationPreparedStatement.executeQuery();

            StringBuilder json = new StringBuilder();
            json.append("[");

            boolean first = true;
            while (getNotificationResultSet.next()) {
                String notificationType = getNotificationResultSet.getString("status");
                if(notificationType.equalsIgnoreCase("pending")){
                      if (!first) 
                           json.append(",");
                      first = false;
                      json.append("{");
                      json.append("\"notification_id\":\"").append(getNotificationResultSet.getString("notification_id")).append("\",");
                      json.append("\"sender_name\":\"").append(getNotificationResultSet.getString("sender_name")).append("\",");
                      json.append("\"sender_email\":\"").append(getNotificationResultSet.getString("sender_email")).append("\",");
                      json.append("\"status\":\"").append(getNotificationResultSet.getString("status")).append("\"");
                      json.append("}");
                }
          }
         json.append("]");
        writer.write(json.toString());
        } 
        catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Failed due to DB error: " + e.getMessage());
        }
    }
}
