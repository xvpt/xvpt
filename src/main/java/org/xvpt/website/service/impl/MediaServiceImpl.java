package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.vo.MediaVO;
import org.xvpt.website.repository.MediaRepository;
import org.xvpt.website.service.MediaMapper;
import org.xvpt.website.service.MediaService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @Value("${xvpt.file-store}")
    private String fileStore;

    @Override
    public Media save(@NotNull MultipartFile file) throws IOException {
        Media entity = new Media();
        entity.setContentType(file.getContentType());
        Media media = mediaRepository.save(entity);
        log.info("Saving media: {}", media.getId());
        // save to local
        File localFile = new File(fileStore, media.getId());
        FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);
        return media;
    }

    @Override
    public MediaVO upload(MultipartFile file) throws IOException {
        return mediaMapper.toMediaVO(this.save(file));
    }

    @Override
    public InputStream getFile(String id) throws IOException {
        if (id == null) return null;
        if (!mediaRepository.existsById(id)) {
            return null; // media not found
        }
        File localFile = new File(fileStore, id);
        return FileUtils.openInputStream(localFile);
    }

    @Override
    public Media compressImage(@NotNull Media originMedia) throws IOException {
        if (!originMedia.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Unsupported image type: " + originMedia.getContentType());
        }
        Media entity = new Media();
        entity.setContentType("image/webp");
        Media compressedMedia = mediaRepository.save(entity);
        File out = new File(fileStore, compressedMedia.getId());
        compressImage(getFile(originMedia.getId()), out);
        return compressedMedia;
    }

    @Override
    public Media compressImage(@NotNull MultipartFile file) throws IOException {
        if (Objects.requireNonNull(file.getContentType()).startsWith("image/") && !file.getContentType().equals("image/webp")) {
            Media entity = new Media();
            entity.setContentType("image/webp");
            Media compressedMedia = mediaRepository.save(entity);
            File out = new File(fileStore, compressedMedia.getId());
            compressImage(file.getInputStream(), out);
            return compressedMedia;
        }
        return save(file);
    }

    private void compressImage(InputStream inputStream, File out) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        ImageIO.write(image, "webp", out);
    }
}
