package org.scoula.ChatBot.DTO;

public class ChatResponse {
    private String reply;
    public ChatResponse(String reply) {
        this.reply = reply;
    }
    public String getReply() { return reply; }
}
