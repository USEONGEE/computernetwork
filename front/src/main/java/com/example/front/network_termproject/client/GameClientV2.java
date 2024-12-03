package com.example.front.network_termproject.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GameClientV2 {

    private static final String REGISTER_URL = "http://localhost:8080/auth/register";
    private static final String LOGIN_URL = "http://localhost:8080/auth/login";
    private static final String WEBSOCKET_URL = "ws://localhost:8080/ws/game";
    private final List<MessageListener> listeners = new ArrayList<>();
    @Getter
    @Setter
    private String username;


    private String sessionCookie;
    private WebSocketClient webSocketClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static GameClientV2 instance;

    private GameClientV2() {}

    public static synchronized GameClientV2 getInstance() {
        if (instance == null) {
            instance = new GameClientV2();
        }
        return instance;
    }

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }


    private void notifyListeners(String type, Object message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(new CallbackMessage(type, message));
        }
    }

    public void register(String username, String password) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestBody = String.format("{\"name\":\"%s\", \"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(REGISTER_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
//            System.out.println("User registered successfully.");
        } else {
            System.err.println("Registration failed: " + response.body());
        }
    }

    public void login(String username, String password) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestBody = String.format("{\"name\":\"%s\", \"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Login successful.");

            List<String> setCookieHeaders = response.headers().map().get("set-cookie");
            if (setCookieHeaders != null) {
                for (String header : setCookieHeaders) {
                    if (header.startsWith("JSESSIONID")) {
                        sessionCookie = header.split(";")[0];
                        System.out.println("Session Cookie: " + sessionCookie);
                        break;
                    }
                }
            }
        } else {
            System.err.println("Login failed: " + response.body());
        }
    }

    public void connectToWebSocket() throws Exception {
        webSocketClient = new WebSocketClient(new URI(WEBSOCKET_URL)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("WebSocket connection opened.");
                sendConnectFrame();
            }

            @Override
            public void onMessage(String message) {
                System.out.println("GameClientV2.onMessage");
                processMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("WebSocket connection closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("WebSocket error: " + ex.getMessage());
            }
        };

        // Add session cookie to the headers
        webSocketClient.addHeader("Cookie", sessionCookie);
        webSocketClient.connect();
    }

    private void sendConnectFrame() {
        String connectFrame = "CONNECT\naccept-version:1.2\nheart-beat:10000,10000\n\n\u0000";
        webSocketClient.send(connectFrame);
        System.out.println("STOMP CONNECT frame sent.");
    }

    public void subscribe(String topic) {
        String subscribeFrame = String.format(
                "SUBSCRIBE\nid:sub-1\ndestination:%s\n\n\u0000",
                topic
        );
        webSocketClient.send(subscribeFrame);
        System.out.println("Subscribed to topic: " + topic);
    }

    public void sendMessage(String destination, Object message) throws Exception {
        String payload = objectMapper.writeValueAsString(message);
        String messageFrame = String.format(
                "SEND\ndestination:%s\n\n%s\u0000",
                destination,
                payload
        );
        webSocketClient.send(messageFrame);
        System.out.println("Message sent to " + destination + ": " + payload);
    }

    public void disconnect() {
        String disconnectFrame = "DISCONNECT\n\n\u0000";
        webSocketClient.send(disconnectFrame);
        webSocketClient.close();
        System.out.println("Disconnected from WebSocket.");
    }

    // TODO 파싱을 잘 해야함.
    // 기존 메서드 및 processMessage 수정
    private void processMessage(String message) {
        System.out.println("GameClientV2.processMessage");
        System.out.println(message);

        try {
            String[] parts = message.split("\n\n", 2);

            // CONNECTED 메시지 무시
            if (parts[0].startsWith("CONNECTED")) {
                return;
            }

            if (parts.length > 1) {
                String body = parts[1].replace("\u0000", "");
                GameMessage gameMessage = objectMapper.readValue(body, new TypeReference<GameMessage<Object>>() {});

                // 메시지 타입에 따른 로직
                switch (gameMessage.getType()) {
                    case "MATCHED":
                        gameMessage = objectMapper.readValue(body, new TypeReference<GameMessage<GameRoom>>() {});
                        notifyListeners("MATCHED", gameMessage.getContent());
                        break;
                    case "MATCHED_NOT_YET":
                        notifyListeners("MATCHED_NOT_YET", gameMessage.getContent());
                        break;
                    case "ADD_EXPRESSION":
                        notifyListeners("ADD_EXPRESSION", gameMessage.getContent());
                        break;
                    case "REMOVE_EXPRESSION":
                        notifyListeners("REMOVE_EXPRESSION", gameMessage.getContent());
                        break;
                    case "REMOVE_ALL_EXPRESSION":
                        notifyListeners("REMOVE_ALL_EXPRESSION", gameMessage.getContent());
                        break;
                    case "ATTACK_SUCCESS":
                        notifyListeners("ATTACK_SUCCESS", gameMessage.getContent());
                        break;
                    case "ATTACKED":
                        notifyListeners("ATTACKED", gameMessage.getContent());
                        break;
                    case "ATTACK_FAIL":
                        notifyListeners("ATTACK_FAIL", gameMessage.getContent());
                        break;
                    case "GAME_OVER":
                        notifyListeners("GAME_OVER", gameMessage.getContent());
                        break;
                    case "YOUR_TURN":
                        notifyListeners("YOUR_TURN", gameMessage.getContent());
                        break;
                    case "ERROR":
                        System.err.println("Error: " + gameMessage.getContent());
                        break;
                    default:
                        System.err.println("Unknown message type: " + gameMessage.getType());
                }
            } else {
                System.err.println("Malformed STOMP frame received.");
            }
        } catch (Exception ex) {
            System.err.println("Failed to process message: " + ex.getMessage());
        }
    }

    public void subscribeToGameTopics() {
        // 게임과 관련된 주제를 구독
        subscribe("/topic/matched");
        subscribe("/topic/add-expression");
        subscribe("/topic/remove-expression");
        subscribe("/topic/remove-all-expression");
        subscribe("/topic/attack");
        subscribe("/queue/private"); // 개인 메시지 구독
        System.out.println("Subscribed to all game-related topics.");
    }

    public void sendMatchRequest() {
        System.out.println("GameClientV2.sendMatchRequest");
        try {
            String frame = "SEND\ndestination:/app/match\n\n\u0000";
            webSocketClient.send(frame);
            System.out.println("Match request sent.");
        } catch (Exception e) {
            System.err.println("Failed to send match request: " + e.getMessage());
        }
    }

    public void sendAddExpression(String roomId, String expression) {
        try {
            GameMessage message = new GameMessage("ADD_EXPRESSION", expression, roomId);
            sendMessage("/app/add-expression", message);
        } catch (Exception e) {
            System.err.println("Failed to send add expression request: " + e.getMessage());
        }
    }

    public void sendRemoveExpression(String roomId) {
        try {
            GameMessage message = new GameMessage("REMOVE_EXPRESSION", null, roomId);
            sendMessage("/app/remove-expression", message);
        } catch (Exception e) {
            System.err.println("Failed to send remove expression request: " + e.getMessage());
        }
    }

    public void sendRemoveAllExpression(String roomId) {
        try {
            GameMessage message = new GameMessage("REMOVE_ALL_EXPRESSION", null, roomId);
            sendMessage("/app/remove-all-expression", message);
        } catch (Exception e) {
            System.err.println("Failed to send remove all expression request: " + e.getMessage());
        }
    }

    public void sendAttack(String roomId) {
        try {
            GameMessage message = new GameMessage("ATTACK", null, roomId);
            sendMessage("/app/attack", message);
        } catch (Exception e) {
            System.err.println("Failed to send attack request: " + e.getMessage());
        }
    }

    public static class GameMessage<T> {
        private String type;
        private T content;
        private String roomId;

        public GameMessage() {}

        public GameMessage(String type, T content, String roomId) {
            this.type = type;
            this.content = content;
            this.roomId = roomId;
        }

        public String getType() {
            return type;
        }

        public T getContent() {
            return content;
        }

        public String getRoomId() {
            return roomId;
        }

        @Override
        public String toString() {
            return "GameMessage{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    ", roomId='" + roomId + '\'' +
                    '}';
        }
    }
}

