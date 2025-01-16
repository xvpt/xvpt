package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.Competition;
import org.xvpt.website.entity.vo.CompetitionVO;
import org.xvpt.website.repository.PhotoRepository;
import org.xvpt.website.service.CompetitionMapper;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CompetitionMapperImpl implements CompetitionMapper {
    private final PhotoRepository photoRepository;

    @Override
    public CompetitionVO toCompetitionVO(@NotNull Competition competition) {
        CompetitionVO competitionVO = new CompetitionVO();
        competitionVO.setId(competition.getId());
        competitionVO.setName(competition.getName());
        competitionVO.setDescription(competition.getDescription());
        competitionVO.setHost(competition.getHost().getId());
        competitionVO.setEndDate(competition.getEndDate().toEpochSecond(ZoneOffset.UTC));
        competitionVO.setMaxUploads(competition.getMaxUploads());
        competitionVO.setEnteredPhotos(photoRepository.countByCompetition(competition));
        return competitionVO;
    }
}
