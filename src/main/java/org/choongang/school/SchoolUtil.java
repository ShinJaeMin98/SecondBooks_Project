package org.choongang.school;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.SchoolConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolUtil {
    private final ConfigInfoService configInfoService;

    public List<String[]> getSchools() {
        SchoolConfig config = configInfoService.get("school_config", SchoolConfig.class)
                .orElseGet(SchoolConfig::new);

        String _schools = config.getSchools();


        return null;
    }
}