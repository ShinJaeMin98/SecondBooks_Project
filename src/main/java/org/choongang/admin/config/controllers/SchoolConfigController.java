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
                int i = l[0].indexOf(" ");
                if(i != -1){
                    l[0] = l[0].substring(0,i).trim();
                }
                l[1] = l[1].replace("www.","");
                l[1] = l[1].replaceAll("/" , "");
                schools += l[0]+"_"+l[1]+"\n";
                k++;
                if(k == 40){
                    break;
                }
            }
            config.setSchools(schools);
        }
    }



}