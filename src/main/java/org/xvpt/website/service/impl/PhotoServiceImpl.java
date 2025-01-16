package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.*;
import org.xvpt.website.entity.dto.DeletePhotoDTO;
import org.xvpt.website.entity.dto.EnterCompetitionDTO;
import org.xvpt.website.entity.dto.ReviewPhotoDTO;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.repository.*;
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
    private final VoteRepository voteRepository;
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
    public PhotoVO enterCompetition(@NotNull EnterCompetitionDTO dto, @NotNull Authentication authentication) throws IOException {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Competition competition = competitionRepository.findById(dto.getCompetition())
                .orElseThrow(() -> new IllegalArgumentException("Could not find competition: " + dto.getCompetition()));
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        Media media = mediaRepository.findById(dto.getMedia())
                .orElseThrow(() -> new IllegalArgumentException("Could not find media: " + dto.getMedia()));
        if (photoRepository.countByOwnerAndCompetition(user, competition) > competition.getMaxUploads()) {
            throw new IllegalArgumentException("You have uploaded too many photos! This contest does not allow uploading more than " + competition.getMaxUploads() + " photos");
        }
        Photo photo = new Photo();
        photo.setDescription(dto.getDescription());
        photo.setOwner(user);
        photo.setCompetition(competition);
        photo.setOriginMedia(media);
        photo.setStatus(PhotoStatus.PENDING);
        Media compressedMedia = mediaService.compressImage(media);
        photo.setCompressedMedia(compressedMedia);
        return photoMapper.toPhotoVO(photoRepository.save(photo), authentication);
    }

    @Override
    public void deletePhoto(@NotNull DeletePhotoDTO dto, @NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Photo photo = photoRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Could not find photo: " + dto.getId()));
        Competition competition = photo.getCompetition();
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        if (!photo.getOwner().equals(user)) {
            throw new IllegalArgumentException("You are not owner of this photo");
        }
        // delete
        photoRepository.delete(photo);
    }

    @Override
    public PhotoVO vote(String photoId, @NotNull Authentication authentication) {
        //noinspection DuplicatedCode
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find photo: " + photoId));
        Competition competition = photo.getCompetition();
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        if (photo.getStatus() != PhotoStatus.ACCEPTED) {
            throw new IllegalArgumentException("You cannot vote this photo until it is accepted");
        }
        if (voteRepository.existsByUserAndPhoto(user, photo)) {
            // voted
            throw new IllegalArgumentException("You already vote this photo");
        }
        // create a Vote object
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPhoto(photo);
        voteRepository.save(vote);
        return photoMapper.toPhotoVO(photoRepository.save(photo), authentication);
    }

    @Override
    public PhotoVO cancelVote(String photoId, @NotNull Authentication authentication) {
        //noinspection DuplicatedCode
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find photo: " + photoId));
        Competition competition = photo.getCompetition();
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        if (photo.getStatus() != PhotoStatus.ACCEPTED) {
            throw new IllegalArgumentException("You cannot vote this photo until it is accepted");
        }
        if (!voteRepository.existsByUserAndPhoto(user, photo)) {
            throw new IllegalArgumentException("You haven't vote this photo");
        }
        voteRepository.deleteByUserAndPhoto(user, photo);
        return photoMapper.toPhotoVO(photoRepository.save(photo), authentication);
    }

    @Override
    public Page<PhotoVO> listPhotos(PageRequest page, String competitionId, Authentication authentication) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find competition: " + competitionId));
        return photoRepository.findAllByCompetitionAndStatus(competition, PhotoStatus.ACCEPTED, page)
                .map(vote -> photoMapper.toPhotoVO(vote, authentication));
    }

    @Override
    public PhotoVO review(@NotNull ReviewPhotoDTO dto) {
        Photo photo = photoRepository.findById(dto.getPhoto())
                .orElseThrow(() -> new IllegalArgumentException("Could not find photo: " + dto.getPhoto()));
        Competition competition = photo.getCompetition();
        if (competition.isOver()) {
            throw new IllegalArgumentException("Competition is over");
        }
        if (dto.getStatus() != PhotoStatus.PENDING) {
            throw new IllegalArgumentException("The new status of the photo cannot be PENDING");
        }
        photo.setStatus(dto.getStatus());
        return photoMapper.toPhotoVO(photoRepository.save(photo), null);
    }

    @Override
    public Page<PhotoVO> listPending(PageRequest page) {
        return photoRepository.findAllByStatus(PhotoStatus.PENDING, page)
                .map(photo -> photoMapper.toPhotoVO(photo, null));
    }

    @Override
    public Page<PhotoVO> listRejected(PageRequest page) {
        return photoRepository.findAllByStatus(PhotoStatus.REJECTED, page)
                .map(photo -> photoMapper.toPhotoVO(photo, null));
    }

    @Scheduled(cron = "0 0 */6 * * *")
    private void deleteExpiredPhotos() {
        // delete all photos with status PENDING and REJECTED in overed competitions
        photoRepository.deleteAll(photoRepository.findAllByStatus(PhotoStatus.PENDING)
                .stream()
                .filter(p -> p.getCompetition().isOver())
                .toList()
        );
        photoRepository.deleteAll(photoRepository.findAllByStatus(PhotoStatus.REJECTED)
                .stream()
                .filter(p -> p.getCompetition().isOver())
                .toList()
        );
    }
}
