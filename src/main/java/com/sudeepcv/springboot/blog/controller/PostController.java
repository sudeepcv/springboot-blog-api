package com.sudeepcv.springboot.blog.controller;

import com.sudeepcv.springboot.blog.entity.Post;
import com.sudeepcv.springboot.blog.payload.PostDto;
import com.sudeepcv.springboot.blog.payload.PostResponse;
import com.sudeepcv.springboot.blog.service.PostService;
import com.sudeepcv.springboot.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

//    if you have only one constructor and the class is a spring bean then don't need @Autowired annotation
//    constructor injection
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }
    @GetMapping
    public PostResponse getAllPosts(@RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                    @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
                                    @RequestParam(value = "sortOrder",defaultValue = AppConstants.DEFAULT_SORT_ORDER,required = false) String sortOrder
                                     ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortOrder);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable (name = "id") Long id ){
        return ResponseEntity.ok(postService.getPostById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable (name = "id") Long id,@Valid @RequestBody PostDto postDto){
    PostDto updatedPost= postService.updatePost(postDto,id);
    return  new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable (name = "id") Long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("post deleted successfully ",HttpStatus.OK);
    }

}
