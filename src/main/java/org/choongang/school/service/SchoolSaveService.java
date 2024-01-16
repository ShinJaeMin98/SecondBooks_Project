package org.choongang.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entities.Configs;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.commons.Utils;
import org.choongang.school.entities.School;
import org.choongang.school.repositories.SchoolRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolSaveService {

    private final Utils utils;
    private final SchoolRepository repository;
    private final Configs configs;

    public void save (RequestSchool form){

        School school = new School();
        school.setGid(form.getGid());
        school.setDomain(form.getDomain());
        school.setMenuLocation(form.getMenuLocation());



        repository.saveAndFlush(school);

    }

}
