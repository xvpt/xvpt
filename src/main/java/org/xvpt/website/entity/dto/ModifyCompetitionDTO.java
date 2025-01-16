package org.xvpt.website.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyCompetitionDTO {
    private String id;

    private String name;
    private String description;

    private long endDate;
}
