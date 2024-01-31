package org.choongang.myPage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entities.BoardData;
import org.choongang.board.service.BoardInfoService;
import org.choongang.board.service.SaveBoardDataService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.Utils;
import org.choongang.email.service.EmailVerifyService;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.member.service.MemberUpdateService;
import org.choongang.myPage.service.ResignService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController implements ExceptionProcessor {

    private final FileInfoService fileInfoService;

    private final SaveBoardDataService saveBoardDataService;
    private final BoardInfoService boardInfoService;

    private final MemberUpdateService memberUpdateService;
    private final ProfileValidator profileValidator;

    private final ResignValidator resignValidator;

    private final MemberUtil memberUtil;

    private final Utils utils;

    private final EmailVerifyService emailVerifyService;

    private final ResignService resignService;



    @GetMapping // 마이페이지 메인
    public String index(Model model) {
        commonProcess("main", model);

        return utils.tpl("myPage/myPage");
    }

    /**
     * 찜 게시글 목록
     *
     * @param search
     * @param model
     * @return
     */
    @GetMapping("/save_post")
    public String savePost(@ModelAttribute BoardDataSearch search, Model model) {
        commonProcess("save_post", model);

        ListData<BoardData> data = saveBoardDataService.getList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("myPage/save_post");
    }

    @GetMapping("/my_post")
    public String myPost(@ModelAttribute BoardDataSearch search, Model model) {
        commonProcess("my_post", model);

        search.setUserId(memberUtil.getMember().getUserId());
        ListData<BoardData> data = boardInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("myPage/my_post");
    }

    @GetMapping("/profile")
    public String profile(@ModelAttribute RequestProfile form, Model model) {
        commonProcess("profile", model);

        Member member = memberUtil.getMember();
        form.setName(member.getName());
        form.setProfileImage(member.getProfileImage());

        return utils.tpl("myPage/profile");
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid RequestProfile form, Errors errors, Model model) {
        commonProcess("profile", model);

        profileValidator.validate(form, errors);

        if (errors.hasErrors()) {

            String gid = memberUtil.getMember().getGid();

            List<FileInfo> profileImages = fileInfoService.getList(gid);
            form.setProfileImage(profileImages.get(0));
            return utils.tpl("myPage/profile");
        }

        memberUpdateService.update(form);


        return "redirect:/myPage";
    }

    /**
     * 탈퇴페이지 -> 비밀번호 확인
     *          -> 이메일 인증 코드
     * @param model
     * @return
     */
    @GetMapping("/resign")
    public String resignStep1(@ModelAttribute RequestResign form ,  Model model){
        commonProcess("resign" , model);


        return utils.tpl("myPage/resign");
    }

    @PostMapping("/resign")
    public String resignStep2(RequestResign form , Errors errors , Model model){
        commonProcess("resign" , model);
        form.setMode("step1");
        resignValidator.validate(form , errors);

        if(errors.hasErrors()){ //비밀번호 확인 실패시 step1으로
            return utils.tpl("myPage/resign");
        }

        //메일 인증 코드 발송
        emailVerifyService.sendCode(memberUtil.getMember().getEmail());

        return utils.tpl("myPage/resign_auth"); //이메일 코드 검증 페이지
    }

    /**
     * enable -> false, 로그아웃 처리
     * @param model
     * @return
     */
    @PostMapping("/resign_done")
    @PreAuthorize("permitAll()")//비회원도 접근 가능하도록 권한 수정
    public String resignProcess(RequestResign form , Errors errors , Model model){
        commonProcess("resign" , model);
        form.setMode("step2");
        resignValidator.validate(form, errors);

        if (errors.hasErrors()){
            return utils.tpl("myPage/resign_auth");//이메일 인증 실패시 step2로
        }

        //현재 세션에 있는 회원(로그인 되어 있는) 탈퇴처리
        resignService.resign();


        return "redirect:/myPage/resign_done"; //탈퇴 완료
    }

    @GetMapping("/resign_done")
    @PreAuthorize("permitAll()")//비회원도 접근 가능하도록 권한 수정
    public String resignDone(Model model){
        commonProcess("resign" , model);

        return utils.tpl("/myPage/resign_done");
    }



    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";
        String pageTitle = Utils.getMessage("마이페이지", "commons");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        List<String> addCommonScript = new ArrayList<>();


        if (mode.equals("save_post")) { // 찜한 게시글 페이지
            pageTitle = Utils.getMessage("찜_게시글", "commons");

        } else if (mode.equals("my_post")) { // 찜한 게시글 페이지
            pageTitle = Utils.getMessage("내가_쓴_글", "commons");

        } else if (mode.equals("profile")) {
            pageTitle = Utils.getMessage("회원정보_수정", "commons");
            addCommonScript.add("fileManager");
            addScript.add("myPage/profile");

        } else if(mode.equals("resign")){
            pageTitle = Utils.getMessage("회원탈퇴", "commons");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);
    }





}
