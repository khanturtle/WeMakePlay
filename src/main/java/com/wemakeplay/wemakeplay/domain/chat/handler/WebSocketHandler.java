package com.wemakeplay.wemakeplay.domain.chat.handler;

import com.google.gson.Gson;
import com.wemakeplay.wemakeplay.domain.chat.Chat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // WebSocket 세션을 저장하기 위한 리스트  훗날 서비스를 위해서라면 DB사용해야함 (MySQL보다 데이터 읽고 쓰는게 빠른 REDIS 사용 권장)
    private List<WebSocketSession> sessionList = new ArrayList<>();

    // 연결 성공시 실행되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // WebSocket 핸드쉐이크 시 전달된 사용자 이름   핸드쉐이크 : 세션정보를 가져올 수 있음
        String name = session.getHandshakeHeaders().get("name").get(0);

        // 세션 리스트에 현재 세션 추가
        sessionList.add(session);

        // 새로운 사용자 입장 메시지를 모든 세션에 전송
        System.out.println("sessionList = " + sessionList.size());
        sessionList.forEach(s-> {
            try {
                s.sendMessage(new TextMessage(name+"님께서 입장하셨습니다."));
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 클라이언트에서 메시지를 수신했을 때 실행
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        // Gson 객체를 생성하여 JSON 문자열을 객체로 변환   Gson : 자바객체를 JSON으로, JSON을 자바객체로 쉽게 표현 및 변환이 가능
        Gson gson = new Gson();
        // WebSocket 핸드쉐이크 시 전달된 사용자 이름
        String name = session.getHandshakeHeaders().get("name").get(0);

        // 현재 시간 정보를 가져옴
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        // 모든 세션에 현재 사용자의 메시지 전송
        sessionList.forEach(s-> {
            try {
                // JSON 문자열을 Chat 객체로 변환
                Chat chatMessage = gson.fromJson(message.getPayload(), Chat.class);
                // Chat 객체에 작성 시간 추가
                chatMessage.setTime(formattedTime);
                // 모든 클라이언트에게 메시지 전송
                s.sendMessage(new TextMessage(name + " : "+chatMessage.getContent()+"["+chatMessage.getTime()+"]"));
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 연결 종료 시 실행되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        // 현재 퇴장한 사용자의 이름
        System.out.println("session = " + sessionList.size());
        String name = session.getHandshakeHeaders().get("name").get(0);

        // 세션 리스트에서 현재 세션 제거
        sessionList.remove(session);

        // 퇴장 메시지를 모든 세션에 전송
        sessionList.forEach(s-> {
            try {
                s.sendMessage(new TextMessage(name+"님께서 퇴장하셨습니다."));
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
