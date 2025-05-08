<img src="assets/images/linkup_banner.png" alt="linkup_banner"/>
<div align="center">

<br>

🤼‍♂️ LinkUP
<br><br>모임이라는 소속에 구애받지 않고 언제든 원할 때 신청해 <br>여러 사람들과 운동을 즐길 수 있는 플랫폼

</div>

<br>

---

## 📚 목차

1. [✨ 한 줄 소개](#1--한-줄-소개)  
2. [🏆 팀 소개](#2--팀-소개)  
3. [📘 프로젝트 기획서](#3--프로젝트-기획서)  
4. [🛠️ 기술 스택](#4--기술-스택)
5. [📁 파일 구조](#5--파일-구조)  
6. [📊 WBS (작업 분배 및 일정)](#6--wbs-작업-분배-및-일정)  
7. [📄 요구사항 정의](#7--요구사항-정의)  
8. [🔄 플로우 차트](#8--플로우-차트--uml)  
9. [📦 DDD](#9--ddd)  
10. [📌 ERD](#10--erd--테이블-정의서)    
11. [⚙️ 시스템 아키텍쳐](#11--시스템-아키텍쳐)  
12. [📒 테스트 케이스 정의서](#12--테스트-케이스-정의서)  
13. [🧪 테스트 케이스 상세](#13--테스트-케이스-상세)  
14. [🫂 팀원 회고](#14--팀원-회고)

<br>

---

## 1. ✨ 한 줄 소개

> **LinkUp**은 사용자가 자신의 **위치와 시간에 맞춰 운동 모임에 참여하거나 직접 개설할 수 있는 운동 모임 플랫폼**입니다.
> 
> 실시간 참여 인원 확인과 장소 예약 기능까지 제공하여,
> **간편한 모임 개설과 참여, 그리고 자연스러운 참여 유도 시스템**을 통해 운동을 더 쉽고 꾸준히 이어갈 수 있도록 지원합니다.

<br>

---

## 2. 🏆 팀 소개

<br>

|                     곽진웅                     |                   김여진                   |                      박성용                      |                   박준서                   |                   장건희                   |                  최지혜                  |
| :-----------------------------------------: | :-------------------------------------: | :-------------------------------------------: | :-------------------------------------: | :-------------------------------------: | :-----------------------------------: |
| [mijuckboon](https://github.com/mijuckboon) | [meowdule](https://github.com/meowdule) | [develop-psy](https://github.com/develup-psy) | [Joonspar](https://github.com/Joonspar) | [jang9465](https://github.com/jang9465) | [jihye25](https://github.com/jihye25) |


<br>

---

## 3. 📘 프로젝트 기획서

<details>
<summary>📌 1. 프로젝트 주제</summary>

<br>

LinkUp은 모임이라는 소속에 구애받지 않고 언제든 원할 때 신청해 여러 사람들과 운동을 즐길 수 있는 플랫폼입니다.

</details>

<br>

<details>
<summary>📌 2. 프로젝트 소개</summary>

<br>

**LinkUp**은 사용자가 자신의 **위치와 시간에 맞춰 운동 모임에 참여하거나 직접 개설할 수 있는 운동 모임 플랫폼**입니다.  
실시간 참여 인원 확인과 장소 예약 기능까지 제공하여, **간편한 모임 개설과 참여, 그리고 자연스러운 참여 유도 시스템**을 통해 운동을 더 쉽고 꾸준히 이어갈 수 있도록 지원합니다.

<br>

</details>

<br>

<details>
<summary>📌 3. 프로젝트 배경 및 필요성</summary>

### 🔎 배경

현대 사회에서 운동은 단순한 건강 관리를 넘어, 사람들과의 교류와 소속감을 느낄 수 있는 중요한 여가 활동으로 자리 잡고 있습니다. 특히 팀 단위 또는 파트너가 필요한 종목들(풋살, 농구, 배드민턴, 탁구 등)의 경우, 함께할 사람을 찾는 일이 운동의 지속성과 몰입도를 결정짓는 주요 요소가 됩니다.

그러나 현재의 운동 참여 방식은 다음과 같은 문제점을 안고 있습니다.

1. **폐쇄적인 동호회 문화**
    - 기존 동호회는 고정 멤버 중심으로 운영되어, 신규 참여자가 심리적 부담을 느끼는 경우가 많습니다.
    - 특히 운동 초보자에게는 높은 실력 수준이나 경쟁적인 분위기가 장벽으로 작용합니다.
2. **비효율적인 시설 예약 방식**
    - 운동 시설 예약은 아직도 전화 또는 오프라인 방식에 의존하는 경우가 많아 불편함이 큽니다.
    - 여러 업체의 정보를 한 곳에서 비교하거나 예약 상태를 실시간으로 확인하기 어렵습니다.
3. **개인 맞춤형 모임 매칭 부재**
    - 기존 서비스는 성별, 연령, 실력 수준 등 사용자의 조건을 고려하지 않은 일률적인 방식으로 매칭이 이루어지고 있습니다.
    - 이는 만족도 낮은 참여로 이어지고, 장기적인 운동 습관 형성에도 부정적인 영향을 줍니다.

더불어 **1인 가구 증가, 유연근무제 도입, 불규칙한 생활 패턴** 등으로 인해

정기적인 모임 참여가 어려운 사용자들이 늘어나고 있으며,

이들은 **즉흥적으로 참여할 수 있는 ‘번개 모임’ 형태의 운동 참여 방식**을 선호하고 있습니다.


### 🧠 필요성

현대인의 라이프스타일 변화와 운동 참여 방식의 다양화는, 기존의 운동 플랫폼으로는 더 이상 사용자들의 수요를 충분히 충족시키기 어렵다는 현실을 보여주고 있습니다. 아래와 같은 사회적 흐름은 본 서비스의 필요성을 뒷받침합니다.

1. **개인화된 라이프스타일과 불규칙한 일상**
    - 1인 가구의 증가, 유연근무제 확산, 비정기적인 스케줄 등으로 인해 고정된 시간에 정기적으로 모임에 참여하기 어려운 사람들이 많아지고 있습니다.
    - 이에 따라 당일 또는 주간 단위의 **즉흥적인 운동 모임에 대한 수요**가 커지고 있습니다.
2. **신뢰 기반 오프라인 커뮤니티의 부재**
    - 온라인에는 다양한 운동 커뮤니티가 존재하지만, 오프라인에서 실질적으로 함께 운동할 수 있는 **신뢰 기반의 모임 공간**은 매우 부족한 실정입니다.
    - 운동을 함께할 사람을 찾는 것 자체가 하나의 장벽이 되어 **운동 지속률 저하**로 이어지고 있습니다.
3. **모임 기반 운동의 효과성**
    - 혼자 하는 운동보다, 함께하는 운동은 꾸준함과 동기 부여 측면에서 훨씬 효과적입니다.
    - 사회적 연결을 통해 **신체 건강과 정서적 안정**을 동시에 도모할 수 있다는 점에서, 모임 중심 플랫폼의 가치는 더욱 커지고 있습니다.
4. **유휴 자원의 활용 필요**
    - 공공 체육시설, 소규모 체육관 등은 특정 시간대에 비효율적으로 운영되는 경우가 많습니다.
    - 본 서비스는 이와 같은 **유휴 공간을 운동 모임과 연결**함으로써, 지역 자원의 효율적 활용을 가능하게 합니다.
5. **건강한 참여 문화를 위한 시스템 필요**
    - 노쇼, 무단 취소, 매너 부족 등 기존 모임 플랫폼에서 발생하는 문제들을 해결하기 위해, 후기 및 평가 시스템을 넘어선 **매너온도 기반 신뢰 시스템**이 요구됩니다.

따라서 본 프로젝트는,

**위치 기반 번개 운동 모임 매칭**, **조건 필터링**, **실시간 인원 확인**, **장소 예약 및 결제 통합**,

그리고 **신뢰 기반 평가 시스템**까지 통합하여,

**현실적인 참여 환경과 지속 가능한 운동 커뮤니티**를 제공합니다.

이는 단순한 운동 플랫폼을 넘어,

**사용자의 라이프스타일에 맞춘 여가 플랫폼이자, 지역 커뮤니티와 체육 자원의 연결 허브**로서의 가능성을 지닙니다.

</details>

<br>

<details>
<summary>📌 4. 타 서비스와의 차별점</summary>

<br>

| 항목 | 본 서비스의 차별화 요소 |
| --- | --- |
| 다양한 운동 종목 | 팀 기반 운동 종목 전반 지원 |
| 모임 개설 구조 | 사용자 주도형 번개 운동 모임 |
| 참여 방식 | 불규칙 일정에도 대응 가능한 즉흥성 |
| 신뢰 시스템 | 매너온도 기반 평가 및 필터링 |
| 결제 구조 | 포인트 기반 자동 정산 및 환불 |
| 장소 예약 | 사업자 연동 예약 시스템 |
| 진입 장벽 해소 | 초보자 환영 구조 및 실력 태그 |

<br>

<br>

본 서비스는 기존 운동 모임 플랫폼과 비교하여 다음과 같은 차별화된 요소를 보유하고 있습니다.

1. **다양한 종목 지원과 즉흥성 강화**
    
    - 기존 플랫폼은 풋살, 배드민턴, 러닝 등 특정 종목에 집중되어 있으나, 본 서비스는 **풋살, 농구, 배드민턴, 테니스, 탁구, 스크린골프, 등산 등 다양한 팀 기반 운동 종목**을 포괄적으로 지원합니다.
    - 특히, '오늘 저녁', '내일 아침'과 같은 **단기성 운동 모임**을 쉽게 생성할 수 있어, **불규칙한 일정을 가진 사용자도 즉흥적으로 참여**할 수 있습니다.
    - 실시간 참여 인원 확인, 매칭 조건 자동 검증, 신청 시 포인트 선결제 구조 등을 통해 **모임 개설부터 참여까지 최소한의 절차로 운동이 성사**됩니다.
2. **사용자 주도형 자율 모임 생성**
    
    - 대부분의 기존 운동 플랫폼은 **관리자 또는 특정 단체 주도의 정기 모임**에 초점이 맞춰져 있으나,
        
        본 서비스는 **일반 사용자가 직접 번개 모임을 개설하고 조건을 설정**할 수 있도록 설계되어 있습니다.
        
    - 성별, 연령대, 실력 수준, 매너온도 등의 조건 필터링을 통해 **자신에게 잘 맞는 참가자를 직접 모집**할 수 있고,
        
        신청 승인 여부도 **주최자가 직접 관리**할 수 있습니다.
        
3. **매너온도 시스템을 통한 신뢰 기반 매칭**
    
    - 후기, 출석률, 노쇼 여부 등을 정량화한 **매너온도 시스템**을 도입하여, 참여자의 신뢰도를 시각적으로 제공합니다.
    - 이를 통해 신뢰성 높은 사용자 중심의 모임 문화를 조성하고 **재참여율을 높이는 선순환 구조**를 구축합니다.
4. **포인트 기반 선결제 및 정산 시스템**
    
    - 기존 플랫폼은 비용 정산이나 결제 처리가 외부 링크 또는 별도 방식으로 이루어지는 경우가 많습니다.
        
        본 서비스는 **모임 생성 시 주최자가 포인트 선결제를 하고**,
        
        참가자가 신청과 함께 **1/N 금액 자동 차감**,
        
        이후 **인원 증가 시 차액 자동 환불** 등의 **통합형 비용 관리 시스템**을 제공합니다.
        
    - 이는 **공정하고 투명한 비용 구조를 제공**함으로써, 사용자의 신뢰와 편의성을 동시에 만족시킵니다.
        
    - 마일리지 적립 기능도 함께 제공되어 사용자 리텐션을 유도합니다.
        
5. **사업자 연동을 통한 장소 및 장비 예약 통합**
    
    - 사업자는 장소 및 장비 정보를 등록하고 예약 가능 시간대를 관리할 수 있으며, 사용자는 플랫폼 내에서 **운동 모임과 장소 대여를 함께 처리**할 수 있습니다.
    - 이를 통해 **시설 이용률을 높이고, 예약 과정을 간소화**할 수 있습니다.

이와 같은 차별 요소들은 단순한 운동 매칭을 넘어서, **사용자 경험 중심의 통합 플랫폼**으로서 본 서비스의 경쟁력을 높여주는 핵심이 됩니다.

</details>

<br>

<details>
<summary>📌 5. 서비스 대상</summary>

<br>

1. **혼자 운동하는 데 어려움을 겪는 직장인**
    - 바쁜 일정과 불규칙한 생활로 인해 정기적인 운동 모임에 참여하기 어려운 직장인에게, 당일 또는 주간 단위로 참여 가능한 유연한 번개 운동 모임을 제공합니다.
2. **운동을 함께할 친구를 찾고 있는 사용자**
    - 동아리나 고정된 운동 모임이 부담스러운 사람들에게 자유롭게 모임을 만들고, 또래와 함께 운동할 수 있는 개방형 플랫폼을 제공합니다.
3. **지역 기반 커뮤니티 활동을 원하는 사용자**
    - 지역 주민과의 교류를 원하는 사람들에게, 운동이라는 공통 관심사를 바탕으로 오프라인 네트워크를 형성할 수 있는 기회를 제공합니다.
4. **즉흥적 운동 참여를 선호하는 활동가 및 운동 매니아**
    - 정해진 루틴이 아닌, 그날그날 운동하고 싶은 사용자에게 실시간으로 매칭 가능한 번개 모임 시스템을 제공합니다.
5. **운동을 처음 시작하는 입문자 및 1인 가구, 이주민**
    - 초보자도 부담 없이 참여할 수 있는 모임과 매너 기반 매칭 시스템을 통해, 운동 입문자와 사회적 연결이 필요한 사용자에게 환영받는 구조를 갖추고 있습니다.

</details>

<br>

<details>
<summary>📌 6. 기대효과</summary>

<br>

1. **사용자 측면 : 운동 참여의 지속성과 접근성 향상**

	- **시간과 장소 제약을 최소화한 유연한 참여 환경**을 제공함으로써,
	    
	    바쁜 현대인도 꾸준한 운동 습관을 형성할 수 있습니다.
	    
	- 초보자도 **매너 기반의 신뢰 시스템과 맞춤형 필터링**을 통해 부담 없이 운동 모임에 참여할 수 있어,
	    
	    운동 진입 장벽이 낮아지고 **보다 다양한 사용자가 스포츠 활동에 접근**할 수 있게 됩니다.

2. **사회적 측면: 지역 커뮤니티와 연결된 건강한 여가 문화 조성**

	- 운동을 매개로 한 지역 중심의 만남은 **오프라인 커뮤니티 활성화**로 이어지며,
	    
	    **고립감 해소, 세대 간 교류, 건강한 사회적 연결**을 유도합니다.
	    
	- 이는 특히 **1인 가구, 이주민, 사회 초년생** 등에게 실질적인 **사회적 소속감과 심리적 안정**을 제공할 수 있습니다.

3. **플랫폼 측면: 신뢰 기반의 자율 생태계 구축**

	- 매너온도 시스템을 통한 **정량적 신뢰도 평가**는
	    
	    건강한 사용자 행동을 유도하고, **재참여율과 플랫폼의 질적 수준을 향상**시킵니다.
	    
	- 사용자 주도의 모임 개설과 참여 구조는 **플랫폼 운영 부담을 줄이는 동시에 확장성과 자생력을 확보**할 수 있는 기반이 됩니다.

4. **지역경제 측면: 유휴 체육시설의 활용도 제고 및 수익 창출**

	- 플랫폼을 통해 공공 및 민간 체육시설의 **공백 시간대 예약과 활용을 유도**함으로써
	    
	    **유휴 자원의 효율적 운영과 지역 기반 수익 모델**이 가능해집니다.
	    
	- 특히 소규모 체육시설, 레저업체, 개인 사업자 등에게는
	    
	    **홍보·예약·정산까지 연동된 관리 편의성**을 제공하여
	    
	    **지역 소상공인의 수익 창출에도 긍정적인 효과**를 기대할 수 있습니다.

</details>

<br>

<details>
<summary>📌 7. 향후 확장 방향</summary>

<br>

1. **개인 운동 콘텐츠 및 클래스 기능 추가**
    - 필라테스, 요가, 헬스 등 **1:1 또는 소규모 개인 운동 중심 콘텐츠**를 도입
    - 강사 또는 트레이너와의 **예약 기반 클래스 연동 시스템** 제공
2. **운동 챌린지 및 랭킹 시스템 도입**
    - 사용자 참여를 유도하는 **걸음 수, 소모 칼로리, 운동 횟수 기반의 챌린지 기능**
    - 지역별/종목별 랭킹 시스템을 통해 **게임화(Gamification) 요소** 강화
3. **전문가 매칭 및 실력 분석 기능**
    - 트레이너 및 운동 지도자와의 **멘토링 연계 서비스**
    - 실력 기반의 데이터 분석(모임 후기, 매너온도, 참여 이력 등)을 통한 **AI 기반 매칭 고도화**
4. **실시간 대기자-모임 자동 연결 시스템**
    - 마감된 모임에 대기한 사용자가 **모임 취소 발생 시 자동으로 참여 확정**
    - 참여율을 높이고, **참여자-공간의 낭비를 줄이는 구조**
5. **날씨 및 위치 기반 추천 알고리즘 고도화**
    - 현재 위치, 기온, 강수량 등을 반영한 **실내/실외 종목 추천 시스템**
    - 특정 지역의 실시간 인기 모임 정보 제공
6. 장소 및 장비 예약 세분화
    - 장소 예약을 넘어 대여 가능한 장비에 대한 예약 시스템 연동
    - 장비의 재고 파악과 대여 가능 상황을 통한 간편한 예약 시스템 제공

</details>

<br>

<details>
<summary>📌 8. 주요 기능</summary>

<br>

### 1) 회원 관리 및 인증  
- 이메일, 소셜 로그인  
- 프로필, 계정 복구, 사업자 인증 등  

### 2) 운동 모임 개설 및 참여  
- 조건 기반 모임 생성 및 승인 관리  
- 실시간 인원 확인  

### 3) 포인트 기반 결제  
- 선결제, 자동 1/N 정산, 환불 처리  
- 포인트 충전 및 마일리지  

### 4) 매너온도 시스템  
- 후기, 노쇼 이력 기반 신뢰 시각화  
- 베스트 플레이어 선정  

### 5) 장소 예약 연동  
- 위치 기반 예약  
- 사업자용 등록 및 외부일정 연동  

### 6) 커뮤니티 및 친구  
- 자유 게시판, 댓글, 좋아요, 신고  
- 친구 추가, 활동 이력 조회  

### 7) 실시간 알림  
- 모임 승인/취소, 친구 알림 등  

### 8) 관리자 기능  
- 회원 제재, 신고 처리, 후기 이의신청  
- 사업자 승인, 공지사항 관리

</details>


<br>

<br>

---

## 4. 🛠️ 기술 스택

<div>
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white" alt="">
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" alt="">
<img src="https://img.shields.io/badge/JPA-6DB33F?style=flat&logo=Hibernate&logoColor=white" alt="">
<img src="https://img.shields.io/badge/MyBatis-000000?style=flat&logo=MyBatis&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white" alt="">
<img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON%20web%20tokens&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Junit5-25A162?style=flat&logo=JUnit5&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=white" alt="">
</div>

### 📦 DataBase
<div>
<img src="https://img.shields.io/badge/MariaDB-003545?style=flat&logo=MariaDB&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat&logo=AmazonS3&logoColor=white" alt="">
</div>

### 🛠️ Tool
<div>
<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white" alt=""> 
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=Postman&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=Figma&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Miro-050038?style=flat&logo=Miro&logoColor=white" alt="">
</div>


---

## 5. 📁 파일 구조


```
📦 LearningCrew_LinkUp
├── 📁 assets                       # 프로젝트 부속 자료
│   ├── 📁 api-docs                 # API 명세 관련 문서
│   ├── 📁 ddl                      # DDL 스크립트 (테이블 생성 등)
│   │   └── 📄 linkup_ddL.sql
│   ├── 📁 images                   # 이미지 자료 모음
│   │   └── 📄 requirements         # UML 다이어그램, 요구사항 명세서 등
│   └── 📁 test_cases               # 테스트 케이스 카테고리별 정리
│       └── 📁 ...
│
├── 📁 linkup
│   ├── 📁 ...
│
├── 📁 linkup_EurekaServer
│   ├── 📁 ...
│
├── 📁 linkup_Gateway
│   ├── 📁 ...
│
├── 📁 linkupUser
│   ├── 📁 ...
│
└── 📄 README.md                     # 프로젝트 소개 및 사용법

```


<details>
<summary>📂 linkup </summary>

```
📦 LinkUp.src.main
├── 📁 java.com.learningcrew.linkup
│   ├── 📁 common
│   │   ├── 📁 util
│   │   ├── 📁 dto
│   │   └── 📁 service
│   │   
│   ├── 📁 config
│   │   └── 📄 ...
│   │   
│   ├── 📁 exception
│   │   └── 📄 ...
│   │   
│   ├── 📁 기능별 파일
│   │   ├── 📁 command
│   │   │   ├── 📁 application
│   │   │   │   ├── 📁 controller
│   │   │   │   ├── 📁 dto
│   │   │   │   └── 📁 service
│   │   │   ├── 📁 domain
│   │   │   │   ├── 📁 aggregate
│   │   │   │   └── 📁 repository
│   │   │   └── 📁 infrastructure
│   │   │       └── 📁 repository
│   │   │
│   │   └── 📁 query
│   │       ├── 📁 controller
│   │       │   └── 📄 ...
│   │       ├── 📁 dto
│   │       │   ├── 📁 request
│   │       │   └── 📁 response
│   │       ├── 📁 mapper
│   │       │   └── 📄 ...
│   │       └── 📁 service
│   │           └── 📄 ...
│   └── 📄 Application.java
│
└── 📁 resources
    ├── 📁 mappers.기능별
    │   └── 📄 기능별_Mapper.xml  
    └── 📄 application.yml  

```


</details>


<br>

<details>
<summary>📂 linkup_EurekaServer </summary>

```
📁 linkup_EurekaServer
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com.learningcrew.linkup
│       │       └── 📄 LinkupEurekaServerApplication.java
│       └── 📁 resources
│           └── 📄 application.yaml



```


</details>


<br>

<details>
<summary>📂 linkup_Gateway </summary>

```
📁 linkup_Gateway
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com.learningcrew.linkup
│       │       ├── 📄 LinkupGatewayApplication.java
│       │       ├── 📁 exception
│       │       └── 📁 gateway
│       │           ├── 📁 filter
│       │           └── 📁 jwt
│       └── 📁 resources
│           └── 📄 application.yaml



```


</details>


<br>

<details>
<summary>📂 linkupUser </summary>

```
📁 linkupUser
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com.learningcrew.linkupuser
│       │       ├── 📄 LinkupUserApplication.java
│       │       ├── 📁 client
│       │       ├── 📁 command
│       │       │   └── 📁 application
│       │       │       ├── 📁 controller
│       │       │       ├── 📁 dto
│       │       │       └── 📁 service
│       │       ├── 📁 domain
│       │       │   ├── 📁 aggregate
│       │       │   └── 📁 repository
│       │       ├── 📁 infrastructure
│       │       │   └── 📁 repository
│       │       └── 📁 query
│       │           ├── 📁 controller
│       │           ├── 📁 dto
│       │           │   ├── 📁 request
│       │           │   └── 📁 response
│       │           ├── 📁 mapper
│       │           └── 📁 service
│       └── 📁 resources
│           ├── 📄 application.yml
│           └── 📁 mappers
│               └── 📄 사용자_Mapper.xml


```


</details>


<br>



---

## 6. 📊 WBS (작업 분배 및 일정)


🔗 [WBS 명세서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS5z7lnFVoKhrHU0zT55KpMlmla72tCCbzkwDi_7PyI23dByzC5GMS2v7jT-7YjDzr9LlZDPXG-xev4/pubhtml?gid=403982901&single=true)


<img src="assets/images/wbs.jpg" alt="wbs" width="550"/>

<br>

---

## 7. 📄 요구사항 정의

🔗 [요구사항 명세서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vR5o5viDNitHaRwGwl4o1biS6vLC52SPAyREVK2vDS9y13eXPo8N7CumbLhWDeejayHAh0Ji5Cmf2tf/pubhtml?gid=1583571973&single=true)


<img src="assets/images/requirement_statement.jpg" alt="requirement_statement" width="550"/>

<br>

---

## 8. 🔄 플로우 차트 & UML

### 사용자 흐름 시각화  
<img src="assets/images/flowchart.png" alt="플로우차트" width="550"/>

<br>

### 유스케이스 다이어그램  
<img src="assets/images/UML.png" alt="UML" width="700"/> 

<br>

---

## 9. 📦 DDD

> 도메인 주도 설계(Domain-Driven Design) 기반의 구조와 설계 원칙 설명  
(예: 도메인 계층 구성, 집합체, 엔티티, 서비스 분리 등)

<br>

<img src="assets/images/ddd-step8.jpg" alt="Step8 Policy" width="700"/>

🔗 [DDD 전체 보기](https://miro.com/app/board/uXjVIIWm93c=/?share_link_id=312811606423)


<br>

<details>
<summary>📌 단계별 DDD</summary>

<details>
<summary>🔹 Step 1. Domain Event 정의</summary>

- 비즈니스 도메인 내에 발생하는 모든 이벤트를 과거형으로 기술(조회 기능 X)  
- 이벤트는 Actor가 Action을 해서 발생한 결과  
- 문장 형태: “XXX 됨”  
- 상의 없이 각자 Event를 무작위로 작성  
- 중복 제거 및 시간순 정렬  
- 이슈/관심 사항은 빨간 포스트잇으로 표시  

<img src="assets/images/ddd-step1.jpg" alt="Step1 Domain Event" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 2. External System 정의</summary>

- 결제 시스템, 이메일 발송, SMS 등 외부 시스템 정의  
- 명사형으로 작성  

<img src="assets/images/ddd-step2.jpg" alt="Step2 External System" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 3. Command 정의</summary>

- 각 Domain Event를 유발하는 명령  
- 현재형 & 명령형으로 작성  

<img src="assets/images/ddd-step3.jpg" alt="Step3 Command" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 4. HotSpot 정의</summary>

- 궁금하거나 결정되지 않은 사항을 정리  

<img src="assets/images/ddd-step4.jpg" alt="Step4 HotSpot" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 5. Actor 정의</summary>

- 도메인 이벤트를 유발하는 주체  
- 예: 사용자, 시스템, 외부 서비스 등  

<img src="assets/images/ddd-step5.jpg" alt="Step5 Actor" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 6. 애그리거트 정의</summary>

- 하나의 단위로 간주되는 도메인 객체 집합  
- 관계와 제약을 관리하는 중심 단위  

<img src="assets/images/ddd-step6.jpg" alt="Step6 Aggregate" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 7. Bounded Context 정의</summary>

- 관련 애그리거트를 하나의 컨텍스트로 묶기  

<img src="assets/images/ddd-step7.jpg" alt="Step7 Bounded Context" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 8. Policy 정의</summary>

- 도메인 내 규칙 정의  
- **액터별 정책 예시**  
  - 사용자: 로그인 후 주문 가능  
  - 관리자: 모든 주문 조회 및 수정  
- **애그리거트별 정책 예시**  
  - 주문: 취소 전 수정만 허용  
  - 상품: 품절 시 주문 불가  
- **이벤트별 정책 예시**  
  - 주문 생성: 3일 이내 결제  
  - 주문 취소: 상태 변경 + 재고 복구  

<img src="assets/images/ddd-step8.png" alt="Step8 Policy" width="700"/>

<br>

</details>

<details>
<summary>🔹 Step 9. Context Mapping</summary>

- Bounded Context 간 상호작용 & 경계 정의  
- 도메인 설계의 명확성 확보  

<img src="assets/images/ddd-step9.png" alt="Step9 Context Mapping" width="700"/>

<br>

</details>

<br>



</details>

<br>

<br>

---

## 10. 📌 ERD & 테이블 정의서

### 논리 ERD  

<img src="assets/images/logical_ERD.png" alt="logical_ERD"/>

<br>

### 물리 ERD  

<img src="assets/images/physical_ERD.png" alt="physical_ERD"/>

<br>

### 테이블 정의서

🔗 [테이블 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vRunQplhnLkZI3XVY3Phoj7wQVirdkzOMEnkMLMfItG7qATPKMGKVSAPNOodzxhBgE6C86Ax5my7o9p/pubhtml?gid=763788645&single=true)

<details>
<summary>🔹 기초 테이블 정의서 </summary>

- 상태 테이블
- 권한 테이블
- 운동 종목 테이블

<img src="assets/images/table_definition_init.jpg" alt="table_definition_init" width="700"/>

<br>

</details>

<details>
<summary>🔹 사용자 테이블 정의서 </summary>

- 사용자 테이블
- 회원 테이블
- 친구 관계 테이블
- 선호 운동 테이블

<img src="assets/images/table_definition_user_1.jpg" alt="table_definition_user_1" width="700"/>
<img src="assets/images/table_definition_user_2.jpg" alt="table_definition_user_2" width="700"/>

<br>

</details>

<details>
<summary>🔹 포인트 테이블 정의서 </summary>

- 계좌 테이블
- 포인트 거래 내역 테이블

<img src="assets/images/table_definition_point.jpg" alt="table_definition_point" width="700"/>

<br>

</details>

<details>
<summary>🔹 장소 테이블 정의서 </summary>

- 사업자 테이블
- 장소 테이블
- 장소 사진 테이블
- 장소 즐겨찾기 테이블
- 장소 예약 테이블
- 장소 후기 테이블

<img src="assets/images/table_definition_place_1.jpg" alt="table_definition_place_1" width="700"/>
<img src="assets/images/table_definition_place_2.jpg" alt="table_definition_place_1" width="700"/>

<br>

</details>

<details>
<summary>🔹 모임 테이블 정의서 </summary>

- 모임 테이블
- 모임 참여이력 테이블
- 참가자 평가 테이블
- 베스트 플레이어 테이블
- 모임 찜 테이블

<img src="assets/images/table_definition_meeting_1.jpg" alt="table_definition_meeting_1" width="700"/>
<img src="assets/images/table_definition_meeting_2.jpg" alt="table_definition_meeting_2" width="700"/>

<br>

</details>

<details>
<summary>🔹 커뮤니티 테이블 정의서 </summary>

- 게시글 테이블
- 게시글 좋아요 테이블
- 게시글 사진 테이블
- 댓글 테이블
- 댓글 좋아요 테이블

<img src="assets/images/table_definition_community_1.jpg" alt="table_definition_community_1" width="700"/>
<img src="assets/images/table_definition_community_2.jpg" alt="table_definition_community_2" width="700"/>

<br>

</details>


<details>
<summary>🔹 알림 테이블 정의서 </summary>

- 알림 유형 테이블
- 알림 설정 테이블
- 도메인 타입 테이블
- 알림 테이블

<img src="assets/images/table_definition_notification.jpg" alt="table_definition_notification" width="700"/>

<br>

</details>

<details>
<summary>🔹 신고 및 제재 테이블 정의서 </summary>

- 신고 유형 테이블
- 신고 이력 테이블
- 사용자 제재 이력 테이블
- 이의제기 테이블
- 블랙리스트 테이블

<img src="assets/images/table_definition_report.jpg" alt="table_definition_report" width="700"/>
<img src="assets/images/table_definition_penalty.jpg" alt="table_definition_penalty" width="700"/>

<br>

</details>

<details>
<summary>🔹 토큰 테이블 정의서 </summary>

- 인증 토큰 테이블
- 리프레시 토큰 테이블

<img src="assets/images/table_definition_token.jpg" alt="table_definition_token" width="700"/>

<br>

</details>





<br>

<br>

---

## 11. ⚙️ 시스템 아키텍쳐

> 전체 시스템 구성 다이어그램  

<img src="assets/images/sysetem_architecture.jpg" alt="sysetem_architecture" width="700"/>

<br>

<img src="assets/images/gateway.png" alt="gateway" width="500"/>

<br>

---

## 12. 📒 REST API 설계 문서(Swagger 등)

<br>

>REST API 설계 문서

[swagger_api.pdf](assets/api-docs/Swagger_API.pdf)

<br>

<img src="assets/api-docs/swagger_api.jpg" alt="rest_api" width="700"/>

<br>

<br>

🔗 [테스트 케이스 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS5z7lnFVoKhrHU0zT55KpMlmla72tCCbzkwDi_7PyI23dByzC5GMS2v7jT-7YjDzr9LlZDPXG-xev4/pubhtml?gid=1875790254&single=true)

<img src="assets/images/test_case_definition_1.jpg" alt="test_case_definition" width="550"/>

<img src="assets/images/test_case_definition_2.jpg" alt="test_case_definition" width="550"/>

<br>

<br>

---

## 13. 🧪 테스트 케이스 상세

- 예약 생성시 사업자 알림

<img src="assets/test_cases/gif/place_reserve_owner.gif" alt="예약 생성시 사업자 알림" width="700"/>

<br>

- 모임 신청자 발생 시 개설자 알림

<img src="assets/test_cases/gif/meeting_builder_notification.gif" alt="모임 신청자 발생 시 개설자 알림" width="700"/>

<br>

- 모임 참가 신청 승인 및 포인트 지불

<img src="assets/test_cases/gif/meeting_participation_accept.gif" alt="모임 참가 신청 승인 및 포인트 지불" width="700"/>

<br>

- 포인트 부족 상황에서의 모임 개설

<img src="assets/test_cases/gif/point_meeting_fail.gif" alt="포인트 부족 상황에서의 모임 개설" width="700"/>

<br>

- 모임 개설 및 장소 대여비 지불 

<img src="assets/test_cases/gif/meeting_place_point.gif" alt="모임 개설 및 장소 대여비 지불" width="700"/>

<br>

- 모임 취소로 인한 참여자 장소 대여비 반환 및 사업자 장소 예약 대금 반환

<img src="assets/test_cases/gif/meeting_delete_refund.gif" alt="모임 참가 신청 승인 및 포인트 지불" width="700"/>

<br>

- 최대 인원으로 인한 장소 대여비 차액 반환

<img src="assets/test_cases/gif/max_people_point_refund.gif" alt="최대 인원으로 인한 장소 대여비 차액 반환" width="700"/>

<br>

<details>
<summary>👤 사용자 관련</summary>

<details>
<summary>계정 관리</summary>

- 자체 회원가입

<img src="assets/test_cases/user/Test_001.png" alt="계정 관리"/>

<br>

- 자체 회원 로그인

<img src="assets/test_cases/user/Test_002.png" alt="계정 관리"/>

<br>

- 비밀번호 찾기

<img src="assets/test_cases/user/Test_005.png" alt="계정 관리"/>

<br>

- 이메일 인증

<img src="assets/test_cases/user/Test_006.png" alt="계정 관리"/>

<br>

- 비밀번호 재설정

<img src="assets/test_cases/user/Test_007.png" alt="계정 관리"/>

<br>

- 계정 복구 신청

<img src="assets/test_cases/user/Test_008.png" alt="계정 관리"/>

<br>

- 토큰 재발급

<img src="assets/test_cases/user/Test_009.png" alt="계정 관리"/>

<br>

- 로그아웃

<img src="assets/test_cases/user/Test_010.png" alt="계정 관리"/>

<br>

- 회원 탈퇴

<img src="assets/test_cases/user/Test_011.png" alt="계정 관리"/>

<br>

</details>

<br>

<details>
<summary>프로필</summary>

- 프로필 조회

<img src="assets/test_cases/user/Test_012.png" alt="프로필 관리"/>

<br>

- 프로필 수정

<img src="assets/test_cases/user/Test_013.png" alt="프로필 관리"/>

<br>

</details>

<br>

<details>
<summary>계좌 관리</summary>

- 계좌 등록

<img src="assets/test_cases/user/Test_015.png" alt="계좌 관리"/>

<br>

- 게좌 수정

<img src="assets/test_cases/user/Test_016.png" alt="계좌 관리"/>

<br>

- 계좌 조회

<img src="assets/test_cases/user/Test_017.png" alt="계좌 관리"/>

<br>

- 사업자 등록

<img src="assets/test_cases/user/Test_018.png" alt="계좌 관리"/>

<br>


</details>

<br>

<details>
<summary>활동 관리</summary>

- 매너 온도 조회

<img src="assets/test_cases/user/Test_021.png" alt="활동 관리"/>

<br>

- 작성 게시글 조회

<img src="assets/test_cases/user/Test_022.png" alt="활동 관리"/>

<br>

- 작성 댓글 조회

<img src="assets/test_cases/user/Test_023.png" alt="활동 관리"/>

<br>

- 참가 모임 이력 조회

<img src="assets/test_cases/user/Test_024.png" alt="활동 관리"/>

<br>

- 포인트 조회

<img src="assets/test_cases/user/Test_025.png" alt="활동 관리"/>

<br>



</details>

<br>

<details>
<summary>친구 관리</summary>

- 친구 목록 조회

<img src="assets/test_cases/user/Test_026.png" alt="친구 관리"/>

<br>

- 친구 요청 신청

<img src="assets/test_cases/user/Test_027.png" alt="친구 관리"/>

<br>

- 친구 요청 수락

<img src="assets/test_cases/user/Test_028.png" alt="친구 관리"/>

<br>

- 친구 요청 거절 및 삭제

<img src="assets/test_cases/user/Test_029.png" alt="친구 관리"/>

<br>

- 친구 신청 목록 조회

<img src="assets/test_cases/user/Test_030.png" alt="친구 관리"/>

<br>

- 친구 개설 모임 조회

<img src="assets/test_cases/user/Test_031.png" alt="친구 관리"/>

<br>

</details>

<br>

<details>
<summary>회원 관리</summary>

- 회원 전체 목록 조회

<img src="assets/test_cases/user/Test_032.png" alt="회원 관리"/>

<br>

- 사업자 권한 신청 조회

<img src="assets/test_cases/user/Test_033.png" alt="회원 관리"/>

<br>

- 사업자 권한 승인

<img src="assets/test_cases/user/Test_034.png" alt="회원 관리"/>

<br>

- 사업자 권한 승인 거절

<img src="assets/test_cases/user/Test_035.png" alt="회원 관리"/>

<br>

</details>

<br>

<details>
<summary>알림 관리</summary>

- 알림 설정

<img src="assets/test_cases/notification/Test_102_set.png" alt="알림 관리"/>

<br>

<img src="assets/test_cases/notification/Test_103_set.png" alt="알림 관리"/>

<br>

- 알림 내역 조회

<img src="assets/test_cases/notification/Test_102.png" alt="알림 관리"/>

<br>

- 알림 확인 표시

<img src="assets/test_cases/notification/Test_103.png" alt="알림 관리"/>

<br>


</details>

<br>

</details>

<br>

<details>
<summary>🎯 포인트 관련</summary>

<details>
<summary>결제</summary>

- 포인트 충전

<img src="assets/test_cases/point/TEST-036.png" alt="포인트 충전"/>

<br>

</details>

<br>

<details>
<summary>포인트 관리</summary>

- 포인트 내역 조회

<img src="assets/test_cases/point/TEST-041.png" alt="포인트 관리"/>

<br>

- 잔여 포인트 환불

<img src="assets/test_cases/point/TEST-042.png" alt="포인트 관리"/>

<br>

</details>

<br>

<details>
<summary>정산</summary>

- 월별 정산 대금 조회

<img src="assets/test_cases/point/TEST-043.png" alt="포인트 정산"/>

<br>

- 정산 내역 조회

<img src="assets/test_cases/point/TEST-044.png" alt="포인트 정산"/>

<br>

</details>

<br>

<br>

</details>

<br>

<details>
<summary>📅 모임</summary>

<details>
<summary>모임 조회</summary>

- 관리자 모임 내역 조회

<img src="assets/test_cases/meeting/Test_46.png" alt="모임 조회"/>

<br>

- 관리자 모임 내역 조회 (조건별)

<img src="assets/test_cases/meeting/Test_46_1.png" alt="모임 조회"/>

<br>

- 개설 모임 목록 조회

<img src="assets/test_cases/meeting/Test_47.png" alt="모임 조회"/>

<br>

- 개설 모임 조건 검색 조회

<img src="assets/test_cases/meeting/Test_48.png" alt="모임 조회"/>

<br>

- 회원별 참가 모임 조회

<img src="assets/test_cases/meeting/Test_49.png" alt="모임 조회"/>

<br>

- 회원별 모임 내역 조회

<img src="assets/test_cases/meeting/Test_50.png" alt="모임 조회"/>

<br>

- 모임 상세 조회

<img src="assets/test_cases/meeting/Test_51.png" alt="모임 조회"/>

<br>


</details>

<br>

<details>
<summary>모임 개설</summary>

- 모임 개설

<img src="assets/test_cases/meeting/create_meeting_TEST-052.PNG" alt="모임 개설"/>

<br>

- 참가 요청 목록 확인
<img src="assets/test_cases/meeting/Test_53.png" alt="모임 개설"/>

<br>

- 참가 승인

<img src="assets/test_cases/meeting/accept_participation_TEST-054.PNG" alt="모임 개설"/>

<br>

- 참가 거절

<img src="assets/test_cases/meeting/reject_participation_TEST-055.PNG" alt="모임 개설"/>

<br>

- 개설자 참가 취소

<img src="assets/test_cases/meeting/leader_update_TEST-056.PNG" alt="모임 개설"/>

<br>

- 모임 취소

<img src="assets/test_cases/meeting/cancel_meeting_TEST-057.PNG" alt="모임 개설"/>

<br>

</details>

<br>

<details>
<summary>모임 참가</summary>

- 참가자 목록 조회

<img src="assets/test_cases/meeting/Test_58.png" alt="모임 참가"/>

<br>

- 참가 신청 성공

<img src="assets/test_cases/meeting/apply_participaiton_success_TEST-059.PNG" alt="모임 참가"/>

<br>

- 참가 신청 실패

<img src="assets/test_cases/meeting/apply_participation_fail_TEST-059.PNG" alt="모임 참가"/>

<br>

- 모임 참가 취소

<img src="assets/test_cases/meeting/cancle_participation_TEST-060.PNG" alt="모임 참가"/>

<br>

</details>

<br>

<details>
<summary>모임 찜</summary>

- 모임 찜 목록 조회

<img src="assets/test_cases/meeting/Test_61.png" alt="모임 찜"/>

<br>

- 모임 찜 등록

<img src="assets/test_cases/meeting/create_interested_meeting_TEST-062.PNG" alt="모임 찜"/>

<br>

- 모임 찜 취소

<img src="assets/test_cases/meeting/delete_interested_meeting_TEST-063.PNG" alt="모임 찜"/>

<br>


</details>

<br>

<details>
<summary>후기 및 평가</summary>

- 참가자 평가 내역 조회

<img src="assets/test_cases/meeting/Test_64.png" alt="모임 조회"/>

<br>

- 평가자별 참가자 평가 내역 조회

<img src="assets/test_cases/meeting/Test_65.png" alt="모임 조회"/>

<br>

- 대상자별 참가자 평가 내역 조회

<img src="assets/test_cases/meeting/Test_66.png" alt="모임 조회"/>

<br>

- 참가자 평가 작성 성공

<img src="assets/test_cases/meeting/participant_review_success_TEST-067.PNG" alt="모임 조회"/>

<br>

- 참가자 평가 작성 실패

<img src="assets/test_cases/meeting/participant_review_fail_TEST-067.PNG" alt="모임 조회"/>

</details>

<br>

</details>

<br>

<details>
<summary>📅 장소 관련</summary>


<details>
<summary>장소 목록</summary>

- 장소 전체 목록 조회

<img src="assets/test_cases/place/TEST-069.png" alt="장소 목록"/>

<br>

- 사업자 장소 목록 조회

<img src="assets/test_cases/place/Test_70.png" alt="장소 목록"/>

<br>

- 예약 장소 목록 조회

<img src="assets/test_cases/place/TEST-071.png" alt="장소 목록"/>

<br>

- 장소 상세 조회

<img src="assets/test_cases/place/TEST-072.png" alt="장소 목록"/>

<br>

- 운동/지역 별 장소 조회

<img src="assets/test_cases/place/TEST-073.png" alt="장소 목록"/>

<br>

- 장소 즐겨찾기 조회

<img src="assets/test_cases/place/TEST-074.png" alt="장소 목록"/>

<br>

- 장소 즐겨찾기 등록

<img src="assets/test_cases/place/TEST-075.png" alt="장소 목록"/>

<br>

- 장소 즐겨찾기 해제

<img src="assets/test_cases/place/TEST-076.png" alt="장소 목록"/>

</details>

<br>

<details>
<summary>장소 관리</summary>

- 장소 등록 

<img src="assets/test_cases/place/TEST-077.png" alt="장소 관리"/>

<br>

- 장소 수정

<img src="assets/test_cases/place/TEST-078.png" alt="장소 관리"/>

<br>

- 운영시간 수정

<img src="assets/test_cases/place/TEST-079.png" alt="장소 관리"/>

<br>

- 장소 사진 수정

<img src="assets/test_cases/place/TEST-080.png" alt="장소 관리"/>

</details>

<br>

<details>
<summary>예약</summary>

- 사업자별 예약 전체 조회

<img src="assets/test_cases/place/TEST-081.png" alt="예약"/>

<br>

- 사업자/상태별 예약 조회

<img src="assets/test_cases/place/TEST-082.png" alt="예약"/>

</details>

<br>

</details>

<br>

<details>
<summary>🗣️ 커뮤니티</summary>


<details>
<summary>게시글 기능 </summary>

- 게시글 전체 내역 조회

<img src="assets/test_cases/community/Test_83.png" alt="게시글"/>

<br>

- 회원별 게시글 내역 조회

<img src="assets/test_cases/community/Test_84.png" alt="게시글"/>

<br>

- 게시글 목록 조회

<img src="assets/test_cases/community/Test_85.png" alt="게시글"/>

<br>

- 게시글 검색

<img src="assets/test_cases/community/Test_86.png" alt="게시글"/>

<br>

- 게시글 상세 조회

<img src="assets/test_cases/community/Test_87.png" alt="게시글"/>

<br>

- 공지사항 등록

<img src="assets/test_cases/community/Test_88.png" alt="게시글"/>

<br>

<img src="assets/test_cases/community/Test_88_1.png" alt="게시글"/>

<br>


- 게시글 작성

<img src="assets/test_cases/community/TEST-089.jpg" alt="게시글"/>

<br>


- 게시글 수정

<img src="assets/test_cases/community/TEST-091.jpg" alt="게시글"/>

<br>

- 게시글 삭제

<img src="assets/test_cases/community/TEST-093.jpg" alt="게시글"/>

<br>

</details>

<br>

<details>
<summary>댓글 기능</summary>

- 댓글 전체 내역 조회

<img src="assets/test_cases/community/TEST-094.png" alt="댓글"/>

<br>

- 회원 별 댓글 내역 조회

<img src="assets/test_cases/community/TEST-095.png" alt="댓글"/>

<br>

- 댓글 작성

<img src="assets/test_cases/community/TEST-096.jpg" alt="댓글"/>

<br>

- 댓글 삭제

<img src="assets/test_cases/community/TEST-097.jpg" alt="댓글"/>

</details>

<br>

<details>
<summary>좋아요 기능</summary>

- 게시글 좋아요

<img src="assets/test_cases/community/TEST-098.png" alt="좋아요"/>

<br>

- 게시글 좋아요 취소

<img src="assets/test_cases/community/TEST-099.png" alt="좋아요"/>

<br>

- 댓글 좋아요

<img src="assets/test_cases/community/TEST-100.png" alt="좋아요"/>

<br>

- 댓글 좋아요 취소

<img src="assets/test_cases/community/TEST-101.png" alt="좋아요"/>

</details>

</details>

<br>

<details>
<summary>🛡️ 관리자 기능</summary>


<details>
<summary>신고 기능</summary>

- 전체 신고 내역 조회

<img src="assets/test_cases/report/Test_124.png" alt="관리자"/>

<br>

- 신고 유형별 내역 조회

<img src="assets/test_cases/report/Test_125.png" alt="관리자"/>

<br>

- 누적 신고 횟수별 신고자 목록 조회

<img src="assets/test_cases/report/Test_126.png" alt="관리자"/>

<br>

- 누적 신고 점수별 피신고자 목록 조회

<img src="assets/test_cases/report/Test_127.png" alt="관리자"/>

<br>

- 신고자별 신고 내역 조회

<img src="assets/test_cases/report/Test_128.png" alt="관리자"/>

<br>

- 피신고자별 신고 내역 조회

<img src="assets/test_cases/report/Test_129.png" alt="관리자"/>

<br>

- 사용자 신고

<img src="assets/test_cases/report/Test_130.png" alt="관리자"/>

<br>

- 게시글 신고

<img src="assets/test_cases/report/Test_131.png" alt="관리자"/>

<br>

- 댓글 신고

<img src="assets/test_cases/report/Test_132.png" alt="관리자"/>

<br>

- 허위 신고 처리

<img src="assets/test_cases/report/Test_133.png" alt="관리자"/>

<br>

- 신고 처리

<img src="assets/test_cases/report/Test_134.png" alt="관리자"/>

</details>


<details>
<summary>사용자 제재 기능 </summary>

- 제재 내역 전체 조회

<img src="assets/test_cases/report/Test_135.png" alt="관리자"/>

<br>

- 종류별 제재 내역 조회

<img src="assets/test_cases/report/Test_136.png" alt="관리자"/>

<br>

- 사용자별 제재 내역 조회

<img src="assets/test_cases/report/Test_137.png" alt="관리자"/>

<br>

- 사용자 및 종류별 제재 내역 조회

<img src="assets/test_cases/report/Test_138.png" alt="관리자"/>

<br>

- 게시글 제재

<img src="assets/test_cases/report/Test_139.png" alt="관리자"/>

<br>

- 댓글 제재

<img src="assets/test_cases/report/Test_140.png" alt="관리자"/>

<br>

- 장소 후기 제재 요청

<img src="assets/test_cases/report/Test_141.png" alt="관리자"/>

<br>

- 장소 후기 제재 확정

<img src="assets/test_cases/report/Test_142.png" alt="관리자"/>

<br>

- 제재 철회

<img src="assets/test_cases/report/Test_143.png" alt="관리자"/>

</details>

<br>


<details>
<summary>이의 제기 기능 </summary>

- 이의 제기 전체 내역 조회

<img src="assets/test_cases/report/Test_144.png" alt="관리자"/>

<br>

- 상태별 이의 내역 조회

<img src="assets/test_cases/report/Test_145.png" alt="관리자"/>

<br>

- 사용자별 이의 내역 조회

<img src="assets/test_cases/report/Test_146.png" alt="관리자"/>

<br>

- 장소 후기 이의 신청

<img src="assets/test_cases/report/Test_147.png" alt="관리자"/>

<br>

- 댓글 제재 이의 신청

<img src="assets/test_cases/report/Test_148.png" alt="관리자"/>

<br>

- 댓글 제재 이의 신청

<img src="assets/test_cases/report/Test_149.png" alt="관리자"/>

<br>

- 이의 제기 승인

<img src="assets/test_cases/report/Test_150.png" alt="관리자"/>

<br>

- 이의 제기 거절

<img src="assets/test_cases/report/Test_151.png" alt="관리자"/>

</details>

<br>


<details>
<summary>블랙리스트 기능 </summary>

- 블랙리스트 조회

<img src="assets/test_cases/report/Test_152.png" alt="관리자"/>

<br>

- 블랙리스트 등록

<img src="assets/test_cases/report/Test_153.png" alt="관리자"/>

<br>

- 블랙리스트 해제

<img src="assets/test_cases/report/Test_154.png" alt="관리자"/>


</details>

</details>

<br>

---

## 14. 🫂 팀원 회고


|**곽진웅**|
|------|

> 처음 해보는 백엔드 프로젝트라 어려워서 많이 헤맸지만 그 과정에서 많이 배울 수 있었습니다. git 관리, 코딩 컨벤션, 네이밍, 유지보수성 등이 중요하다고 배우지만 경험이 없다보니 크게 와닿지는 않았는데, 이번 프로젝트를 진행해보며 정말 실감할 수 있었습니다. 특히, 문자열을 이용한 방식이 유지보수성이 좋지 않으므로 그 대안으로 Enum 클래스를 사용한다고 배웠는데, 그게 왜 좋은 지 몸소 느낄 수 있었던 게 기억에 남습니다. 저번 DB 프로젝트에서는 비즈니스 로직을 거의 건드려보지 못했는데, 이번 프로젝트에서는 Spring이라는 새로운 툴에 적응하면서 복잡한 로직에 대한 코드 또한 작성한 걸 보면 많이 성장했구나 싶습니다. 개발 흐름을 파악하는 것이 어려웠는데 이번 프로젝트를 통해 어느 정도 파악했으니, 앞으로의 백엔드 프로젝트는 더 잘 수행할 수 있을 것으로 생각합니다. 기능 구현하느라 시간이 부족해 테스트 코드를 작성해보지 못한 점이 조금 아쉽습니다. 실력 좋은 팀원들 덕분에 많이 배웠습니다!

<br>


|**김여진**|
|------|

> 자바를 처음 다뤄보는 만큼, 프로젝트 시작 전부터 불안감이 정말 컸던 것 같습니다.
모르는 게 많았고, 무엇을 어떻게 해야 할지도 막막해서 매번 검색이나 질문에 의존하게 된 부분이 많았습니다. 그래도 여러 도움 덕분에 맡은 역할만큼은 무사히 수행한 것 같아 그 점에서 조금은 안심이 되었습니다.
시간이 촉박하다 보니 처음에 구상했던 모든 내용을 구현하지는 못했지만, 선택과 집중을 통해 필요한 기능을 중심으로 현실적인 범위 안에서 마무리할 수 있었던 점은 의미 있게 느껴졌습니다.
다만, 여유 시간이 부족하다 보니 내가 작성한 코드가 정말 적절한 방식이었는지 팀원들과 충분히 이야기 나누거나 리뷰를 받을 수 없었던 점은 아쉬움으로 남습니다.

<br>

|**박성용**|
|------|

> 처음에 프로젝트를 시작하면서 의욕이 넘쳤습니다. 이것도 저것도 쉽게쉽게 구현할 수 있을 줄 알았는데 생각보다 너무 어려웠고 매핑 하나 하는것도 힘이 들었습니다. 나중에는 구현하는게 무섭기까지 했습니다. 그럼에도 불구하고 강사님 강의도 반복해서 듣고 반복하여 실제 코드를 작성하다보니 코드 작성하는 것이 익숙해지고 나중에는 요령도 생겨서 더 효율적이고 효과적이고 안정적인 기능 구현 방법은 무엇일까 하는 고민들을 할 수 있어 개인적으로 크게 성장 한 것 같습니다. 처음 프로젝트 시작할 때 기능 구현을 빨리 하고 팀원분들을 많이 도와드릴려고 했는데 생각보다 시간이 남지 않아 많이 도와드리지 못하여 아쉬움이 있습니다. 
개인적으로 이번 프로젝트에서 책임감을 느끼고 임하다보니 더 많이 공부하게 된 것 같고 단지 제가 아는 것들을 많은 분들과 나누지 못한게 아쉽습니다. 따라서 프론트엔드때는 꼭꼭 깃허브 관리도 전문가수준으로 설계도 해보고 프로젝트를 설계하는 것이 저의 꿈입니다...

<br>


|**박준서**|
|------|

> 이번 프로젝트를 통해 여러모로 다양한 것을 배울 수 있었습니다. 기술적인 측면은 물론, 팀원 간 의견이 다를 때 어떻게 조율하고 진행할지에 대한 협업 방식도 익힐 수 있었습니다. 대학교 시절의 프로젝트는 주어진데이터를 기반으로 단순한 쿼리를 작성하거나 빨리빨리 프로젝트를 끝내기 위해서 기술만 사용하는 프로젝트였습니다. 이번에는 처음부터 끝까지 팀원들과 소통하며 함께 방향을 정하고 , 다양한 기술을 기초부터 배우는 과정이 특히 의미 있었습니다. 아직 부족한 점이 많아 기여를 많이 못하였지만, 팀원분들의 많은 도움 덕분에 문제를 해결해나갈 수 있었고, 이 과정에서 많은 것을 배울 수 있었습니다. 다만, 욕심이 앞서 이것저것 시도해보고 싶었지만 역량이 부족해 다 해내지 못한 점은 아쉬움으로 남습니다. 하지만 이 역시 값진 배움의 일부라 생각하며, 앞으로 진행할 프론트앤드 프로젝트에서는 더 성장한 모습으로 임하겠습니다.

<br>


|**장건희**|
|------|

> 처음 Notification 도메인을 맡았을 때는 "내가 이걸 다 구현할 수 있을까?" 하는 걱정이 정말 컸던 것 같습니다.알림이라는 게 단순히 메시지를 띄우는 정도로 생각했었는데, 막상 구현하려다 보니 이메일 전송, 실시간 전송(SSE), 유저의 수신 설정 등 생각보다 고려할 게 많았고, 구조를 어떻게 잡아야 할지부터 막막했던 기억이 납니다.
특히나 프로젝트 후반에는 MSA 구조로 알림 시스템을 외부 API로 전환해야 하는 이슈까지 생기면서, 기존 방식으로는 한계가 있겠다는 걸 절감했고, NotificationHelper 구조로 다른 도메인에서도 알림을 쉽게 보낼 수 있도록 만들고, 템플릿 변수 처리나 반복 알림 처리 같은 부분도 하나씩 해결해가면서 점점 알림 시스템의 윤곽이 잡혀가는 과정이 기억에 남습니다.
사실 처음에는 실시간 통신(SSE)이나 Gmail SMTP 같은 것들도 낯설어서 에러 로그만 봐도 당황하곤 했는데, 포스트맨으로 테스트해보고 직접 로그 찍어가며 하나씩 해결해보니까 오히려 그 과정을 통해 자신감이 붙은 것 같습니다.
이번 프로젝트를 통해 단순히 기능을 구현하는 걸 넘어서, "이 기능이 유지보수가 쉬울까?", "나중에 확장하려면 어떻게 짜야 할까?" 같은 고민을 하게 됐고, 그게 백엔드 개발자로서 한 단계 성장할 수 있었던 계기가 된 것 같습니다. 부족한 점도 많았고, 완벽한 코드를 짰다고 말할 수는 없지만, 함께 해준 팀원들 덕분에 끝까지 잘 마무리할 수 있었던 것 같습니다. 다들 고생 많으셨고, 덕분에 정말 많이 배웠습니다!

<br>


|**최지혜**|
|------|

> 프로젝트를 하며 처음에 어디서부터 해나가야 할지 감도 잡히지 않았는데 직접 부딪히며 하다 보니 어느 정도의 방향성을 잡아나갈 수 있었습니다. 이슈 생성도 해보고 코드 작성도 해보면서 배운 내용들을 적용해 나가다 보니 어느 부분을 모르고 어떻게 해결해야 하는지에 대해 공부하며 정리해나갈 수 있었습니다. 코드 오류의 연속이었지만 포스트맨으로 성공한 걸 보면 신기하면서도 조금씩 성장해가는 느낌을 받았습니다.  프로젝트를 하며 부족한 점을 몸소 느꼈고 프로젝트 준비 기간이 조금만 더 있었으면 하는 마음이 컸습니다. 이번 프로젝트를 통해 많이 배웠고 성장해나갈 수 있는 좋은 기회였습니다. 프로젝트 준비 열심히 해준 팀원분들 고생 많으셨고 모르는 부분 잘 알려주셔서 감사했습니다

<br>

---
