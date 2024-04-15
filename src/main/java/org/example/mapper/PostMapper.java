package org.example.mapper;

import org.example.model.PostEntity;
import org.example.model.PostParameter;
import org.example.model.PostResult;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostResult mapToPostResult(PostEntity postEntity) {
        long id = postEntity.getId();
        String context = postEntity.getContent();
        return new PostResult(id, context);
    }

    public PostEntity mapToPostEntity(PostParameter postParameter) {
        return new PostEntity(postParameter.getId(), postParameter.getContent(), false);
    }
}
