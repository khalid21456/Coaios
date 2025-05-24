package com.Coaios.AISocialMedia.tools;


import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@Description("""
            Read 5 recent posts to comment or to like on one of theme
        """)

@Service("readPosts")
public class ReadPosts implements Function<ReadPosts.Request, ReadPosts.Response> {

    @Autowired
    private PostService postService;

    @Override
    public Response apply(Request request) {
        System.out.println("ReadPosts tool is used");
        return new Response();
    }

    public record Request(){};

    public record Response(
    ){};


}
