package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
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
    public ResponseEntity<Post> show(@PathVariable Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(201).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(
            @PathVariable Long id,
            @RequestBody Post data) {

        var maybePost = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();

        if (maybePost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var post = maybePost.get();
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setAuthor(data.getAuthor());
        post.setCreatedAt(data.getCreatedAt());

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        posts.removeIf(post -> post.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
