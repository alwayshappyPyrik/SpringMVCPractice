package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.mapper.PostMapper;
import org.example.model.PostEntity;
import org.example.model.PostParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final Map<Long, PostEntity> listPosts = new ConcurrentHashMap<>();
    private final AtomicLong postID = new AtomicLong();
    private final PostMapper mapper;

    public PostRepository(PostMapper mapper) {
        this.mapper = mapper;
    }

    public List<PostEntity> all() {
        List<PostEntity> postEntities = new ArrayList<>();
        for (PostEntity value : listPosts.values()) {
            if (!value.isRemoved()) {
                long id = value.getId();
                String context = value.getContent();
                postEntities.add(new PostEntity(id, context, false));
            }
        }
        return postEntities;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Поста с таким id нету в репозитории")
    public Optional<PostEntity> getById(long id) {
        PostEntity postEntity = listPosts.get(id);
        if (postEntity != null && !postEntity.isRemoved()) {
            return Optional.of(listPosts.get(id));
        } else {
            throw new NotFoundException("Поста с таким id нету в репозитории");
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Поста с таким id нету в репозитории")
    public PostEntity save(PostParameter postParameter) {
        PostEntity postEntity = mapper.mapToPostEntity(postParameter);
        if (postParameter.getId() == 0) {
            long id = postParameter.getId();
            long newId = findFreeId(id);
            listPosts.put(postID.incrementAndGet(), new PostEntity(newId, postParameter.getContent(), false));
            return postEntity;
        } else {
            if (listPosts.containsKey(postParameter.getId()) && !postEntity.isRemoved()) {
                final long id = listPosts.get(postParameter.getId()).getId();
                listPosts.put(id, new PostEntity(postParameter.getId(), postParameter.getContent(), false));
                return postEntity;
            } else {
                throw new NotFoundException("Поста с таким id нету в репозитории");
            }
        }
    }

    public long findFreeId(long id) {
        if (listPosts.isEmpty()) {
            return ++id;
        } else {
            PostEntity maxValueInMap = Collections.max(listPosts.values());
            id = maxValueInMap.getId();
            return ++id;
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void removeById(long id) {
        PostEntity postEntity = listPosts.get(id);
        if (postEntity != null && !postEntity.isRemoved()) {
            postEntity.setRemoved(true);
        } else if (postEntity != null && postEntity.isRemoved()) {
            throw new NotFoundException("Пост с таким id уже помечен, как удаленный");
//          Если хотим физическое удаление :)
//          listPosts.remove(id);
        } else if (postEntity == null) {
            throw new NotFoundException("Поста с таким id нету в репозитории");
        }
    }
}
