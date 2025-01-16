package org.xvpt.website.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.dto.DeleteCompetitionDTO;
import org.xvpt.website.entity.dto.HostCompetitionDTO;
import org.xvpt.website.entity.dto.ModifyCompetitionDTO;
import org.xvpt.website.entity.vo.CompetitionVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CompetitionService {
    Page<CompetitionVO> list(PageRequest page);

    CompetitionVO host(HostCompetitionDTO dto, Authentication authentication);

    List<CompetitionVO> listOwn(Authentication authentication);

    CompetitionVO modify(ModifyCompetitionDTO dto, Authentication authentication);

    void deleteCompetition(DeleteCompetitionDTO dto, Authentication authentication);

    CompetitionVO uploadThumbnail(String competitionId , MultipartFile file, Authentication authentication) throws IOException;

    InputStream getThumbnail(String competitionId) throws IOException;

    CompetitionVO getCompetition(String id);
}
