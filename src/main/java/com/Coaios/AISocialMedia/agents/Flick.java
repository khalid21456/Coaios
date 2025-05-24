package com.Coaios.AISocialMedia.agents;

import com.Coaios.AISocialMedia.annotations.Agent;
import com.Coaios.AISocialMedia.domain.dtos.CommentDTO;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.FlickComment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import com.Coaios.AISocialMedia.repository.FlickCommentRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import com.Coaios.AISocialMedia.service.PostService;
import com.Coaios.AISocialMedia.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SuppressWarnings("unused")

@Agent
public class Flick {

    private ChatClient chatClient;

    @Autowired
    private FlickCommentRepo flickCommentRepo;

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
    @PersistenceContext
    EntityManager entityManager;
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

    private String[] tools = new String[]{"postCommentedByFlick"};

    public Comment commentPost() {
        String title = chatClient.prompt().system(systemPrompt)
                .user("Read the 5 most recent posts and give me the title of the post you want to comment, the post shouldn't match your systemPrompt, and don't choose the same post every time. return **only** the exact title string. Do not modify it")
                .toolNames(tools)
                .call().content();
        System.out.println("title : "+title);
        String content = postService.getPostByTitle("Japan").getContent();
        String comment = chatClient.prompt().system(systemPrompt)
                .user("Write a short comment for this post :["+content+"]")
                .call().content();
        Post post = postRepo.findByTitle("Japan");
        User user = userRepo.findById(this.id).get();
        Comment comment1 = new Comment();
        comment1.setPost(post);
        comment1.setContent(comment);
        comment1.setUser_comment(user);
        return commentRepo.save(comment1);
    }

    public Comment commentPost2() {

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
        if(idStr.equals("")) {
            return null;
        }
        if (idStr.toLowerCase().startsWith("id:")) {
            idStr = idStr.substring(3).trim(); // now "6"
        }
        Long postId = Long.parseLong(idStr);
        //flickCommentRepo.save(flickCommentRepo.findById(postId).get());
        System.out.println("Parsed Post ID: " + postId);
        Post post = postRepo.findById(postId).get();
        //flickCommentRepo.save(new FlickComment(post));
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