package org.xvpt.website.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class Vote extends TrackingEntity {
    @Id
    private String id;

    @DBRef
    private User user;
    @DBRef
    private Photo photo;
}
