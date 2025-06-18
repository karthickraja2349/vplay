
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/signaling")
public class SignalingServer {

    // Store clients (user1, user2)
    private static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();
    private static int userCount = 0;

    private String userId;

    @OnOpen
    public void onOpen(Session session) {
        userId = "user" + (++userCount);
        clients.put(userId, session);
        System.out.println(userId + " connected.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // Relay to the other user
            for (String uid : clients.keySet()) {
                if (!uid.equals(userId)) {
                    Session peer = clients.get(uid);
                    if (peer != null && peer.isOpen()) {
                        peer.getBasicRemote().sendText(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(userId);
        System.out.println(userId + " disconnected.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error from " + userId + ": " + throwable.getMessage());
    }
}


//chrome://flags/#unsafely-treat-insecure-origin-as-secure
//sudo netstat -tulnp | grep 8080
//yu9WE4m5#3

