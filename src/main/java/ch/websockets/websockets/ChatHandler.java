package ch.websockets.websockets;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

public class ChatHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public ChatHandler() {
      Thread thread = new Thread(() -> {
            while(true) {
                try {
                    for(WebSocketSession session : sessions) {
                        if(!session.isOpen()) {
                            sessions.remove(session);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

    System.out.println("Received Message: " + message.getPayload());

    String username = "client";  // Default username if 'from' is not present

    // Parse the received message from the client
    String receivedMessage = message.getPayload();

    try {
        // Parse the received message as JSON
        JSONObject jo = new JSONObject(receivedMessage);

        // Extract the 'from' field if it exists
        if (jo.has("from")) {
            username = jo.getString("from");
        }

        // Send the message to all connected clients
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(username + ": " + jo.getString("message")));
        }
    } catch (JSONException ex) {
        ex.printStackTrace();
    }
}

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Got Handshake: " + session);
        sessions.add(session);
    }

}
