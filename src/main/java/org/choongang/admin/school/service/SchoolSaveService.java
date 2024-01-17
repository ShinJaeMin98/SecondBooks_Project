package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolSaveService {

    private final SchoolUtil util;
    private final SchoolRepository repository;
    private final SchoolSearchService searchService;

    public void save(RequestSchool form){

        if(form.getNum() == null || form.getNum()==-1L || form.getMode().equals("add")){
            School school = new School();

            String schoolName = util.getSchoolName(form.getDomain());

            school.setGid(form.getGid());
            school.setSchoolName(schoolName);
            school.setDomain(form.getDomain());
            school.setMenuLocation(form.getMenuLocation());

            System.out.println(school+"===============================");
            repository.saveAndFlush(school);
        }
        else {
            School school = searchService.findSchoolByNum(form.getNum());

            school.setDomain(form.getDomain());
            school.setMenuLocation(form.getMenuLocation());
            school.setSchoolName(util.getSchoolName(form.getDomain()));
        }

    }

}
