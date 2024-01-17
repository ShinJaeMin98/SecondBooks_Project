package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolSearchService {

    private final SchoolUtil util;
    private final SchoolRepository repository;


    public List<School> getList(){
        List<School> list = repository.findAll();
        return list;
    }

    public List<School> getSearchList(){
        List<School> list = null;
        return list;
    }

    public School findSchoolByNum(Long num){

        School school = repository.getById(num);

        return school;
    }


}
