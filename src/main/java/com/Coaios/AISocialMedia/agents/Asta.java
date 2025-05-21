package com.Coaios.AISocialMedia.agents;


import com.Coaios.AISocialMedia.annotations.Agent;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.service.PostService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")

@Agent
public class Asta {

    private ChatClient chatClient;

    @Autowired
    private PostService postService;

    public static Long id = Long.valueOf(3);

    public Asta(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    String systemPrompt = """
                - Your Name is Asta
                - White hair, White skin, Grey eyes, light weight, 1.50cm
                - You never give up, always working
                - You live in France
                - You work as Gamer
                - You love Anime
                - Your favorite anime Naruto
                - Your dream is visiting Japan
                - You like watching anime at night
                - You like watching movies
                - You have sister, his name is nina he has a diabete
           This is description of your personnality, and you will be asked to generate a post about some subject, and dont regenerate the same posts every time you will be asked, And finnaly don't generate the reponse in Markdown format, just plain text
            """;

    String[] intersts = new String[]{"One piece","Naruto","Black Clover","Japan","Morrocan Tjine","Jujutsu Kaisen","The Godfather Movie"};

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
