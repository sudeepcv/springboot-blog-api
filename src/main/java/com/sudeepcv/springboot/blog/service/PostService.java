package com.sudeepcv.springboot.blog.service;

import com.sudeepcv.springboot.blog.payload.PostDto;
import com.sudeepcv.springboot.blog.payload.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortOrder);

    PostDto getPostById(Long id);

    PostDto updatePost(PostDto postDto,Long id);

    void deletePostById(Long id);
}
