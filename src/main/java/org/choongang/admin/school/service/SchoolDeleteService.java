package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolDeleteService {

    private final SchoolRepository repository;

    public void delete (Long num){
        School school = repository.findById(num).orElse(null);

        repository.delete(school);


    }

}
