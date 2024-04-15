package org.example.controller;

import org.example.model.PostParameter;
import org.example.model.PostResult;
import org.example.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<PostResult> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public PostResult getById(@PathVariable("id") long id) {
        return service.getById(id);
    }

    @PostMapping
    public PostResult save(@RequestBody PostParameter postParameter) {
        return service.save(postParameter);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable("id") long id) {
        service.removeById(id);
    }
}
