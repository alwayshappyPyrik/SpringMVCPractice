package org.example.service;

import org.example.exception.NotFoundException;
import org.example.mapper.PostMapper;
import org.example.model.PostEntity;
import org.example.model.PostParameter;
import org.example.model.PostResult;
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

    public List<PostResult> all() {
        return repository.all()
                .stream()
                .map(postMapper::mapToPostResult)
                .collect(Collectors.toList());
    }

    public PostResult getById(long id) {
        PostEntity postEntity = repository.getById(id).orElseThrow(NotFoundException::new);
        return postMapper.mapToPostResult(postEntity);
    }

    public PostResult save(PostParameter postParameter) {
        PostEntity postEntity = repository.save(postParameter);
        return postMapper.mapToPostResult(postEntity);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

