package com.blueRescue.blueRescue.controller;

import com.blueRescue.blueRescue.service.DumpAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor // 의존성 주입(DI)을 위해 롬복 어노테이션 추가
@RequestMapping("/api/v1/analysis")
@CrossOrigin(origins = "http://localhost:3000")
public class DumpAnalysisController {

    private final DumpAnalysisService dumpAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDumpFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        // 1. 파일이 비어있는지 확인
        if (file.isEmpty()) {
            log.warn("업로드 요청에 파일이 포함되어 있지 않습니다.");
            response.put("success", false);
            response.put("message", "파일이 비어 있습니다. 올바른 덤프 파일을 선택해 주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 2. 확장자 안전 검증 (.dmp)
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".dmp")) {
            log.warn("차단된 파일 형식 업로드 시도: {}", originalFilename);
            response.put("success", false);
            response.put("message", "확장자가 .dmp인 윈도우 덤프 파일만 업로드할 수 있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        log.info("성공적으로 파일을 수신했습니다. 파일명: {}", originalFilename);

        /* =================================================================
         * [임시 테스트 단계] 원래는 파일 내부를 파싱해야 하지만,
         * 백엔드-AI 연동을 먼저 확인하기 위해 가상의 블루스크린 데이터를 셋팅합니다.
         * ================================================================= */
        String mockErrorCode = "0x000000D1 (DRIVER_IRQL_NOT_LESS_OR_EQUAL)";
        String mockCauserFile = "nvlddmkm.sys (NVIDIA 그래픽 카드 드라이버)";

        // 3. 진짜 Gemini AI 분석 엔진 호출!
        String aiResult = dumpAnalysisService.analyzeDumpContent(mockErrorCode, mockCauserFile);

        // 4. 최종 결과 반환
        response.put("success", true);
        response.put("message", "덤프 파일 분석이 완료되었습니다!");
        response.put("fileName", originalFilename);
        response.put("analysisResult", aiResult); // 여기에 진짜 구글 Gemini가 준 답변이 담깁니다.

        return ResponseEntity.ok(response);
    }
}