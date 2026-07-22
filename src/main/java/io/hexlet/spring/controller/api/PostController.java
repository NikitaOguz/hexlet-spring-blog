package io.hexlet.spring.controller.api;

import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostController(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }
    @GetMapping
    public Page<PostDTO> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return postRepository.findByPublishedTrue(pageable)
                .map(postMapper::toDTO);
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post with id " + id + " not found"));

        return postMapper.toDTO(post);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody Post post) {

        Post saved = postRepository.save(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody Post data) {

        var post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setAuthor(data.getAuthor());

        Post updated = postRepository.save(post);

        return ResponseEntity.ok(postMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));

        postRepository.delete(post);

        return ResponseEntity.noContent().build();
    }
}
