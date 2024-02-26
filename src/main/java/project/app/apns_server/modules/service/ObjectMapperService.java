package project.app.apns_server.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.service.apns.dto.ApnRequestDto;

@Service
@RequiredArgsConstructor
public class ObjectMapperService {

    private final ObjectMapper objectMapper;

    public String serializeApnRequestDto(ApnRequestDto dto){
        try {
            return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
