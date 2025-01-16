package org.xvpt.website.entity.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionVO {
    private String id;

    private String name;
    private String description;
    private String host;
    private long endDate;

    private int maxUploads;
    private int enteredPhotos;
}
