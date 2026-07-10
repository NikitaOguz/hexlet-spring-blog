package io.hexlet.spring.controller;

import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Post>> index(
            @RequestParam(defaultValue = "10") Integer limit) {

        var result = posts.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public Post show(@PathVariable Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(
            @PathVariable Long id,
            @Valid @RequestBody Post data) {

        var post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setAuthor(data.getAuthor());
        post.setCreatedAt(data.getCreatedAt());

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {

        var post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id " + id + " not found"));

        posts.remove(post);

        return ResponseEntity.noContent().build();
    }
}
