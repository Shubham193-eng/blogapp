package com.myblog.blogapp.service.impl;

import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.PostService;
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

     private PostRepository postRepo;
     private ModelMapper mapper;//model mapper is used to mapping dto to entity and entity to dto.
//another way to create bean place of @Autowired
    public PostServiceImpl(PostRepository postRepo,ModelMapper mapper) {
        this.postRepo = postRepo;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
       Post post = mapToEntity(postDto);//it will call method entity and pass dto object.

        Post postEntity =postRepo.save(post);
        PostDto dto=mapToDto(postEntity);//entity to dto it will also call below method
        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable =  PageRequest.of(pageNo, pageSize, sort); //here we are creating pageable object
        Page<Post> posts= postRepo.findAll(pageable);
        List<Post> content = posts.getContent();
        List<PostDto> contents=content.stream().map(post->mapToDto(post)).collect(Collectors.toList());//stream api java 8 feature's

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {
        //if record is not found then only else part will be execute.
       Post post=postRepo.findById(id).orElseThrow(
               ()->new ResourceNotFoundException("post","id",id)
       );

        PostDto postDto=mapToDto(post);
        return postDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) //here we provide dto obj and id no first it will search data in
    {
        //data base based on id number,if data is found then we have to update record based on postDto obj data .
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post newPost = postRepo.save(post);
        return mapToDto(newPost);

    }

    @Override
    public void deletePost(long id) {
        Post post=postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        postRepo.deleteById(id);
    }

    public Post mapToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);//copy obj content form dto to entity.

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
    public PostDto mapToDto(Post post)
    {
        PostDto dto= mapper.map(post, PostDto.class);
//        PostDto dto=new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
        return dto;

    }
}
