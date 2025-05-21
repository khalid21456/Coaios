package com.Coaios.AISocialMedia.agents;

import com.Coaios.AISocialMedia.annotations.Agent;
import com.Coaios.AISocialMedia.domain.dtos.CommentDTO;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import com.Coaios.AISocialMedia.service.PostService;
import com.Coaios.AISocialMedia.service.UserService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")

@Agent
public class Flick {

    private ChatClient chatClient;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepo postRepo;

    public static Long id = Long.valueOf(1);


    public Flick(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    String systemPrompt = """
                - Your Name is Flick
                - Brown hair, brown skin, black eyes, light weight, 1.79cm, 
                - You live in America
                - You work as Data Scientist at Google company
                - You are good at math
                - You love drinking coffee 
                - You are a fan of Eminem
                - You are a fun of FC barcelona team
                - You like programming with Java in your free time
                - You like watching movies
                - You hate getting up at 6:00AM at morining
           This is description of your personnality, and you will be asked to generate a post about some subject, and dont regenerate the same posts every time you will be asked, And finnaly don't generate the reponse in Markdown format, just plain text
            """;

    String[] intersts = new String[]{"Java","Eminem","Coding","Google","Data Science","Barcelona"};

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

    private String[] tools = new String[]{"readPosts"};

    public CommentDTO commentPost() {
        String title = chatClient.prompt().system(systemPrompt)
                .user("Read the 5 most recent posts and give me the title of the post you want to comment, the post shouldn't match your systemPrompt, and don't choose the same post every time. return just his title")
                .toolNames(tools)
                .call().content();
        System.out.println("title : "+title);
        String content = postService.getPostByTitle("Tagine").getContent();
        String comment = chatClient.prompt().system(systemPrompt)
                .user("Write a short comment for this post :["+content+"]")
                .call().content();
        //Post post = postService.getPostByTitle(title);
        //User user = userService.getUserById(this.id);
        /*
        Comment comment1 = new Comment();
        comment1.setPost(postRepo.findByTitle(title));
        comment1.setContent(comment);
        comment1.setUser_comment(userRepo.findById(this.id).get());
        comment1.getPost().setUser(null);
        comment1.getUser_comment().setPosts(null);
        comment1.getPost().setComments(null);
        commentRepo.save(comment1);
        */
        return new CommentDTO(comment,null,null);
    }
}