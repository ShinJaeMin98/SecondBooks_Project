package org.choongang.school;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.SchoolConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolUtil {
    private final ConfigInfoService configInfoService;



    public List<String[]> getSchools() {
        SchoolConfig config = configInfoService.get("school_config", SchoolConfig.class)
                .orElseGet(SchoolConfig::new);

        String _schools = config.getSchools();

        List<String[]> schools = StringUtils.hasText(_schools) ?
                Arrays.stream(_schools.trim().split("\\n"))
                        .map(s -> s.replaceAll("\\r", ""))
                        .map(s -> s.split("_"))
                        .toList()
                : null;

        return schools;

    }

    /**
     * 학교명 -> 이메일 도메인
     *
     * @param schoolName
     * @return
     */
    public String getDomain(String schoolName) {
        List<String[]> schools = getSchools();

        if (schools != null) {
            return schools.stream()
                    .filter(s -> s[0].equals(schoolName))
                    .findFirst()
                    .map(s -> s[1])
                    .orElse("");
        }

        return null;
    }

    /**
     * 이메일 도메인 -> 학교명
     *
     * @param domain
     * @return
     */
    public String getSchoolName(String domain) {
        List<String[]> schools = getSchools();

        if (schools != null) {
            return schools.stream()
                    .filter(s -> s[1].equals(domain))
                    .findFirst()
                    .map(s -> s[0])
                    .orElse("");
        }

        return null;
    }
}