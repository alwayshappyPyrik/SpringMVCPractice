package org.example.controller;

import org.example.mapper.PostMapper;
import org.example.model.PostDTO;
import org.example.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;
    private final PostMapper postMapper;

    public PostController(PostService service, PostMapper postMapper) {
        this.service = service;
        this.postMapper = postMapper;
    }

    @GetMapping
    public List<PostDTO> all() {
        return service.all();
    }

//    @GetMapping("/{id}")
//    public PostDTO getById(@PathVariable("id") long id) {
//        return service.getById(id);
//    }

    @PostMapping
    public PostDTO save(@RequestBody PostDTO postDTO) {
        return service.save(postDTO);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable("id") long id) {
        service.removeById(id);
    }
}
