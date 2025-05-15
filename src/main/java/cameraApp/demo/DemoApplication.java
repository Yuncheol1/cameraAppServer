package cameraApp.demo;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	private Process ngrokProcess;

	public static void main(String[] args) throws SQLException, IOException {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean
//	public ApplicationRunner runNgrokOnStartup() {
//		return args -> {
//			try {
//				ProcessBuilder pb = new ProcessBuilder("ngrok", "http", "--url=fowl-one-definitely.ngrok-free.app.app", "80");
//				pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//				pb.redirectError(ProcessBuilder.Redirect.INHERIT);
//				ngrokProcess = pb.start();
//				System.out.println("Ngrok start");
//			} catch (IOException e) {
//				System.err.println("Ngrok Fail : " + e.getMessage());
//			}
//		};
//	}
//
//	@PreDestroy
//	public void stopNgrokOnShutdown() {
//		if (ngrokProcess != null && ngrokProcess.isAlive()) {
//			ngrokProcess.destroy();
//			System.out.println("Ngrok stop");
//		}
//	}
}
