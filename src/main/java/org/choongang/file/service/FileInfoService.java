package org.choongang.file.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.choongang.configs.FileProperties;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.entities.QFileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
@Transactional
public class FileInfoService {

    private final FileProperties fileProperties;
    private final FileInfoRepository repository;
    private final HttpServletRequest request;

    public FileInfo get(Long seq) {
        FileInfo fileInfo = repository.findById(seq)
                .orElseThrow(FileNotFoundException::new);


        addFileInfo(fileInfo); // 파일 추가 정보 처리

        return fileInfo;
    }

    /**
     * 파일 목록 조회
     *
     * @param gid
     * @param location
     * @param mode - ALL : 기본값 - 완료, 미완료 모두 조회
     *               DONE : 완료된 파일
     *               UNDONE : 미완료된 파일
     * @return
     */
    public List<FileInfo> getList(String gid, String location, String mode) {
        QFileInfo fileInfo = QFileInfo.fileInfo;

        mode = StringUtils.hasText(mode) ? mode : "ALL";

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fileInfo.gid.eq(gid));

        if (StringUtils.hasText(location)) {
            builder.and(fileInfo.location.eq(location));
        }

        if (!mode.equals("ALL")) {
            builder.and(fileInfo.done.eq(mode.equals("DONE")));
        }

        List<FileInfo> items = (List<FileInfo>)repository.findAll(builder, Sort.by(asc("createdAt")));

        items.forEach(this::addFileInfo);

        return items;
    }

    public List<FileInfo> getList(String gid) {
        return getList(gid, null, "ALL");
    }

    public List<FileInfo> getList(String gid, String location) {
        return getList(gid, location, "ALL");
    }

    public List<FileInfo> getListDone(String gid) {
        return getList(gid, null, "DONE");
    }

    public List<FileInfo> getListDone(String gid, String location) {
        return getList(gid, location, "DONE");
    }

    /**
     * 파일 추가 정보 처리
     *      - 파일 서버 경로(filePath)
     *      - 파일 URL(fileUrl)
     * @param fileInfo
     */
    public void addFileInfo(FileInfo fileInfo) {
        long seq = fileInfo.getSeq();
        long dir = seq % 10L;
        String fileName = seq + fileInfo.getExtension();

        /* 파일 경로, URL S */
        String filePath = fileProperties.getPath() + dir + "/" + fileName;
        String fileUrl = request.getContextPath() + fileProperties.getUrl() + dir + "/" + fileName;

        fileInfo.setFilePath(filePath);
        fileInfo.setFileUrl(fileUrl);
        /* 파일 경로, URL E */

        /* 썸네일 경로, URL S */
        List<String> thumbsPath = new ArrayList<>();
        List<String> thumbsUrl = new ArrayList<>();

        String thumbDir = getThumbDir(seq);
        String thumbUrl = getThumbUrl(seq);

        File _thumbDir = new File(thumbDir);
        if (_thumbDir.exists()) {
            for (String thumbFileName : _thumbDir.list()) {
                thumbsPath.add(thumbDir + "/" + thumbFileName);
                thumbsUrl.add(thumbUrl + "/" + thumbFileName);
            }
        } // endif

        fileInfo.setThumbsPath(thumbsPath);
        fileInfo.setThumbsUrl(thumbsUrl);
        /* 썸네일 경로, URL E*/
    }

    /**
     * 파일별 특정 사이즈 썸네일 조회
     *
     * @param seq
     * @param width
     * @param height
     * @return
     */
    public String[] getThumb(long seq, int width, int height) {
        FileInfo fileInfo = get(seq);
        String fileType = fileInfo.getFileType(); // 파일이 이미지인지 체크
        if (fileType.indexOf("image/") == -1) {
            return null;
        }

        String fileName = seq + fileInfo.getExtension();

        String thumbDir = getThumbDir(seq);
        File _thumbDir = new File(thumbDir);
        if (!_thumbDir.exists()) {
            _thumbDir.mkdirs();
        }

        String thumbPath = String.format("%s/%d_%d_%s", thumbDir, width, height, fileName);
        File _thumbPath = new File(thumbPath);
        if (!_thumbPath.exists()) { // 썸네일 이미지가 없는 경우
            try {
                Thumbnails.of(new File(fileInfo.getFilePath()))
                        .size(width, height)
                        .toFile(_thumbPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String thumbUrl = String.format("%s/%d_%d_%s", getThumbUrl(seq), width, height, fileName);

        return new String[] { thumbPath, thumbUrl };
    }

    public String getThumbDir(long seq) {

        String thumbDirCommon = "thumbs/" + (seq % 10L) + "/" + seq;
        return fileProperties.getPath() + thumbDirCommon;
    }

    public String getThumbUrl(long seq) {
        String thumbDirCommon = "thumbs/" + (seq % 10L) + "/" + seq;
        return fileProperties.getUrl() + thumbDirCommon;
    }
}
