package org.xvpt.website.service;

import org.xvpt.website.entity.Competition;
import org.xvpt.website.entity.vo.CompetitionVO;

public interface CompetitionMapper {
    CompetitionVO toCompetitionVO(Competition competition);
}
