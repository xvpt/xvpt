package org.xvpt.website.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterCompetitionDTO {
    private String competition;
    private String media; // media id
    private String description; // photo description
}
