package org.xvpt.website.service;

import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.vo.MediaVO;

import java.io.IOException;
import java.io.InputStream;

public interface MediaService {
    Media save(MultipartFile file) throws IOException;

    MediaVO upload(MultipartFile file) throws IOException;

    InputStream getFile(String id) throws IOException;

    Media compressImage(Media originMedia) throws IOException;

    Media compressImage(MultipartFile file) throws IOException;
}
