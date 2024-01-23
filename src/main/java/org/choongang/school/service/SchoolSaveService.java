package org.choongang.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.file.service.FileUploadService;
import org.choongang.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolSaveService {

    private final SchoolUtil util;
    private final SchoolRepository repository;
    private final SchoolInfoService infoService;
    private final FileUploadService fileUploadService;

    public void save(RequestSchool form){

        //추가일 경우
        if(form.getNum()==-1L || form.getMode().equals("add")){
            School school = new School();

            String schoolName = util.getSchoolName(form.getDomain());

            school.setGid(form.getGid());
            school.setSchoolName(schoolName);
            school.setDomain(form.getDomain());
            school.setContent(form.getComment());
            school.setMenuLocation(form.getMenuLocation());

            //System.out.println(school+"=================저장==============");
            repository.saveAndFlush(school);

            fileUploadService.processDone(school.getGid());

        }
        else {  //수정일 경우
            School school = infoService.findSchoolByNum(form.getNum());

            String domain = form.getDomain();

            school.setDomain(domain);
            school.setMenuLocation(form.getMenuLocation());
            school.setSchoolName(util.getSchoolName(domain));
            school.setContent(form.getComment());
            repository.saveAndFlush(school);
            System.out.println(school+"=================수정==============");
            fileUploadService.processDone(school.getGid());
        }

    }

}
