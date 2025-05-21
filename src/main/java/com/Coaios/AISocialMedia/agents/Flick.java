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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

    private String[] tools = new String[]{"readPosts"};

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

        List<Post> recentPosts = postService.getPostsForAgent(); // or whatever method you use
        StringBuilder prompt = new StringBuilder("Choose one post to comment on by returning only its ID:\n");

        for (Post p : recentPosts) {
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
        return null;
    }

    public Comment commentPost3() {
        //Post post = new Post();
        // Step 1: Get recent posts and build prompt
        List<Post> recentPosts = postService.getPostsForAgent();
        StringBuilder prompt = new StringBuilder("Choose one post to comment on by returning only its ID:\n");

        for (Post p : recentPosts) {
            prompt.append("ID: ").append(p.getId()).append(" → ").append(p.getTitle()).append("\n");
        }
        prompt.append("The post should not match your systemPrompt.");

        // Step 2: AI chooses a post ID
        String idStr = chatClient.prompt()
                .system(systemPrompt)
                .user(prompt.toString())
                .call()
                .content()
                .toString()
                .trim();

        if (idStr.toLowerCase().startsWith("id:")) {
            idStr = idStr.substring(3).trim();
        }

        Long postId = null;
        try {
            postId = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            System.err.println("❌ Could not parse post ID from AI response: " + idStr);
            return null;
        }

        System.out.println("✅ Parsed Post ID: " + postId);

        // Step 3: Retrieve the post
        //post = postRepo.findById(postId).orElse(null);
        Post post = entityManager.getReference(Post.class, postId);
        if (post == null) {
            System.err.println("❌ Post not found for ID: " + postId);
            return null;
        }

        // Step 4: Ask the AI to generate a comment
        String commentText = chatClient.prompt()
                .system(systemPrompt)
                .user("Write a short comment for this post: [" + post.getContent() + "]")
                .call()
                .content()
                .toString()
                .trim();

        System.out.println("💬 AI Comment: " + commentText);

        // Step 5: Retrieve the user
        User user = userRepo.findById(this.id).orElse(null);
        if (user == null) {
            System.err.println("❌ User not found with ID: " + this.id);
            return null;
        }

        // Step 6: Create and save the comment
        Comment comment = new Comment();
        comment.setContent(commentText);
        comment.setUser_comment(user);
        comment.setPost(post); // ✅ don't null anything on post

        commentRepo.save(comment);
        System.out.println("✅ Comment saved successfully.");
        return null;
    }

    @Transactional // Add this annotation to ensure proper transaction management
    public Comment commentPost4() {
        try {
            // Step 1: Get recent posts and build prompt
            List<Post> recentPosts = postService.getPostsForAgent();
            if (recentPosts.isEmpty()) {
                System.err.println("❌ No posts available to comment on");
                return null;
            }

            // Print all available post IDs for debugging
            System.out.println("Available posts: " + recentPosts.stream()
                    .map(p -> p.getId().toString())
                    .collect(Collectors.joining(", ")));

            StringBuilder prompt = new StringBuilder("Choose one post to comment on by returning only its ID:\n");
            for (Post p : recentPosts) {
                prompt.append("ID: ").append(p.getId()).append(" → ").append(p.getTitle()).append("\n");
            }
            prompt.append("The post should not match your systemPrompt.");

            // Step 2: AI chooses a post ID
            String aiResponse = chatClient.prompt()
                    .system(systemPrompt)
                    .user(prompt.toString())
                    .call()
                    .content()
                    .toString()
                    .trim();

            String idStr = aiResponse;
            if (idStr.toLowerCase().startsWith("id:")) {
                idStr = idStr.substring(3).trim();
            }

            // Extract just the number if there's any other text
            idStr = idStr.replaceAll("[^0-9]", "");

            if (idStr.isEmpty()) {
                System.err.println("❌ Could not extract post ID from AI response: " + aiResponse);
                return null;
            }

            Long postId = Long.parseLong(idStr);
            System.out.println("✅ Parsed Post ID: " + postId);

            // Step 3: IMPORTANT - Verify the post ID is in our available posts list
            boolean postExists = recentPosts.stream()
                    .anyMatch(p -> p.getId().equals(postId));

            if (!postExists) {
                System.err.println("❌ Post ID " + postId + " is not in the available posts list");
                return null;
            }

            // Step 3: Retrieve the post - using repository directly
            Post post = postRepo.findById(postId).orElse(null);
            if (post == null) {
                System.err.println("❌ Post not found for ID: " + postId);
                return null;
            }

            // Verify the post is fully loaded
            System.out.println("Found post: ID=" + post.getId() + ", Title=" + post.getTitle());

            // Step 4: Ask the AI to generate a comment
            String commentText = chatClient.prompt()
                    .system(systemPrompt)
                    .user("Write a short comment for this post: [" + post.getContent() + "]")
                    .call()
                    .content()
                    .toString()
                    .trim();

            System.out.println("💬 AI Comment: " + commentText);

            // Step 5: Retrieve the user
            User user = userRepo.findById(this.id).orElse(null);
            if (user == null) {
                System.err.println("❌ User not found with ID: " + this.id);
                return null;
            }

            // Verify the user is fully loaded
            System.out.println("Found user: ID=" + user.getId());

            // Step 6: Create and save the comment - IMPORTANT: Use fully loaded entities
            Comment comment = new Comment();
            comment.setContent(commentText);
            comment.setUser_comment(user);
            comment.setPost(post);

            // Debug output before saving
            System.out.println("⚙️ Pre-save: Comment with content: " + comment.getContent());
            System.out.println("⚙️ Pre-save: User reference: " + (comment.getUser_comment() != null ? comment.getUser_comment().getId() : "NULL"));
            System.out.println("⚙️ Pre-save: Post reference: " + (comment.getPost() != null ? comment.getPost().getId() : "NULL"));

            // If we got here and post is still null, create a temporary fix by setting nullable=true
            if (comment.getPost() == null) {
                System.err.println("❌ Critical error: Post reference is null before saving despite checks!");

                // Temporary solution: Use a DTO approach
                return createCommentWithoutPostConstraint(commentText, user, postId);
            }

            Comment savedComment = commentRepo.save(comment);
            System.out.println("✅ Comment saved successfully with ID: " + savedComment.getId());

            return savedComment;
        } catch (Exception e) {
            System.err.println("❌ Error creating comment: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Fallback method if the post reference is persistently null
    private Comment createCommentWithoutPostConstraint(String commentText, User user, Long postId) {
        try {
            // Execute a direct SQL insert to bypass JPA constraint issues
            // NOTE: This is a temporary workaround until the root cause is fixed

            // First save the comment without a post reference
            Comment comment = new Comment();
            comment.setContent(commentText);
            comment.setUser_comment(user);
            Comment savedComment = commentRepo.save(comment);

            // Then update the post_id directly with SQL
            // This assumes you have access to EntityManager
            Query query = entityManager.createNativeQuery(
                    "UPDATE comment SET post_id = :postId WHERE id = :commentId");
            query.setParameter("postId", postId);
            query.setParameter("commentId", savedComment.getId());
            int updatedRows = query.executeUpdate();

            System.out.println("⚙️ Direct SQL update affected " + updatedRows + " rows");

            // Refresh the comment entity to load the updated values
            entityManager.refresh(savedComment);

            return savedComment;
        } catch (Exception e) {
            System.err.println("❌ Error in fallback method: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}