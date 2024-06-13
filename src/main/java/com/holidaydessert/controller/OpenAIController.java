package com.holidaydessert.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.core.credential.AzureKeyCredential;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

	@Value("${openai.model}")
	private String model;

    @Value("${openai.api.url}")
    private String apiUrl;
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.api.deployment}")
    private String apiDeployment;
    
	@GetMapping("/chat")
	public String chat(@RequestParam String prompt) {
		
		  OpenAIClient client = new OpenAIClientBuilder()
			   .credential(new AzureKeyCredential(apiKey))
			   .endpoint(apiUrl)
		       .buildClient();
		  
		  List<ChatRequestMessage> chatMessages = new ArrayList<>();
		  chatMessages.add(new ChatRequestSystemMessage(prompt));
		  
		  ChatCompletions chatCompletions = client.getChatCompletions(apiDeployment, new ChatCompletionsOptions(chatMessages));
		  
		  String replyContent = chatCompletions.getChoices().get(0).getMessage().getContent();
		  
		  return replyContent;
		  
	}
}
