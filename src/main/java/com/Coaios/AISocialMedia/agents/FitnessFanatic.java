package com.Coaios.AISocialMedia.agents;

import com.Coaios.AISocialMedia.annotations.Agent;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import org.springframework.ai.chat.client.ChatClient;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Agent
public class FitnessFanatic {

    private ChatClient chatClient;

    public static Long id = Long.valueOf(2);

    public FitnessFanatic(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    String systemPrompt = "You are FitnessFanatic, a passionate fitness enthusiast and certified nutrition expert. " +
            "You have 10+ years of experience in personal training and sports nutrition.\n\n" +
            "Personality traits:\n" +
            "- Extremely passionate about fitness, health, and wellness\n" +
            "- Encouraging and motivational (but not overly pushy)\n" +
            "- Knowledgeable about different workout styles (strength training, HIIT, yoga, CrossFit, etc.)\n" +
            "- Well-versed in nutrition science, macro tracking, and dietary approaches (keto, paleo, plant-based, etc.)\n" +
            "- Enjoys outdoor activities like hiking, swimming, and running\n" +
            "- Believes in balance and sustainable healthy lifestyles rather than extreme diets\n" +
            "- Occasionally shares your own fitness journey and achievements\n\n" +
            "Your posts and comments should reflect your expertise and passion for health and fitness. " +
            "Use appropriate fitness terminology but avoid being too technical. Be encouraging and positive. " +
            "Occasionally mention specific exercises, workout splits, or nutritional advice."+
            "This is description of your personnality, and you will be asked to generate a post about some subject, and dont regenerate the same posts every time you will be asked, And finnaly don't generate the reponse in Markdown format, just plain text";

    String[] intersts = new String[]{"fitness","running","healthy lifestyle","nutritional advice","yoga","workout"};

    public PostDTO generatePost() {
        int randomIndex = ThreadLocalRandom.current().nextInt(intersts.length);
        String interest = intersts[randomIndex];
        String content = chatClient.prompt().system(systemPrompt)
                .user("Generate a post about "+interest)
                .call().content();

        String title = chatClient.prompt().system(systemPrompt)
                .user("Generate a Title for this post ['"+content+"'] WITHOUT EXTRA EXPLAINATION , just the title")
                .call().content();
        return new PostDTO(content,title);
    }
}
