package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.*;
import org.xvpt.website.entity.dto.JoinCompetitionDTO;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.repository.CompetitionRepository;
import org.xvpt.website.repository.MediaRepository;
import org.xvpt.website.repository.PhotoRepository;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.MediaService;
import org.xvpt.website.service.PhotoMapper;
import org.xvpt.website.service.PhotoService;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final CompetitionRepository competitionRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PhotoMapper photoMapper;

    @Override
    public InputStream getFile(String photoId, boolean origin) throws IOException {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find photo: " + photoId));
        if (photo.getStatus() != PhotoStatus.ACCEPTED) {
            return null; // rejected or pending a review
        }
        if (origin) {
            return mediaService.getFile(photo.getOriginMedia().getId());
        }
        return mediaService.getFile(photo.getCompetition().getId());
    }

    @Override
    public PhotoVO joinCompetition(@NotNull JoinCompetitionDTO dto, @NotNull Authentication authentication) throws IOException {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = competitionRepository.findById(dto.getCompetition())
                .orElseThrow(() -> new IllegalArgumentException("Could not find competition: " + dto.getCompetition()));
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        Media media = mediaRepository.findById(dto.getMedia())
                .orElseThrow(() -> new IllegalArgumentException("Could not find media: " + dto.getMedia()));
        Photo photo = new Photo();
        photo.setDescription(dto.getDescription());
        photo.setOwner(user);
        photo.setCompetition(competition);
        photo.setOriginMedia(media);
        photo.setStatus(PhotoStatus.PENDING);
        Media compressedMedia = mediaService.compressImage(media);
        photo.setCompressedMedia(compressedMedia);
        return photoMapper.toPhotoVO(photoRepository.save(photo));
    }
}
