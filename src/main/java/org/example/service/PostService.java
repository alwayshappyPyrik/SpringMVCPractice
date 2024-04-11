package org.example.service;

import org.example.exception.NotFoundException;
import org.example.mapper.PostMapper;
import org.example.model.PostDTO;
import org.example.model.PostEntity;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository repository;
    private final PostMapper postMapper;

    public PostService(PostRepository repository, PostMapper postMapper) {
        this.repository = repository;
        this.postMapper = postMapper;
    }

    public List<PostDTO> all() {
        return repository.all()
                .stream()
                .map(postMapper::mapToPostDTO)
                .collect(Collectors.toList());
    }

//    public PostDTO getById(long id) {
//        return repository.getById(id).orElseThrow(NotFoundException::new);
//    }

    public PostDTO save(PostDTO post) {
        PostEntity postEntity = postMapper.mapToPostEntity(post);
        return repository.save(postEntity);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

