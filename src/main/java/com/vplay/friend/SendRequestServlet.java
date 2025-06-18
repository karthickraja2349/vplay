package com.vplay.friend;

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

import com.vplay.database.DatabaseConnection;
import com.vplay.database.SQLQueries;

@WebServlet("/sendRequest")
public class SendRequestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        int userId = (Integer) session.getAttribute("user_id");
        String userMailId = (String) session.getAttribute("emailID");
        String userName = (String) session.getAttribute("userName");

        try {
            // Read and parse JSON
            StringBuilder json = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null)
                json.append(line);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json.toString());

            String friendMailId = jsonObject.get("friendMailId").toString().toLowerCase();

            if (friendMailId.equalsIgnoreCase(userMailId)) {
                writer.write("{\"message\": \"You can't send a request to yourself\"}");
                return;
            }

            // Get friend ID
            String query = SQLQueries.GET_USERID_BY_EMAIL;
            PreparedStatement stmt = DatabaseConnection.getPreparedStatement(query);
            stmt.setString(1, friendMailId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                writer.write("{\"message\": \"No user found with this email\"}");
                return;
            }

            int friendId = rs.getInt("user_id");
            
            //same user
            if(friendId == userId){
                  writer.write("{\"message\": \"You can't  send Request to YourSelf \"}");
                  return;
            }
            
            //isAlready Friend
            String isFriend = SQLQueries.IS_FRIEND;
            PreparedStatement isFriendPreparedStatement = DatabaseConnection.getPreparedStatement(isFriend);
            isFriendPreparedStatement.setInt(1, userId);
            isFriendPreparedStatement.setInt(2, friendId);
            ResultSet getIsFriendPreparedStatement = isFriendPreparedStatement.executeQuery();
            
            if(getIsFriendPreparedStatement.next()){
                    writer.write("{\"message\": \"You are Already friends \"}");
                    return;
            }

           
            String insert = SQLQueries.SEND_FRIEND_REQUEST;
            PreparedStatement insertStmt = DatabaseConnection.getPreparedStatement(insert);
            insertStmt.setString(1, userName); 
            insertStmt.setString(2, userMailId);
            insertStmt.setInt(3, friendId);
            insertStmt.setString(4, "pending");

            insertStmt.executeUpdate();

            writer.write("{\"message\": \"Request sent successfully\"}");

        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error sending request: " + e.getMessage());
        }
    }
}

