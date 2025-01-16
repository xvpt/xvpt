package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.Competition;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.dto.DeleteCompetitionDTO;
import org.xvpt.website.entity.dto.HostCompetitionDTO;
import org.xvpt.website.entity.dto.ModifyCompetitionDTO;
import org.xvpt.website.entity.vo.CompetitionVO;
import org.xvpt.website.repository.CompetitionRepository;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.CompetitionMapper;
import org.xvpt.website.service.CompetitionService;
import org.xvpt.website.service.MediaService;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionMapper competitionMapper;

    private final MediaService mediaService;

    @Override
    public Page<CompetitionVO> list(PageRequest page) {
        return competitionRepository.findAll(page)
                .map(competitionMapper::toCompetitionVO);
    }

    @Override
    public CompetitionVO host(@NotNull HostCompetitionDTO dto, @NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = new Competition();
        competition.setName(dto.getName());
        competition.setDescription(dto.getDescription());
        competition.setHost(user);
        competition.setEndDate(LocalDateTime.now().plusDays(dto.getDuration()));
        log.info("Host competition: {}", competition.getName());
        return competitionMapper.toCompetitionVO(competitionRepository.save(competition));
    }

    @Override
    public List<CompetitionVO> listOwn(@NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        return competitionRepository.findAllByHost(user)
                .stream()
                .map(competitionMapper::toCompetitionVO)
                .toList();
    }

    @Override
    public CompetitionVO modify(@NotNull ModifyCompetitionDTO dto, @NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = competitionRepository.findByIdAndHost(dto.getId(), user)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        competition.setName(dto.getName());
        competition.setDescription(dto.getDescription());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getEndDate()), ZoneOffset.UTC);
        if (endDate.isBefore(now)) {
            throw new IllegalArgumentException("End date is before now");
        }
        competition.setEndDate(endDate);
        return competitionMapper.toCompetitionVO(competitionRepository.save(competition));
    }

    @Override
    public void deleteCompetition(@NotNull DeleteCompetitionDTO dto, @NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = competitionRepository.findByIdAndHost(dto.getId(), user)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        competitionRepository.delete(competition);
        log.info("Deleted competition {}", competition.getName());
    }

    @Override
    public CompetitionVO uploadThumbnail(String competitionId, MultipartFile file, @NotNull Authentication authentication) throws IOException {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = competitionRepository.findByIdAndHost(competitionId, user)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        Media thumbnail = mediaService.compressImage(file);
        competition.setThumbnail(thumbnail);
        return competitionMapper.toCompetitionVO(competitionRepository.save(competition));
    }

    @Override
    public InputStream getThumbnail(String competitionId) throws IOException {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        return mediaService.getFile(competition.getThumbnail().getId());
    }
}
