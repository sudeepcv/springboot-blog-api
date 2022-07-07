package com.sudeepcv.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    private long id;
    @NotEmpty
    @Size(min = 3,message = "the name should have at least 3 char")
    private String name;
    @NotEmpty
    @Email(message = "not a valid email")
    private String email;
    @NotEmpty
    @Size(min = 10,max = 1000,message = "the comment should have at least 10 and at most of 1000 char ")
    private String body;
}
