package com.holidaydessert.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.DetectedTextLine;
import com.azure.ai.vision.imageanalysis.models.DetectedTextWord;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisOptions;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisResult;
import com.azure.ai.vision.imageanalysis.models.VisualFeatures;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.util.BinaryData;
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

	@Override
	public ApiReturnObject generateCaptionForImage(MultipartFile file) {

		ImageAnalysisClient client = new ImageAnalysisClientBuilder()
		    .endpoint(azureOpenAIConfig.getEndpoint())
		    .credential(new KeyCredential(azureOpenAIConfig.getKey()))
		    .buildClient();

		try {
		ImageAnalysisResult result = client.analyze(
				BinaryData.fromBytes(file.getBytes()),
			    Arrays.asList(VisualFeatures.CAPTION), // visualFeatures
			    new ImageAnalysisOptions().setGenderNeutralCaption(true)); // options:  Set to 'true' or 'false' (relevant for CAPTION or DENSE_CAPTIONS visual features)

			// Print analysis results to the console
			System.out.println("Image analysis results:");
			System.out.println(" Caption:");
			System.out.println("   \"" + result.getCaption().getText() + "\", Confidence " 
			    + String.format("%.4f", result.getCaption().getConfidence()));

            return new ApiReturnObject(200, "分析成功", result.getCaption().getText());

		} catch (HttpResponseException e) {
			System.out.println("Exception: " + e.getClass().getSimpleName());
			System.out.println("Status code: " + e.getResponse().getStatusCode());
			System.out.println("Message: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage());
		}
        return new ApiReturnObject(500, "分析失敗", null);
	}

	@Override
	public ApiReturnObject generateCaptionForURL(String url) {

		ImageAnalysisClient client = new ImageAnalysisClientBuilder()
		    .endpoint(azureOpenAIConfig.getEndpoint())
		    .credential(new KeyCredential(azureOpenAIConfig.getKey()))
		    .buildClient();

		try {
		ImageAnalysisResult result = client.analyzeFromUrl(
				url, // imageUrl: the URL of the image to analyze
			    Arrays.asList(VisualFeatures.CAPTION), // visualFeatures
			    new ImageAnalysisOptions().setGenderNeutralCaption(true)); // options:  Set to 'true' or 'false' (relevant for CAPTION or DENSE_CAPTIONS visual features)

			// Print analysis results to the console
			System.out.println("Image analysis results:");
			System.out.println(" Caption:");
			System.out.println("   \"" + result.getCaption().getText() + "\", Confidence "
			    + String.format("%.4f", result.getCaption().getConfidence()));
			
            return new ApiReturnObject(200, "分析成功", result.getCaption().getText());

		} catch (HttpResponseException e) {
			System.out.println("Exception: " + e.getClass().getSimpleName());
			System.out.println("Status code: " + e.getResponse().getStatusCode());
			System.out.println("Message: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage());
		}
        return new ApiReturnObject(500, "分析失敗", null);
	}

	@Override
	public ApiReturnObject extractTextFromImage(MultipartFile file) {

		ImageAnalysisClient client = new ImageAnalysisClientBuilder()
		    .endpoint(azureOpenAIConfig.getEndpoint())
		    .credential(new KeyCredential(azureOpenAIConfig.getKey()))
		    .buildClient();

		try {
		ImageAnalysisResult result = client.analyze(
				BinaryData.fromBytes(file.getBytes()), // imageData: Image file loaded into memory as BinaryData
			    Arrays.asList(VisualFeatures.READ), // visualFeatures
			    null); // options: There are no options for READ visual feature
		
        	StringBuilder extractedText = new StringBuilder();
			// Print analysis results to the console
			System.out.println("Image analysis results:");
			System.out.println(" Read:");
			for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
				extractedText.append(line.getText()).append("\n");
			    System.out.println("Line: '" + line.getText() + "',Bounding polygon " + line.getBoundingPolygon());
			    for (DetectedTextWord word : line.getWords()) {
			        System.out.println("Word: '" + word.getText() + "',Bounding polygon " + word.getBoundingPolygon() + ",Confidence " + String.format("%.4f", word.getConfidence()));
			    }
			}
			return new ApiReturnObject(200, "分析成功", extractedText.toString());
			
		} catch (HttpResponseException e) {
			System.out.println("Exception: " + e.getClass().getSimpleName());
			System.out.println("Status code: " + e.getResponse().getStatusCode());
			System.out.println("Message: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage());
		}
        return new ApiReturnObject(500, "分析失敗", null);
	}

	@Override
	public ApiReturnObject extractTextFromURL(String url) {

		ImageAnalysisClient client = new ImageAnalysisClientBuilder()
		    .endpoint(azureOpenAIConfig.getEndpoint())
		    .credential(new KeyCredential(azureOpenAIConfig.getKey()))
		    .buildClient();

		try {
			ImageAnalysisResult result = client.analyzeFromUrl(
				url, // imageUrl: the URL of the image to analyze
				Arrays.asList(VisualFeatures.READ), // visualFeatures
				null); // options: There are no options for READ visual feature

        	StringBuilder extractedText = new StringBuilder();
			// Print analysis results to the console
			System.out.println("Image analysis results:");
			System.out.println(" Read:");
			for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
				System.out.println("   Line: '" + line.getText()
				    + "', Bounding polygon " + line.getBoundingPolygon());
				for (DetectedTextWord word : line.getWords()) {
					System.out.println("     Word: '" + word.getText()
					+ "', Bounding polygon " + word.getBoundingPolygon()
					+ ", Confidence " + String.format("%.4f", word.getConfidence()));
				}
			}
			return new ApiReturnObject(200, "分析成功", extractedText.toString());
			
		} catch (HttpResponseException e) {
			System.out.println("Exception: " + e.getClass().getSimpleName());
			System.out.println("Status code: " + e.getResponse().getStatusCode());
			System.out.println("Message: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage());
		}
        return new ApiReturnObject(500, "分析失敗", null);
	}
}
