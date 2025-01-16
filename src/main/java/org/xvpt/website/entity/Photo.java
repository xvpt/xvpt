package org.xvpt.website.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Photo extends TrackingEntity {
    @Id
    private String id;

    private String description;

    @DBRef
    private User owner;
    @DBRef
    private Competition competition;

    private PhotoStatus status;

    @DBRef
    private Media compressedMedia;
    @DBRef
    private Media originMedia;
}
