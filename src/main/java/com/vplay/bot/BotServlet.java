 package com.vplay.bot;

import java.util.List;
import java.util.Scanner;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import com.vplay.utils.QAService;
import com.vplay.utils.LuceneIndexer;

@WebServlet("/bot")
public class BotServlet extends HttpServlet {

    private LuceneIndexer indexer;
    private QAService qaService;

    @Override
    public void init() throws ServletException {
      try{
        qaService = new QAService();
        indexer = new LuceneIndexer();

List<String> docs = List.of(
    // Registration
    "To register, provide a unique username, email ID, and a strong password with at least 8 characters including letters, numbers, and special characters.",
    "After registering, you will receive an email notification confirming your account.",
    "Once registered, you will be redirected to the login page.",

    // Login
    "To log in, use your registered username and password.",
    "Passwords are securely stored using hashing algorithms for safety.",

    // Profile and Security
    "Your profile is secured and cannot be accessed unless you are logged in.",
    "You cannot make a call unless you are friends with the other user.",
    "Calls and chats are protected by session-based access control to prevent unauthorized use.",

    // Friend System
    "You can view your friends after logging in.",
    "To become friends, send a friend request to an existing user.",
    "Friend requests are shown under notifications where you can accept or reject.",
    "When a friend request is accepted, both users are connected as friends.",

    // Calling Feature
    "To make a call, you must enter your friend's registered email ID.",
    "A call invitation link is sent to your friend if both of you are friends.",
    "When both users open the invitation link, they can communicate in real time.",
    "Users cannot call someone who is not on their friends list.",

    // Notifications
    "Notifications show new friend requests and call invitations.",
    "Only logged-in users can access notifications or start a call."
);





        indexer.indexDocs(docs);
      }
      catch(Exception e){
           e.printStackTrace();
      }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    String question = request.getParameter("question");
    String answer;

    try {
        if (indexer == null || qaService == null) {
            throw new IllegalStateException("Service not initialized.");
        }

        String paragraph = indexer.searchBestParagraph(question);
        if (paragraph == null) {
            answer = "Sorry, I couldn't find an answer.";
        } else {
            answer = qaService.answer(question, paragraph);
        }

        out.print("{\"answer\": \"" + escapeJson(answer) + "\"}");
    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"answer\": \"Error: " + escapeJson(e.getMessage()) + "\"}");
    }

    out.flush();
}



    // Escape double quotes and backslashes for JSON compatibility
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}


/*
Can I call someone who is not my friend?
 Ask
Answer: users cannot call someone who is not on their friends list


How do I register?
 Ask
Answer: users must provide a unique username , email id , and a strong password


What happens after registration?
 Ask
Answer: an email notification


How are calls initiated?
 Ask
Answer: session - based access control


Is my profile secure?
 Ask
Answer: your profile is secured



Where do I see my friend requests?
 Ask
Answer: notifications where you can accept or reject . friend requests show up in the notification section


Where  I see my friend requests?
 Ask
Answer: the notification section

Can I chat with someone who is not my friend?
 Ask
Answer: users cannot call someone who is not on their friends list


How do I make a call to my friend?
 Ask
Answer: you must enter your friend ' s registered email id


How is my password stored?
 Ask
Answer: ##ing to protect user credentials . passwords are securely stored using


What notifications do I receive?
 Ask
Answer: email notification confirming your account . notifications show new friend requests and call invitations


What options do I have after logging in?
 Ask
Answer: each user has a personal profile


Can I see my friends list?
 Ask
Answer: you can view your friends after logging in .
*/

/*user can register by giving username, emailid, password (username, mail was unique) , password must be 8 length with alphanumericwith special characters

after that email notification was send to the user (for successfully register)

then user was redirected to login page , they can login with thier username and password

after that they had options 
      1) friends -  see their friends
      2) Add friend - send friend Request to the user who already had an account.
      3)Notifications - see the friendRequest notification( accept btn and reject btn - if accept both are friends, if not nothing)
      4) call - user need to enter the registered mailid of the friend, then invitation link was visible to him and send to the friend via mailid
                  he click the link, friend also open the link , both of them able to communicate
                  
    user profile was secure, user cannot able to made a call request , if they are not friends.
    passsword was stored via password hashing.*/
