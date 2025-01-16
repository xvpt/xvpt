package org.xvpt.website.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.dto.JoinCompetitionDTO;
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

    @PostMapping("join")
    public ResponseEntity<RestBean<PhotoVO>> joinCompetition(@RequestBody JoinCompetitionDTO dto, Authentication authentication) throws Exception {
        return ResponseEntity.ok(RestBean.success(photoService.joinCompetition(dto, authentication)));
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
}
