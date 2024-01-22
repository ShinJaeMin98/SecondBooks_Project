package org.choongang.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.school.repositories.SchoolRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolVerifyService {

    private final SchoolRepository repository;

    public boolean doubleCheck(String name){
        boolean schools = repository.existsBySchoolName(name);
        return schools;
    }


}
