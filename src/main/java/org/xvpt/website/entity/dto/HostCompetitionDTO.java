package org.xvpt.website.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostCompetitionDTO {
    private String name;
    private String description;
    private int maxUploads;

    private int duration; // Unit: days
}
