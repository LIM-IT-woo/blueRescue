package com.blueRescue.blueRescue.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class DumpAnalysisService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=";

    public String analyzeDumpContent(String errorCode, String causerFile) {
        String url = GEMINI_API_URL + apiKey;
        RestTemplate restTemplate = new RestTemplate();

        // AI 프롬프트 조립
        String prompt = """
            당신은 세계 최고 수준의 Windows 커널 디버깅 전문가입니다.
            제공된 덤프 정보를 바탕으로 불필요한 미사여구나 장황한 인사말은 완전히 제외하되,
            정보를 읽는 사람이 신뢰감을 가질 수 있도록 명확하고 정중한 어체('~입니다.', '~하시기 바랍니다.')를 사용하여 평문으로 출력하세요.
            
            ===[분석 리포트]===
            ■ 오류 코드 (Bug Check Code): [여기에 0x00000000 형태의 코드만 작성]
            ■ 원인 드라이버/파일 (Caused By Driver): [여기에 파일명.sys 형태만 작성]
            
            ■ 핵심 원인 요약:
            [어떤 충돌이 왜 발생했는지 '조건과 결과'를 명확히 나누어 '~ 때문입니다.' 또는 '~ 원인입니다.' 형태로 2줄 이내로 작성]
            
            ■ 추천 해결 단계:
            1. [가장 확실한 조치 사항을 '~하시기 바랍니다.' 또는 '~을 추천합니다.' 형태로 작성]
            2. [그 다음 단계 조치 사항을 '~하시기 바랍니다.' 형태로 작성]
            3. [하드웨어 및 시스템 점검 사항을 '~하시기 바랍니다.' 형태로 작성]
            """;

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        Map<String, Object> partMap = new HashMap<>();

        partMap.put("text", prompt);
        contentMap.put("parts", Collections.singletonList(partMap));
        requestBody.put("contents", Collections.singletonList(contentMap));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("DumpAnalysisService - Gemini API 분석 요청 전송 중... 에러코드: {}", errorCode);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map> candidates = (List<Map>) response.getBody().get("candidates");
                Map content = (Map) candidates.get(0).get("content");
                List<Map> parts = (List<Map>) content.get("parts");
                return (String) parts.get(0).get("text");
            }
        } catch (Exception e) {
            log.error("Gemini API 통신 중 에러 발생: ", e);
            return "AI 분석 엔진 통신 실패: " + e.getMessage();
        }

        return "분석 결과를 가져오지 못했습니다.";
    }
}