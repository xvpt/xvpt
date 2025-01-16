package org.xvpt.website.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xvpt.website.XvptApplication;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.vo.CompetitionVO;
import org.xvpt.website.service.CompetitionService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/competition")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @GetMapping
    public ResponseEntity<RestBean<Page<CompetitionVO>>> list(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(RestBean.success(competitionService.list(PageRequest.of(page, size))));
    }

    @GetMapping("thumbnail")
    public void thumbnailImage(@RequestParam String id, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        response.setContentType("image/webp");
        try {
            inputStream = competitionService.getThumbnail(id);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write(RestBean.failure(404, "Competition not found").toJson());
        }
        if (inputStream == null) {
            // return the default thumbnail
            inputStream = XvptApplication.class.getResourceAsStream("/default-thumbnail.webp");
        }
        assert inputStream != null;
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
