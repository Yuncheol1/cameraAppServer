package cameraApp.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static cameraApp.demo.DemoApplication.db;


@Component
public class PoseWebSocketHandler extends TextWebSocketHandler {
    static <T> void print(T value) {
        System.out.print(value);
    }
    static String endl = "\n";
    static String tap = "\t";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ WebSocket 연결됨: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // JSON인지 검사
        String payload = message.getPayload().trim();
        if (payload.startsWith("{") && payload.endsWith("}")) {
            try
            {
                db.InsertValues_Landmark(1, payload);
            } catch (Exception e) {
                System.out.println("JSON 파싱 실패: " + e.getMessage());
            }
        } else {
            System.out.println("JSON 형식 아님: " + payload);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("연결 종료됨: " + session.getId());
    }
}
