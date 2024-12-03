package com.example.front;

import com.example.front.network_termproject.SplashScreen;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.swing.*;

@SpringBootApplication
@EnableAsync
public class FrontApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        // Spring Boot 애플리케이션 실행
        var context = SpringApplication.run(FrontApplication.class, args);

//        // Swing 애플리케이션 실행
//        SwingUtilities.invokeLater(() -> new SplashScreen().setVisible(true));
    }

    public CommandLineRunner runSplashScreen(ApplicationContext context) {
        return args -> {
            SplashScreen splashScreen = context.getBean(SplashScreen.class); // Get SplashScreen bean
            SwingUtilities.invokeLater(() -> splashScreen.setVisible(true)); // Show SplashScreen on the EDT
        };
    }

}
