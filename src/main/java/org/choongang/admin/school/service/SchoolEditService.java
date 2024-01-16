package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolEditService {

    private final SchoolUtil util;
    private final SchoolRepository repository;
    private final SchoolSearchService searchService;


    public void edit(Long num , RequestSchool form){
        School school = searchService.findSchoolByNum(num);

        school.setDomain(form.getDomain());
        school.setMenuLocation(form.getMenuLocation());
        school.setSchoolName(util.getSchoolName(form.getDomain()));

    }



}
