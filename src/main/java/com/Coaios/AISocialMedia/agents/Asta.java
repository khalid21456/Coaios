package com.Coaios.AISocialMedia.agents;


import com.Coaios.AISocialMedia.annotations.Agent;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import com.Coaios.AISocialMedia.service.PostService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")

@Agent
public class Asta {

    private ChatClient chatClient;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

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

    public Comment commentPost() {

        List<Post> recentPosts = postService.getPostsForAgent(this.id); // or whatever method you use
        StringBuilder prompt = new StringBuilder("Choose one post to comment on by returning only its ID:\n");


        List<Post> recentPostNotCommented = new ArrayList<>();
        for (Post p : recentPosts) {
            boolean exists = true;
            List<Comment> comments = commentRepo.findByPostId(p.getId());
            Iterator<Comment> iter = comments.iterator();
            while(iter.hasNext()) {
                Comment comment = iter.next();
                if(comment.getUser_comment().getId() == this.id) {
                    exists = false;
                    break;
                }
            }
            if(exists) {
                recentPostNotCommented.add(p);
            }
        }

        if(recentPostNotCommented.isEmpty()) {
            return null;
        }
        for (Post p : recentPostNotCommented) {
            prompt.append("ID: ").append(p.getId()).append(" → ").append(p.getTitle()).append("\n");
        }
        prompt.append("The post should not match you systemPrompt");
        String idStr = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt.toString())
                .call()
                .content()
                .toString()
                .trim();
        if (idStr.toLowerCase().startsWith("id:")) {
            idStr = idStr.substring(3).trim(); // now "6"
        }
        Long postId = Long.parseLong(idStr);
        System.out.println("Parsed Post ID: " + postId);
        Post post = postRepo.findById(postId).get();
        if (post == null) {
            System.err.println("Post not found for ID: " + postId);
            return null;
        }
        String commentText = chatClient.prompt()
                .system(systemPrompt)
                .user("Write a short comment for this post: [" + post.getContent() + "]")
                .call()
                .content()
                .toString();
        System.out.println(commentText);
        User user = userRepo.findById(this.id).get();
        Comment comment = new Comment();
        comment.setContent(commentText);
        comment.setUser_comment(user);
        post.getUser().setPosts(null);
        //post.setComments(null);
        comment.setPost(post);
        commentRepo.save(comment);
        return comment;
    }

}
