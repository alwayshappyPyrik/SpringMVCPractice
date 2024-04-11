package org.example.mapper;

import org.example.model.PostDTO;
import org.example.model.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostEntity mapToPostEntity(PostDTO postDTO) {
        PostEntity postEntity = new PostEntity();
        boolean removed = postEntity.isRemoved();
        return new PostEntity(postDTO.getId(), postDTO.getContent(), removed);
    }

    public PostDTO mapToPostDTO(PostEntity postEntity) {
        long id = postEntity.getId();
        String context = postEntity.getContent();
        return new PostDTO(id, context);
    }
}
