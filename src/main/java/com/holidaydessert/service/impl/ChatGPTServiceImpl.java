package com.holidaydessert.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.core.credential.AzureKeyCredential;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.AzureOpenAIConfig;
import com.holidaydessert.service.ChatGPTService;

@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    @Autowired
    private AzureOpenAIConfig azureOpenAIConfig;
    
	@Override
	public ApiReturnObject getChat(String prompt) {
		
		OpenAIClient client = new OpenAIClientBuilder()
				.credential(new AzureKeyCredential(azureOpenAIConfig.getKey()))
				.endpoint(azureOpenAIConfig.getEndpoint())
				.buildClient();

		List<ChatRequestMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatRequestSystemMessage(prompt));

		ChatCompletions chatCompletions = client.getChatCompletions(azureOpenAIConfig.getDeployment(), new ChatCompletionsOptions(chatMessages));

		String replyContent = chatCompletions.getChoices().get(0).getMessage().getContent();
		
		return new ApiReturnObject(200, "取得對話成功", replyContent);

	}
}
