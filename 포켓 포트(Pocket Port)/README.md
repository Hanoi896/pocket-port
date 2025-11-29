# 포켓 포트 (Pocket Port) 🚢

작은 항구를 은하계 무역의 중심지로 키워나가는 안드로이드 방치형 경영 게임!

Jetpack Compose로 제작된 모바일 항구 관리 시뮬레이션 게임입니다.

---

## 🎮 게임 특징

### 핵심 시스템
- **🏭 자동 생산**: 공장에서 컨테이너를 자동으로 생산
- **🚢 무역 시스템**: 7개의 무역 루트 (로컬 시장 → 달 기지까지!)
- **⬆️ 업그레이드**: 공장과 운송 수단 강화
- **🎯 미니게임**: 60초 퍼즐로 추가 골드 획득
- **💎 유물 시스템**: 영구 버프 아이템 수집
- **👑 VIP 구독**: 모든 작업 속도 1.5배 부스트

### 시각 효과
- **벡터 그래픽**: 선명한 배, 공장, 크레인 스프라이트
- **파티클 효과**: 공장 연기, 배의 물결 효과
- **부드러운 애니메이션**: 실시간 움직임 표현

---

## 📥 다운로드 및 설치

### 방법 1: APK 다운로드 (추천)
1. 상단 [Actions](../../actions) 탭 클릭
2. 가장 최근의 성공한 빌드 선택
3. 하단 **Artifacts** 섹션에서 `pocket-port-debug` 다운로드
4. ZIP 압축 해제 후 `app-debug.apk`를 안드로이드 기기로 전송
5. APK 설치 및 실행!

> ⚠️ **주의**: 출처를 알 수 없는 앱 설치를 허용해야 합니다.

### 방법 2: 소스 코드로 빌드
Android Studio 또는 JDK 17+ 필요:
```bash
./gradlew assembleDebug
```
빌드된 APK 위치: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 게임 플레이 가이드

### 🎯 목표
작은 항구를 **은하계 무역 허브**로 성장시키세요!

### 🔄 게임 루프
1. **생산** 🏭: 공장이 자동으로 컨테이너 생산
2. **운송** 🚛: 트럭과 크레인이 컨테이너를 배에 적재
3. **무역** 🚢: 배가 목적지로 출항하여 골드 획득
4. **업그레이드** ⬆️: 골드로 시설 강화
5. **확장** 🌍: 레벨업으로 새로운 무역 루트 해금

### 🎮 조작 방법
- **Upgrade Factory**: 생산 속도 증가
- **Upgrade Ship**: 배 이동 속도 및 적재량 증가
- **Mini Game**: 퍼즐 게임으로 보너스 골드 획득
- **Artifacts**: 수집한 유물 확인
- **VIP**: VIP 패스 활성화 (1.5배 속도 부스트)

### 💡 팁
- 초반에는 **공장 업그레이드**에 집중하여 생산량 확보
- 배가 느리다면 **배 업그레이드** 또는 **VIP** 활용
- 레벨이 오르면 자동으로 더 먼 무역 루트 해금
- 미니게임으로 빠르게 골드 획득 가능

---

## 🛠️ 기술 스택

### 언어 & 프레임워크
- **Kotlin** - 주 개발 언어
- **Jetpack Compose** - 선언형 UI 프레임워크
- **Coroutines** - 비동기 처리

### 아키텍처 & 라이브러리
- **MVVM 패턴** - 관심사 분리
- **Room Database** - 로컬 데이터 저장
- **StateFlow** - 반응형 상태 관리
- **Gson** - JSON 직렬화

### 주요 시스템
```
📦 Domain Layer
├── GameManager (게임 루프 관리)
├── ProductionSystem (생산 로직)
├── TransportSystem (운송 로직)
├── TradeSystem (무역 로직)
├── UpgradeSystem (업그레이드 로직)
├── PuzzleManager (미니게임)
└── ArtifactSystem (유물 관리)

🎨 UI Layer
├── GameScreen (메인 화면)
├── PortCanvas (항구 시각화)
├── PuzzleScreen (미니게임 화면)
└── ArtifactScreen (유물 화면)

💾 Data Layer
├── Room Database (영구 저장)
└── PortEntity (게임 상태)
```

---

## 📊 게임 콘텐츠

### 🌍 무역 루트 (7개)
1. **Local Market** (로컬 시장) - 거리: 100
2. **City Center** (도심) - 거리: 500
3. **Neighbor Port** (인근 항구) - 거리: 1,200
4. **Capital City** (수도) - 거리: 3,000
5. **Overseas Empire** (해외 제국) - 거리: 8,000
6. **Arctic Station** (북극 기지) - 거리: 15,000
7. **Lunar Base** (달 기지) - 거리: 50,000

### 💎 유물 (8개)
- **Golden Gear** (황금 톱니바퀴): 생산 속도 +20%
- **Iron Anchor** (철 닻): 이동 속도 +10%
- **Ancient Chest** (고대 상자): 저장 용량 +50%
- **Lucky Coin** (행운의 동전): 무역 보상 +30%
- **Premium Oil** (프리미엄 오일): 생산 속도 +50%
- **Wind Sail** (바람 돛): 이동 속도 +30%
- **Mystic Gem** (신비한 보석): 무역 보상 +100%
- **Auto Bot** (자동 로봇): 생산 속도 +100%

---

## 🚀 개발 로드맵

### ✅ 완료된 기능 (Phase 1-3)
- [x] 핵심 게임 루프 (생산, 운송, 무역)
- [x] 업그레이드 시스템
- [x] Room DB 저장/불러오기
- [x] 미니게임 (퍼즐)
- [x] 유물 시스템
- [x] VIP 구독 시스템 (Mock)
- [x] 벡터 스프라이트
- [x] 파티클 효과

### 🔜 향후 계획
- [ ] 실제 Admob 광고 연동
- [ ] Google Play Billing (실제 결제)
- [ ] 사운드 효과 및 BGM
- [ ] 더 많은 애니메이션
- [ ] 다국어 지원 (영어, 일본어)
- [ ] 업적 시스템
- [ ] 리더보드

---

## 📄 라이선스

MIT License - 자유롭게 사용 및 수정 가능합니다!

---

## 👨‍💻 개발 정보

- **개발 기간**: 2025년 11월
- **개발 도구**: Kotlin, Jetpack Compose
- **최소 안드로이드 버전**: API 24 (Android 7.0)
- **타겟 안드로이드 버전**: API 34 (Android 14)

---

<div align="center">

**Made with ❤️ using Jetpack Compose**

즐거운 항해 되세요! 🚢💨

</div>
