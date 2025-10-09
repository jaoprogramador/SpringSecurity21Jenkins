package com.jao.springai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {
	
	private final ImageClient imageClient;
	
	public ImageController (ImageClient imageClient) {
		this.imageClient = imageClient;
	}
	
	@GetMapping
	public String getURLImage(@RequestParam("text") String text) {
		return this.imageClient.call(new ImagePrompt(text)).getResult().bgetOutput().getUrl();
		
	}
	

}
