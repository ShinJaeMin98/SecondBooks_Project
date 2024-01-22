package org.choongang.school.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.SchoolSearch;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.school.entities.QSchool;
import org.choongang.school.repositories.SchoolRepository;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolInfoService {
    private final EntityManager em;
    private final HttpServletRequest request;

    private final SchoolUtil util;
    private final SchoolRepository repository;

    //////////////////////////////return Type ListData<School> 로 바꿔야 페이징 처리 가능..///////////////////////////////////////////////////
    public List<School> getList(){
        List<School> list = repository.findAll();
        return list;
    }

    //이것만 만들면 getList 대신 이거 사용하면 됨
    public ListData<School> getList2(SchoolSearch search){

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        System.out.println(page+"ddddddddddddddddddddddddddddddddddddddddd");
        int pageOfSchool = 5;/* 1 페이지당 학교 수 */

        int limit = Utils.onlyPositiveNumber(search.getLimit(), pageOfSchool);
        int offset = (page - 1) * limit; // 레코드 시작 위치

        QSchool school = QSchool.school;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt(); //검색타입 ALL sName domain
        String skey = search.getSkey(); //검색어

        List<School> items = null;
        long total = 0L;

        if(StringUtils.hasText(skey)){ //검색어 있을 경우
            skey = skey.trim();

            BooleanExpression sNameCond = school.schoolName.contains(skey);// 학교명 - schoolName LIKE '%skey%';
            BooleanExpression domainCond = school.domain.contains(skey);// 도메인 - domain LIKE '%skey%';

            if (sopt.equals("sName")){//학교명 검색
                andBuilder.and(sNameCond);

            }else if (sopt.equals("domain")){//도메인 검색
                andBuilder.and(domainCond);

            }else if (sopt.equals("ALL")){//전체 검색 (도메인 + 학교명)

                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(sNameCond)
                        .or(domainCond);

                andBuilder.and(orBuilder);

            }
            items = new JPAQueryFactory(em)
                    .selectFrom(school)
                    .offset(offset)
                    .limit(limit)
                    .where(andBuilder)
                    .fetch();




        } else { //검색어 없을 경우

            items = repository.findAll();
        }

        total = items.size();                                   //페이지 구간 개수
        Pagination pagination = new Pagination(page, (int)total, 5, limit, request);


        return new ListData <>(items, pagination);
    }











    /////////////////////////////////////////////////////////////////////////////////


    /**
     *  회원가입시 이메일 선택 목록에서 사용
     *
     */
    public List<School> getAllList(){
        List<School> list = repository.findAll();
        return list;
    }


    public School findSchoolByNum(Long num) {

        School school = repository.getById(num);

        return school;
    }

    public School findSchoolByDomain(String domain){

        School school = repository.findByDomain(domain);

        return school;
    }

}
