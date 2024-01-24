package org.choongang.admin.config.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExcelUtils;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

@Controller
@RequestMapping("/admin/config/school")
@RequiredArgsConstructor
public class SchoolConfigController implements ExceptionProcessor {

    private final ConfigInfoService infoService;
    private final ConfigSaveService saveService;
    private final ExcelUtils excelUtils;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "config";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {
        return Menu.getMenus("config");
    }

    @ModelAttribute("subMenuCode")
    public String getSubMenuCode() {

        return "school";
    }

    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "학교설정";
    }

    @GetMapping
    public String index(Model model) {

        SchoolConfig config = infoService.get("school_config", SchoolConfig.class).orElseGet(SchoolConfig::new);

        insertSchool(config);

        model.addAttribute("schoolConfig", config);


        return "admin/config/school";
    }

    @PostMapping
    public String save(SchoolConfig config, Model model) {

        saveService.save("school_config", config);

        model.addAttribute("message", "저장 되었습니다.");

        return "admin/config/school";
    }

    /**
     * 엑셀에 형식이 다른 학교들 처리 필요
     * @param config
     */

    public void insertSchool(SchoolConfig config){
        if(config.getSchools() == null || config.getSchools().equals("")){
            List<String[]> list = excelUtils.getData("data/schools.xlsx", new int[] {0, 1}, 0);
            String schools = "";
            int k = 0;
            for(String[] l : list){
                //l[0] : 학교명 , l[1] : 도메인

                l[1] = l[1].replace("www.","")
                        .replace("https://" , "");

                int v = schools.indexOf(l[0]);

                if(!l[1].equals("") && l[1] != null){
                    if(v == -1){
                        schools += l[0]+"_"+l[1]+"\n";
                    }
                }

                /**
                 * 400까지가 대학교 이후는 대학원 , 대학원은 도메인이 대학교와 다르고, 같은 대학원이어도 전공마다 도메인이 다름
                 * ( 고려대 , 고려대 대학원 ) => 고려대 , ( 연세대 연세대 대학원 ) => 연세대 이렇게 같은 학교로 묶을 수 있으면 상관 없는데
                 * 고려대 korea.ac.kr , 고려대 교육대학원 korea.ac.kr/edu , 고려대 의과대학원 korea.ac.kr/medi 이런식으로 달라서
                 * ( 고려대 , 고려대 대학원 ) => 고려대 / 같은 취급으로 묶어주려면 좀 귀찮을듯
                 */
                k++;
                if(k == 423){
                    break;
                }



            }
            config.setSchools(schools);
        }
    }



}