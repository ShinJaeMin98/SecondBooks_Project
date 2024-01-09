package org.choongang.admin.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entities.Configs;
import org.choongang.admin.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigInfoService {

    private final ConfigsRepository repository;

    public <T> Optional<T> get(String code, Class<T> clazz) {
        return get(code, clazz, null);
    }

    public <T> Optional<T> get(String code, TypeReference<T> typeReference) {
        return get(code, null, typeReference);
    }

    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        Configs config = repository.findById(code).orElse(null);
        if (config == null || !StringUtils.hasText(config.getData())) {
            return Optional.ofNullable(null);
        }

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String jsonString = config.getData();
        try {
            T data = null;
            if (clazz == null) { // TypeRefernce로 처리
                data = om.readValue(jsonString, new TypeReference<T>() {});
            } else { // Class로 처리
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }
}
