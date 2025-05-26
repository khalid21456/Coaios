package com.Coaios.AISocialMedia.domain.dtos;

@SuppressWarnings("unused")
public class CommentUserDTO {

    private Long idPost;
    private Long idUser;
    private String content;


    public Long getIdPost() {
        return idPost;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
