package org.xvpt.website.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
public class Competition extends TrackingEntity {
    @Id
    private String id;

    private String name;
    private String description;
    @DBRef
    private User host;
    @DBRef
    private Media thumbnail;

    private LocalDateTime endDate;
    private int maxUploads;

    public boolean isOver() {
        return endDate.isBefore(LocalDateTime.now());
    }
}
