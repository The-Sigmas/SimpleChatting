package ch.websockets.websockets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public SocketHandler() {
      Thread thread = new Thread(() -> {
            while(true) {
                try {
                    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    List<WebSocketSession> disconnectedSessions = new ArrayList<>();
                    for(WebSocketSession session : sessions) {
                        try {
                            if(session.isOpen()) {
                                session.sendMessage(new TextMessage(time));
                            } else {
                                disconnectedSessions.remove(session);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    sessions.removeAll(disconnectedSessions);
                    Thread.sleep(1000);
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

        for(WebSocketSession webSocketSession : sessions) {
            Map<?, ?> value = new Gson().fromJson(message.getPayload(), Map.class);
            System.out.println(webSocketSession.getId() + ": " + value);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

}
