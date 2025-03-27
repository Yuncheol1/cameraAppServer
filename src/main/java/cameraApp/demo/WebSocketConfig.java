package cameraApp.demo;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final PoseWebSocketHandler poseWebSocketHandler;

    public WebSocketConfig(PoseWebSocketHandler poseWebSocketHandler) {
        this.poseWebSocketHandler = poseWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(poseWebSocketHandler, "/ws/pose")
                .setAllowedOrigins("*"); // 디바이스에서 접근 가능하게 허용
    }
}
