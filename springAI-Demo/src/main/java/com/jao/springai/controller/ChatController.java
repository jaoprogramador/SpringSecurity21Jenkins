package com.jao.springai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {
	private final ChatClient chatClient;
	
	public ChatController (ChatClient chatClient) {
		this.chatClient = chatClient;
		
	}
	
	@GetMapping
	public String chat(@RequestParam String texto) {
		
		return chatClient.call(texto);
	}

}
