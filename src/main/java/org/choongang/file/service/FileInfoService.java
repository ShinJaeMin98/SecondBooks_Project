package org.choongang.file.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.configs.FileProperties;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {

    private final FileProperties fileProperties;
    private final FileInfoRepository repository;
    private final HttpServletRequest request;

    public FileInfo get(Long seq){
        FileInfo fileInfo = repository.findById(seq)
                .orElseThrow(FileNotFoundException::new);

        addFileInfo(fileInfo);

        return fileInfo;
    }

    public List<FileInfo> getList(String gid, String location, String mode){
        QFileInfo fileInfo = QFileInfo.fileInfo;

        mode = StringUtils.hasText(mode) ? mode : "ALL";

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fileInfo.gid.eq(gid));

        if(StringUtils.hasText(location)){
            builder.and(fileInfo.location.eq(location));
        }

        if(!mode.equals("ALL")){
            builder.and(fileInfo.done.eq(mode.equals("DONE")));
        }

        List<FileInfo> items = (List<FileInfo>)repository.findByAll(builder, Sort.by(asc("createdAt")));


    }





}
