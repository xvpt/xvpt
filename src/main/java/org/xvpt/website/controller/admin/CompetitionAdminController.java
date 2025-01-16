package org.xvpt.website.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.dto.DeleteCompetitionDTO;
import org.xvpt.website.entity.dto.HostCompetitionDTO;
import org.xvpt.website.entity.dto.ModifyCompetitionDTO;
import org.xvpt.website.entity.vo.CompetitionVO;
import org.xvpt.website.service.CompetitionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/competition")
public class CompetitionAdminController {
    private final CompetitionService competitionService;

    @PostMapping("host")
    public ResponseEntity<RestBean<CompetitionVO>> create(@RequestBody HostCompetitionDTO dto, Authentication authentication) {
        return ResponseEntity.ok(RestBean.success(competitionService.host(dto, authentication)));
    }

    @PostMapping("modify")
    public ResponseEntity<RestBean<CompetitionVO>> modify(@RequestBody ModifyCompetitionDTO dto, Authentication authentication) {
        try {
            return ResponseEntity.ok(RestBean.success(competitionService.modify(dto, authentication)));
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @PostMapping("thumbnail")
    public ResponseEntity<RestBean<CompetitionVO>> uploadThumbnail(@RequestParam("id") String id, @RequestParam("file") MultipartFile file, Authentication authentication) throws Exception {
        try {
            return ResponseEntity.ok(RestBean.success(competitionService.uploadThumbnail(id, file, authentication)));
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<RestBean<?>> deleteCompetition(@RequestBody DeleteCompetitionDTO dto, Authentication authentication) {
        try {
            competitionService.deleteCompetition(dto, authentication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
        return ResponseEntity.ok(RestBean.success("Ok"));
    }

    @GetMapping("list")
    public List<CompetitionVO> list(Authentication authentication) {
        return competitionService.listOwn(authentication);
    }
}
