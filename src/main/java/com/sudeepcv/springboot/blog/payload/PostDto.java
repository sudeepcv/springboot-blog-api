package com.sudeepcv.springboot.blog.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min = 2,message = "post title should have atleat two characters")
    private String title;
    @NotEmpty
    @Size(min=10,message = "description should have 10 charaters")
    private String description;
    @NotEmpty
    @Size(min = 10,message = "content should have 10 charaters")
    private String content;
    private Set<CommentDto> comments;
}
