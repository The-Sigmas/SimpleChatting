package ch.websockets.websockets;

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
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        System.out.println("Recieved Message: " + message.getPayload());

        String username = "client";
        for(WebSocketSession webSocketSession : sessions) {
            try {
                String receivedMessage = (String) message.getPayload();
                webSocketSession.sendMessage(new TextMessage(username + ": " + receivedMessage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Got Handshake: " + session);
        sessions.add(session);
    }

}
