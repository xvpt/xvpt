package org.xvpt.website.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.dto.DeletePhotoDTO;
import org.xvpt.website.entity.dto.EnterCompetitionDTO;
import org.xvpt.website.entity.dto.VoteDTO;
import org.xvpt.website.entity.vo.MediaVO;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.service.MediaService;
import org.xvpt.website.service.PhotoService;

import java.io.InputStream;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final MediaService mediaService;
    private final PhotoService photoService;

    @PostMapping("upload")
    public ResponseEntity<RestBean<MediaVO>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(RestBean.success(mediaService.upload(file)));
    }

    @PostMapping("enter")
    public ResponseEntity<RestBean<PhotoVO>> joinCompetition(@RequestBody EnterCompetitionDTO dto, Authentication authentication) throws Exception {
        try {
            return ResponseEntity.ok(RestBean.success(photoService.enterCompetition(dto, authentication)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @GetMapping("list")
    public ResponseEntity<RestBean<Page<PhotoVO>>> listPhotos(@RequestParam int page, @RequestParam(name = "competition") String competitionId, Authentication authentication) {
        return ResponseEntity.ok(RestBean.success(photoService.listPhotos(PageRequest.of(page, 50), competitionId, authentication)));
    }

    @DeleteMapping
    public ResponseEntity<RestBean<?>> deletePhoto(@RequestBody DeletePhotoDTO dto, Authentication authentication) {
        try {
            photoService.deletePhoto(dto, authentication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
        return ResponseEntity.ok(RestBean.success("Ok"));
    }

    @GetMapping
    public void download(@RequestParam("id") String id, @RequestParam(name = "origin", defaultValue = "false") boolean origin, HttpServletResponse response) throws Exception {
        InputStream stream = photoService.getFile(id, origin);
        if (stream == null) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }
        IOUtils.copy(stream, response.getOutputStream());
    }

    @PostMapping("vote")
    public ResponseEntity<RestBean<PhotoVO>> vote(@RequestBody VoteDTO dto, Authentication authentication) {
        try {
            return ResponseEntity.ok(RestBean.success(photoService.vote(dto.getId(), authentication)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @DeleteMapping("vote")
    public ResponseEntity<RestBean<PhotoVO>> cancelVote(@RequestBody VoteDTO dto, Authentication authentication) {
        try {
            return ResponseEntity.ok(RestBean.success(photoService.cancelVote(dto.getId(), authentication)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }
}
