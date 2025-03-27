package cameraApp.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class PoseWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ WebSocket 연결됨: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {


        // JSON인지 검사
        String payload = message.getPayload().trim();  // 👈 앞뒤 공백 제거
        System.out.println("📨 받은 메시지: " + payload);
      
        if (payload.startsWith("{") && payload.endsWith("}")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> jsonMap = mapper.readValue(payload, Map.class);
                System.out.println("✅ JSON 파싱 성공: " + jsonMap);
            } catch (Exception e) {
                System.out.println("❌ JSON 파싱 실패: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ JSON 형식 아님 (start-end mismatch): " + payload);
        }


        // 👉 JSON 파싱 필요하면 여기에 Jackson 사용 가능
        // ObjectMapper mapper = new ObjectMapper();
        // Map<String, Object> poseData = mapper.readValue(message.getPayload(), Map.class);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("❌ 연결 종료됨: " + session.getId());
    }
}
