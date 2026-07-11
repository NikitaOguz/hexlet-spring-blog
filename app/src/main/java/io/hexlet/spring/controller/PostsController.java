package io.hexlet.spring.controller;

import io.hexlet.spring.exception.ResourceNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostRepository postRepository;

    public PostsController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public Page<Post> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return postRepository.findByPublishedTrue(pageable);
    }

    @GetMapping("/{id}")
    public Post show(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        Post saved = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(
            @PathVariable Long id,
            @Valid @RequestBody Post data) {

        var post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setAuthor(data.getAuthor());
        post.setCreatedAt(data.getCreatedAt());

        return ResponseEntity.ok(postRepository.save(post));
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
