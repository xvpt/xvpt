package org.xvpt.website.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.dto.ReviewPhotoDTO;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.service.PhotoService;

@RestController
@RequestMapping("/api/admin/photo")
@RequiredArgsConstructor
public class PhotoAdminController {
    private final PhotoService photoService;

    @PostMapping("review")
    public ResponseEntity<RestBean<PhotoVO>> review(@RequestBody ReviewPhotoDTO dto) {
        try {
            return ResponseEntity.ok(RestBean.success(photoService.review(dto)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @GetMapping("padding")
    public ResponseEntity<RestBean<Page<PhotoVO>>> paddingPhotos(@RequestParam int page) {
        return ResponseEntity.ok(RestBean.success(photoService.listPending(PageRequest.of(page, 50))));
    }

    @GetMapping("rejected")
    public ResponseEntity<RestBean<Page<PhotoVO>>> rejectedPhotos(@RequestParam int page) {
        return ResponseEntity.ok(RestBean.success(photoService.listRejected(PageRequest.of(page, 50))));
    }
}
