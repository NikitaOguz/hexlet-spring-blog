package io.hexlet.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.model.Post;
import org.springframework.http.ResponseEntity;
@SpringBootApplication
@RestController
public class Application {
	// Хранилище добавленных страниц, то есть обычный список
	private List<Post> posts = new ArrayList<Post>();

	// Запуск приложения
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/posts") // Список страниц
	public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
		var result = posts.stream().limit(limit).toList();

		return ResponseEntity.ok()
				.header("X-Total-Count", String.valueOf(posts.size()))
				.body(result);
	}

	@GetMapping("/posts/{id}") // Получение одного поста по id
	public ResponseEntity<Post> show(@PathVariable  Long id) {
		var post = posts.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst();
		return ResponseEntity.of(post);
	}

	@PostMapping("/posts") // Создание страницы
	public ResponseEntity<Post> create(@RequestBody Post post) {
		posts.add(post);
		return ResponseEntity.status(201).body(post);
	}

	@PutMapping("/posts/{id}") // Обновление страницы
	public ResponseEntity<Post> update(@PathVariable  Long id, @RequestBody Post data) {
		var maybePost = posts.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst();

		if (maybePost.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}

		var post = maybePost.get();
		post.setTitle(data.getTitle());
		post.setContent(data.getContent());
		post.setAuthor(data.getAuthor());
		post.setCreatedAt(data.getCreatedAt());

		return ResponseEntity.ok(post); // 200
	}

	@DeleteMapping("/posts/{id}") // Удаление страницы
	public ResponseEntity<Void> destroy(@PathVariable Long id) {
		posts.removeIf(p -> p.getId().equals(id));
		return ResponseEntity.noContent().build();
	}
}
