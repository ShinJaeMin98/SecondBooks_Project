package org.choongang.admin.school.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.repositories.SchoolRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolVerifyService {

    private final SchoolRepository repository;

    public boolean doubleCheck(String domain){




        return false;
    }


}
