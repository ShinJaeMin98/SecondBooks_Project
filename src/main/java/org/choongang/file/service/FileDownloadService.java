package org.choongang.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;

    public void download(Long seq) {
        FileInfo data = infoService.get(seq);
        String filePath = data.getFilePath();

        // 파일명 -> 2바이트 인코딩으로 변경 (윈도우즈 시스템에서 한글 깨짐 방지)
        String fileName = null;
        try {
            fileName = new String(data.getFileName().getBytes(), "ISO8859_1");
        } catch (UnsupportedEncodingException e) {}

        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream out = response.getOutputStream(); // 응답 Body에 출력

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setIntHeader("Expires", 0); // 만료 시간 X
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Length", String.valueOf(file.length()));

            while(bis.available() > 0) {
                out.write(bis.read());
            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}