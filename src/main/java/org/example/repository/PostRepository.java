package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.mapper.PostMapper;
import org.example.model.PostDTO;
import org.example.model.PostEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        for (PostEntity value : listPosts.values()) {
            if (!value.isRemoved()) {
                long id = value.getId();
                String context = value.getContent();
                listPosts.put(id, new PostEntity(id, context, false));
            }
        }
        return new ArrayList<>(listPosts.values());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Поста с таким id нету")
    public Optional<PostEntity> getById(long id) {
        PostEntity postEntity = listPosts.get(id);
        if (!postEntity.isRemoved()) {
            PostDTO postDTO = mapper.mapToPostDTO(postEntity);
            return Optional.ofNullable(listPosts.get(id));
        } else {
            throw new NotFoundException("Поста с таким id нету");
        }
    }

    //    Если от клиента приходит пост с id=0, значит, это создание нового поста. Вы сохраняете его в списке и присваиваете ему новый id.
    //    Если от клиента приходит пост с id !=0, значит, это сохранение (обновление) существующего поста. Вы ищете его в списке по id и обновляете.


    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Поста с таким id нету")
    public PostDTO save(PostEntity post) {
        PostDTO postDTO = mapper.mapToPostDTO(post);
        if (post.getId() == 0) {
            listPosts.put(postID.getAndIncrement(), new PostEntity(post.getId(), post.getContent(), false));
            return postDTO;
        } else {
            PostEntity postId = listPosts.get(post.getId());
            if (listPosts.containsKey(post.getId()) && !post.isRemoved()) {
                final long id = listPosts.get(post.getId()).getId();
                listPosts.put(id, new PostEntity(post.getId(), post.getContent(), false));
                return postDTO;
            } else if (postId == null) {
                throw new NotFoundException("Поста с таким id нету");
            }
        }
        return postDTO;
    }

    public void removeById(long id) {
        PostEntity postEntity = listPosts.get(id);
        if (postEntity.isRemoved()) {
            listPosts.remove(id);
        } else {
            postEntity.setRemoved(true);
        }
    }
}
