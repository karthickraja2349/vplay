package com.vplay.notification;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/accept")
public class AcceptServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("emailID") == null) {
            writer.write("{\"error\": \"Missing emailID\"}");
            return;
        }

        String userMailId = (String) session.getAttribute("emailID");

        try {
            // Read JSON body
            StringBuilder jsonBuilder = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonBuilder.toString());

            // Extract data
            long notificationId = Long.parseLong(jsonObject.get("notification_id").toString());
            String action = jsonObject.get("action").toString().toLowerCase();
            String friendMailId = jsonObject.get("sender_email").toString().toLowerCase();

            String status;
            if (action.equals("accept")) {
                status = "accepted";
            } 
            else if (action.equals("reject")) {
                status = "rejected";
            } 
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("{\"error\":\"Invalid action\"}");
                return;
            }

            // Update status in DB
            PreparedStatement stmt = DatabaseConnection.getPreparedStatement(SQLQueries.UPDATE_REQUEST);
            stmt.setString(1, status);
            stmt.setLong(2, notificationId);
            int updated = stmt.executeUpdate();

            JSONObject responseJson = new JSONObject();
            if (updated > 0) {
                responseJson.put("result", "success");
                responseJson.put("status", status);

                // Only add friend if accepted
                if (status.equals("accepted")) {
                    addFriend(userMailId, friendMailId);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseJson.put("result", "notification not found");
            }

            writer.println(responseJson.toJSONString());

        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("{\"error\":\"Invalid JSON\"}");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            writer.write("{\"error\":\"Server error\"}");
        }
    }

    //  Common reusable method to get user ID by email
    private int getUserIdByEmail(String email) throws Exception {
        PreparedStatement ps = DatabaseConnection.getPreparedStatement(SQLQueries.GET_USER_ID);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        }
        throw new Exception("User not found: " + email);
    }

    //  Insert mutual friendship entries
    private void addFriend(String userMail, String friendMail) throws Exception {
        int userId = getUserIdByEmail(userMail);
        int friendId = getUserIdByEmail(friendMail);

        PreparedStatement ps1 = DatabaseConnection.getPreparedStatement(SQLQueries.ADD_FRIEND);
        ps1.setInt(1, userId);
        ps1.setInt(2, friendId);
        ps1.executeUpdate();

        PreparedStatement ps2 = DatabaseConnection.getPreparedStatement(SQLQueries.ADD_FRIEND);
        ps2.setInt(1, friendId);
        ps2.setInt(2, userId);
        ps2.executeUpdate();
    }
}

