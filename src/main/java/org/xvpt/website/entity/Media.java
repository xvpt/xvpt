package org.xvpt.website.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Media extends TrackingEntity {
    @Id
    private String id;

    private String contentType;
}
