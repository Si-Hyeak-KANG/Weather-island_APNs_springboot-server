package project.app.apns_server.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.dto.ApnRequestDto;
import project.app.apns_server.modules.dto.WeatherKitHeaderDto;
import project.app.apns_server.modules.dto.WeatherKitPayloadDto;
import project.app.apns_server.modules.vo.AppInfoVo;

@Service
@RequiredArgsConstructor
public class ObjectMapperService {

    private final ObjectMapper objectMapper;

    public String serializeApnRequestDto(ApnRequestDto dto){
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeAppInfoVo(AppInfoVo dto){
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public AppInfoVo deserializeAppInfoVo(String value){
        try {
            return objectMapper.readValue(value, AppInfoVo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeWeatherApiResponseDto(WeatherApiResponseDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeWeatherKitHeaderDto(WeatherKitHeaderDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeWeatherKitPayloadDto(WeatherKitPayloadDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
