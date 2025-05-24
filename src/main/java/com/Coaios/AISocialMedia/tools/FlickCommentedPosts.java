package com.Coaios.AISocialMedia.tools;


import com.Coaios.AISocialMedia.domain.entities.FlickComment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.repository.FlickCommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@Description("""
            Those are the posts you commented
        """)

@Service("postCommentedByFlick")
public class FlickCommentedPosts implements Function<FlickCommentedPosts.Request, FlickCommentedPosts.Response> {

    @Autowired
    private FlickCommentRepo flickCommentRepo;

    @Override
    public Response apply(Request request) {
        System.out.println("ReadPosts tool is used");
        List<FlickComment> posts = flickCommentRepo.findAll();
        Iterator<FlickComment> iter = posts.iterator();
        FlickComment flickComment = null;
        List<Post> posts1 = new ArrayList<>();
        while(iter.hasNext()) {
            flickComment = iter.next();
            Post post = flickComment.getPost();
            post.setUser(null);
            posts1.add(post);
        }
        return new Response(posts1);
    }

    public record Request(){};

    public record Response(
            List<Post> posts
    ){};


}
