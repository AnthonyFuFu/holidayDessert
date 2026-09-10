package com.holidaydessert.service;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatGPTService {

    @Autowired
    private AzureOpenAIConfig azureOpenAIConfig;
    
	public ApiReturnObject getChat(String prompt) {
		
		OpenAIClient client = new OpenAIClientBuilder()
				.credential(new AzureKeyCredential(azureOpenAIConfig.getKey()))
				.endpoint(azureOpenAIConfig.getEndpoint())
				.buildClient();

		List<ChatRequestMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatRequestSystemMessage(prompt));

		ChatCompletions chatCompletions = client.getChatCompletions(azureOpenAIConfig.getDeployment(), new ChatCompletionsOptions(chatMessages));

		String replyContent = chatCompletions.getChoices().get(0).getMessage().getContent();
		
		return ApiReturnObject.success("取得對話成功", replyContent);

	}

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
			log.info("Image analysis results:");
			log.info(" Caption:");
			log.info("   \"" + result.getCaption().getText() + "\", Confidence " 
			    + String.format("%.4f", result.getCaption().getConfidence()));

            return ApiReturnObject.success("分析成功", result.getCaption().getText());

		} catch (HttpResponseException e) {
			log.info("Exception: " + e.getClass().getSimpleName());
			log.info("Status code: " + e.getResponse().getStatusCode());
			log.info("Message: " + e.getMessage());
		} catch (Exception e) {
			log.info("Message: " + e.getMessage());
		}
        return ApiReturnObject.serverError("分析失敗");
	}

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
			log.info("Image analysis results:");
			log.info(" Caption:");
			log.info("   \"" + result.getCaption().getText() + "\", Confidence "
			    + String.format("%.4f", result.getCaption().getConfidence()));
			
            return ApiReturnObject.success("分析成功", result.getCaption().getText());

		} catch (HttpResponseException e) {
			log.info("Exception: " + e.getClass().getSimpleName());
			log.info("Status code: " + e.getResponse().getStatusCode());
			log.info("Message: " + e.getMessage());
		} catch (Exception e) {
			log.info("Message: " + e.getMessage());
		}
        return ApiReturnObject.serverError("分析失敗");
	}

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
			log.info("Image analysis results:");
			log.info(" Read:");
			for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
				extractedText.append(line.getText()).append("\n");
				log.info("Line: '" + line.getText() + "',Bounding polygon " + line.getBoundingPolygon());
			    for (DetectedTextWord word : line.getWords()) {
					log.info("Word: '" + word.getText() + "',Bounding polygon " + word.getBoundingPolygon() + ",Confidence " + String.format("%.4f", word.getConfidence()));
			    }
			}
			return ApiReturnObject.success("分析成功", extractedText.toString());
			
		} catch (HttpResponseException e) {
			log.info("Exception: " + e.getClass().getSimpleName());
			log.info("Status code: " + e.getResponse().getStatusCode());
			log.info("Message: " + e.getMessage());
		} catch (Exception e) {
			log.info("Message: " + e.getMessage());
		}
        return ApiReturnObject.serverError("分析失敗");
	}

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
			log.info("Image analysis results:");
			log.info(" Read:");
			for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
				log.info("   Line: '" + line.getText()
				    + "', Bounding polygon " + line.getBoundingPolygon());
				for (DetectedTextWord word : line.getWords()) {
					log.info("     Word: '" + word.getText()
					+ "', Bounding polygon " + word.getBoundingPolygon()
					+ ", Confidence " + String.format("%.4f", word.getConfidence()));
				}
			}
			return ApiReturnObject.success("分析成功", extractedText.toString());
			
		} catch (HttpResponseException e) {
			log.info("Exception: " + e.getClass().getSimpleName());
			log.info("Status code: " + e.getResponse().getStatusCode());
			log.info("Message: " + e.getMessage());
		} catch (Exception e) {
			log.info("Message: " + e.getMessage());
		}
        return ApiReturnObject.serverError("分析失敗");
	}
	
}
