package com.sudeepcv.springboot.blog.service.impl;

import com.sudeepcv.springboot.blog.entity.Post;
import com.sudeepcv.springboot.blog.exception.ResourceNotFoundException;
import com.sudeepcv.springboot.blog.payload.PostDto;
import com.sudeepcv.springboot.blog.payload.PostResponse;
import com.sudeepcv.springboot.blog.repository.PostRepository;
import com.sudeepcv.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);

        Post newPost=postRepository.save(post);

        PostDto postResponse=mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortOrder) {

         Pageable pageable=sortOrder.equals("DEC")? PageRequest.of(pageNo,pageSize,Sort.by(sortBy).descending()):
                 PageRequest.of(pageNo,pageSize,Sort.by(sortBy));

        Page<Post> posts=postRepository.findAll(pageable);
//        get content form page object
        List<Post> postList=posts.getContent();
        List<PostDto> content=postList.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setLast(posts.isLast());
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post=  postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post=  postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        Post updatedPost=postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        Post post=  postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post){
        PostDto postDto=modelMapper.map(post,PostDto.class);
//        PostDto postDto=new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }
    private Post mapToEntity(PostDto postDto){
        Post post =modelMapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
        return post;
    }
}
