package com.ccd.backend.WebSocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("New connection opened: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Received message from " + session.getId() + ": " + message.getPayload());
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Connection closed: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.log(Level.SEVERE, "Error in session " + session.getId(), exception);
    }
}
