package org.choongang.school.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.admin.school.controllers.SchoolSearch;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.QSchool;
import org.choongang.school.entities.School;
import org.choongang.school.repositories.SchoolRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolInfoService {
    private final EntityManager em;
    private final HttpServletRequest request;

    private final SchoolUtil util;
    private final SchoolRepository repository;
    private final FileInfoService fileInfoService;

    public School get(String domain) {
        School school = repository.getByDomain(domain).orElseThrow(SchoolNotFoundException::new);

        addSchoolInfo(school);

        return school;
    }

    //////////////////////////////return Type ListData<School> 로 바꿔야 페이징 처리 가능..///////////////////////////////////////////////////
    public List<School> getList(){
        List<School> list = repository.findAll();
        return list;
    }

    //이것만 만들면 getList 대신 이거 사용하면 됨
    public ListData<School> getList2(SchoolSearch search){

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        System.out.println(page+"ddddddddddddddddddddddddddddddddddddddddd");
        /*페이지 블럭 수*/
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 4);
        int offset = (page - 1) * limit; // 레코드 시작 위치

        QSchool school = QSchool.school;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt(); //검색타입 ALL sName domain
        String skey = search.getSkey(); //검색어

        if(StringUtils.hasText(skey)) { //검색어 있을 경우
            skey = skey.trim();

            BooleanExpression sNameCond = school.schoolName.contains(skey);// 학교명 - schoolName LIKE '%skey%';
            BooleanExpression domainCond = school.domain.contains(skey);// 도메인 - domain LIKE '%skey%';

            if (sopt.equals("sName")) {//학교명 검색
                andBuilder.and(sNameCond);

            } else if (sopt.equals("domain")) {//도메인 검색
                andBuilder.and(domainCond);

            } else if (sopt.equals("ALL")) {//전체 검색 (도메인 + 학교명)

                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(sNameCond)
                        .or(domainCond);

                andBuilder.and(orBuilder);

            }
        }
        PathBuilder<School> pathBuilder = new PathBuilder<>(School.class, "school");

        List<School> items = new JPAQueryFactory(em)
                .selectFrom(school)
                .offset(offset)
                .limit(limit)
                .where(andBuilder)
                .orderBy(
                        new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt"))
                )
                .fetch();


        long total = repository.count(andBuilder);
        //페이지 구간 개수
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

    public School get(Long num) {
        School school = repository.findById(num).orElseThrow(SchoolNotFoundException::new);

        addSchoolInfo(school);

        return school;
    }

    public RequestSchool getForm(Long num) {
        School school = get(num);

        RequestSchool form = new ModelMapper().map(school, RequestSchool.class);
        form.setDomain(school.getDomain());
        form.setGid(school.getGid());
        form.setNum(school.getNum());
        form.setComment(school.getContent());

        form.setMode("edit");

        return form;
    }

    public void addSchoolInfo(School school) {
        if (school == null) {
            return;
        }
        String gid = school.getGid();

        List<FileInfo> banner_top = fileInfoService.getListDone(gid, "banner_top");
        List<FileInfo> banner_bottom = fileInfoService.getListDone(gid, "banner_bottom");

        List<FileInfo> logoImage = fileInfoService.getListDone(gid, "logo");

        if (logoImage != null && !logoImage.isEmpty()) {
            school.setLogoImage(logoImage.get(0));
        }

        if (banner_top != null && !banner_top.isEmpty()) {
            school.setBanner_top(banner_top.get(0));
        }

        if (banner_bottom != null && !banner_bottom.isEmpty()) {
            school.setBanner_bottom(banner_bottom.get(0));
        }
    }


}
