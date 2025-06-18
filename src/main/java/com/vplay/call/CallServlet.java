package com.vplay.call;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetAddress;
import java.util.UUID;

import com.vplay.database.DatabaseConnection;
import com.vplay.database.SQLQueries;

@WebServlet("/call")
public class CallServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        int userId = (Integer) session.getAttribute("user_id");
        String userMailId = (String) session.getAttribute("emailID"); 
        
        

        try {
            // Read JSON
            StringBuilder json = new StringBuilder();
            BufferedReader bufferedReader = request.getReader();  
            String line;
            while ((line = bufferedReader.readLine()) != null)
                json.append(line);

            // Parse JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json.toString());

            // Extract friendMailId
            String friendMailId = jsonObject.get("friendMailId").toString().toLowerCase();
            
            if (friendMailId.equalsIgnoreCase(userMailId)) {
               writer.write("{\"message\": \"You can't call yourself\"}");
                return;
          }
            
            // Lookup friend ID
            int friendId = -1;
            String getFriendId = SQLQueries.GET_USERID_BY_EMAIL;
            PreparedStatement stmt = DatabaseConnection.getPreparedStatement(getFriendId);
            stmt.setString(1, friendMailId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                friendId = rs.getInt("user_id"); 
            }
            else {
                writer.write("{\"message\": \"There is no user with this Email ID\"}");
                return;
            }
            
          
            // Check if user is friend
            String isFriendQuery = SQLQueries.IS_FRIEND;
            PreparedStatement friendStmt = DatabaseConnection.getPreparedStatement(isFriendQuery);
            friendStmt.setInt(1, userId);
            friendStmt.setInt(2, friendId);
            ResultSet friendRs = friendStmt.executeQuery();

            if (friendRs.next()) {
                String ipAddress = "192.168.10.188";//InetAddress.getLocalHost().getHostAddress();
                String roomId = UUID.randomUUID().toString().substring(0,8);
                String meetingURL = "http://192.168.10.188:8080/vplay/html/meeting.html?room=" + roomId;

                
                JSONObject responseJSON = new JSONObject();
               responseJSON.put("message", "Calling " + friendMailId + "...");
               responseJSON.put("roomId", roomId);
               responseJSON.put("meetingUrl", meetingURL);

              writer.write(responseJSON.toJSONString());
            }
            else {
                writer.write("{\"message\": \"You are not friends with this user\"}");
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed due to error: " + e.getMessage());
        }
    }
}

