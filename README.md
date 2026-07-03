# Blue Rescue: Windows 커널 덤프 분석 시스템

`Blue Rescue`는 Windows 운영체제에서 블루스크린 발생 시 생성되는 덤프 파일(`.dmp`)을 AI(Google Gemini)를 통해 정밀 분석하고, 사용자에게 명확한 해결책을 제시하는 시스템입니다.

## 🚀 주요 특징

* **보안 우선 설계:** API 키를 직접 코드에 노출하지 않고 환경 변수를 통해 안전하게 관리합니다.
* **사용자 중심 UI/UX:** 다크 모드 기반의 세련된 대시보드와 드래그 앤 드롭 업로드 기능을 제공합니다.
* **오프라인 개발 모드(예정):** 외부 API 호출 없이 개발/테스트가 가능한 목업(Mock) 환경을 지원하여 효율성을 높였습니다.
* **직관적인 가이드:** 덤프 파일이 어디에 위치하는지 즉시 확인할 수 있는 경로 가이드를 제공합니다.

---

## 🛠️ 기술 스택

* **Backend:** Spring Boot, Java 17, RestTemplate
* **Frontend:** React.js, CSS3 (Glassmorphism)
* **AI Engine:** Google Gemini (Generative AI)

---

## 🔐 보안 및 설정 (중요)

본 프로젝트는 API 보안을 위해 **환경 변수**를 사용합니다. 실행 전 반드시 아래 설정을 완료해야 합니다.

1. **API 키 설정:**
* 윈도우 시스템 환경 변수에 `GEMINI_API_KEY`를 등록합니다.
* 변수 값에 본인의 구글 Gemini API 키를 입력합니다.


2. **설정 확인:**
* `application.properties` 파일에서 `${GEMINI_API_KEY:}` 설정을 통해 자동으로 키를 읽어옵니다.



---

## 📂 프로젝트 구조

```text
blueRescue/
├── .gitignore                  # 통합 Git 제외 규칙 (IntelliJ, node_modules, build 차단)
├── blue-rescue-front/          # React 프론트엔드 애플리케이션
│   ├── src/
│   │   ├── App.js              # 메인 대시보드 UI 및 사용자 동선 로직
│   │   ├── App.css             # 반응형 레이아웃 스타일시트
│   │   ├── index.js            # React 엔트리 포인트
│   │   └── ...
│   └── package.json            # 프론트엔드 의존성 관리
│ 
└── blue-rescue-backend/        # Spring Boot 백엔드 애플리케이션
    ├── src/main/java/com/blueRescue/blueRescue/
    │   ├── BlueRescueApplication.java  # 메인 애플리케이션 실행 클래스
    │   ├── controller/
    │   │   └── DumpAnalysisController.java  # 파일 업로드 API 컨트롤러
    │   └── service/
    │       └── DumpAnalysisService.java     # Gemini AI 분석 핵심 로직
    └── src/main/resources/
        └── application.properties       # API 자격증명 및 서버 설정
```

---

## 💡 현재 진행 단계

* [x] 프로젝트 초기 설정 및 보안 아키텍처 구축
* [x] AI 분석 프롬프트 고도화 및 시스템 안정성 확보
* [x] 프론트엔드 UI 디자인 및 로고 에셋 적용
* [ ] Mock 서비스 스위칭 로직 구현 (현재 작업 예정)

---

## 📝 기여 방법

본 프로젝트는 지속적으로 발전하고 있습니다. 이슈나 제안 사항이 있다면 언제든 알려주세요!

---
