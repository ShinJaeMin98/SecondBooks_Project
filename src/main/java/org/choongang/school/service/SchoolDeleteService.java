package org.choongang.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.file.service.FileDeleteService;
import org.choongang.school.repositories.SchoolRepository;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolDeleteService {

    private final SchoolRepository repository;

    private final FileDeleteService fileDeleteService;

    public void delete (Long num){
        School school = repository.findById(num).orElse(null);

        String gid = school.getGid();

        repository.delete(school);

        fileDeleteService.delete(gid);

        repository.flush();

    }
    public void deleteChks (List<Long> chks){
        for(Long num : chks){
            School school = repository.findById(num).orElse(null);
            delete(school.getNum());
            repository.flush();
        }
    }


}
