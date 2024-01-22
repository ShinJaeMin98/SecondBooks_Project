package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.SchoolSearch;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolInfoService {

    private final SchoolUtil util;
    private final SchoolRepository repository;

    //////////////////////////////return Type ListData<School> 로 바꿔야 페이징 처리 가능..///////////////////////////////////////////////////
    public List<School> getList(SchoolSearch search){
        List<School> list = repository.findAll();
        return list;
    }

    public List<School> getAllList(){
        List<School> list = repository.findAll();
        return list;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public School findSchoolByNum(Long num) {

        School school = repository.getById(num);

        return school;
    }

    public School findSchoolByDomain(String domain){

        School school = repository.findByDomain(domain);

        return school;
    }

}
