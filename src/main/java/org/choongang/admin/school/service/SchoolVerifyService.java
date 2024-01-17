package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolVerifyService {

    private final SchoolRepository repository;

    public boolean doubleCheck(String domain){

        List<School> schools = repository.findByDomain(domain);

        if(schools.size() == 0){    //등록 가능하면 t
            System.out.println("등록되지 않은 학교 입니다.");
            return true;
        } else {    //등록 가능하면 이미 등록 됐으면 f
            System.out.println("등록된 학교 입니다.");
            return false;
        }


    }


}
