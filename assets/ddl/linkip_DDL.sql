-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        11.4.4-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 0. linkupdb 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `linkupdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `linkupdb`;

-- 외래 키 제약 조건을 무시하여 테이블 삭제
-- -----------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

-- 0-1. 기초 테이블
DROP TABLE IF EXISTS `status`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `sport_type`;

-- 0-2. 사용자 테이블
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `owner`;
DROP TABLE IF EXISTS `friend`;
DROP TABLE IF EXISTS `user_sport`;

-- 0-3. 포인트 테이블
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `point_transaction`;
DROP TABLE IF EXISTS `settlement`;

-- 0-4. 장소 관련 테이블
DROP TABLE IF EXISTS `place`;
DROP TABLE IF EXISTS `place_image`;
DROP TABLE IF EXISTS `operation_time`;
DROP TABLE IF EXISTS `favorite`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `place_review`;

-- 0-5. 모임 관련 테이블
DROP TABLE IF EXISTS `meeting`;
DROP TABLE IF EXISTS `meeting_participation_history`;
DROP TABLE IF EXISTS `participant_review`;
DROP TABLE IF EXISTS `best_player`;
DROP TABLE IF EXISTS `interested_meeting`;

-- 0-6. 커뮤니티 테이블
DROP TABLE IF EXISTS `community_post`;
DROP TABLE IF EXISTS `post_like`;
DROP TABLE IF EXISTS `post_image`;
DROP TABLE IF EXISTS `community_comment`;
DROP TABLE IF EXISTS `comment_like`;

-- 0-7. 알림 테이블
DROP TABLE IF EXISTS `notification_type`;
DROP TABLE IF EXISTS `notification_setting`;
DROP TABLE IF EXISTS `domain_type`;
DROP TABLE IF EXISTS `notification`;

-- 0-8. 신고 및 제재 테이블
DROP TABLE IF EXISTS `report_type`;
DROP TABLE IF EXISTS `report_history`;
DROP TABLE IF EXISTS `user_penalty_history`;
DROP TABLE IF EXISTS `objection`;
DROP TABLE IF EXISTS `blacklist`;

-- 0-9. 토큰 관련 테이블
DROP TABLE IF EXISTS `verification_token`;
DROP TABLE IF EXISTS `refresh_token`;

SET FOREIGN_KEY_CHECKS = 1;


-- 1. 테이블 생성

-- 1-1. 기초 테이블 생성

-- 1) 상태 테이블 생성
CREATE TABLE IF NOT EXISTS `status` (
    `status_id`   INT         NOT NULL AUTO_INCREMENT COMMENT '상태 ID',
    `status_type` VARCHAR(8)  NOT NULL UNIQUE COMMENT '상태 유형',
    PRIMARY KEY (`status_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='상태 코드 테이블';

-- 2) 권한 테이블 생성
CREATE TABLE IF NOT EXISTS `role` (
    `role_id`   TINYINT      NOT NULL AUTO_INCREMENT COMMENT '권한 ID',
    `role_name` VARCHAR(10)  NOT NULL UNIQUE COMMENT '권한명',
    PRIMARY KEY (`role_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='회원 역할 정의 테이블';

-- 3) 운동 종목 테이블 생성
CREATE TABLE IF NOT EXISTS `sport_type` (
    `sport_id`   TINYINT      NOT NULL AUTO_INCREMENT COMMENT '운동 ID',
    `sport_name` VARCHAR(20)  NOT NULL UNIQUE COMMENT '운동 종목',
    PRIMARY KEY (`sport_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='스포츠 종목 정의 테이블';





-- 1-2. 사용자 관련 테이블 생성

-- 1) 사용자 테이블 생성
CREATE TABLE IF NOT EXISTS `user` (
    `user_id`        INT          NOT NULL AUTO_INCREMENT COMMENT '사용자 ID',
    `role_id`        TINYINT      NOT NULL COMMENT '권한 ID',
    `status_id`      INT          NOT NULL COMMENT '상태 ID',
    `user_name`      VARCHAR(50)  NOT NULL COMMENT '사용자 명',
    `password`       VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `contact_number` VARCHAR(30)  UNIQUE COMMENT '전화번호',
    `email`          VARCHAR(50)  NOT NULL UNIQUE COMMENT '이메일',
    `point_balance`  INT          NOT NULL DEFAULT 0 COMMENT '현재 포인트 잔액',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 일시',
    `deleted_at`     DATETIME     DEFAULT NULL COMMENT '탈퇴 신청 일시',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB 
  AUTO_INCREMENT=10
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='사용자 정보 테이블';


-- 2) 회원 테이블 생성
CREATE TABLE IF NOT EXISTS `member` (
    `member_id`            INT                NOT NULL COMMENT '사용자 ID',
    `gender`               ENUM('M', 'F')     NOT NULL COMMENT '성별',
    `nickname`             VARCHAR(30)        NOT NULL UNIQUE COMMENT '닉네임',
    `birth_date`           DATETIME           DEFAULT NULL COMMENT '생년월일',
    `introduction`         TEXT                        COMMENT '자기소개',
    `manner_temperature`   DECIMAL            NOT NULL DEFAULT 36.5 COMMENT '매너온도',
    `profile_image_url`    TEXT               NOT NULL DEFAULT '기본이미지URL' COMMENT '프로필 이미지 URL',
    PRIMARY KEY (`member_id`),
    CONSTRAINT `chk_member_gender` CHECK (`gender` IN ('M', 'F'))
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='회원 정보 테이블';

-- 3) 사업자 테이블 생성
-- 사업자 테이블 생성
CREATE TABLE IF NOT EXISTS `owner` (
    `owner_id`                          INT       NOT NULL COMMENT '사업자 ID',
    `status_id`                         INT       NOT NULL COMMENT '인증 상태 ID',
    `business_registration_document_url` TEXT     NOT NULL COMMENT '사업자 등록증 파일 경로',
    `authorized_at`                     DATETIME  DEFAULT NULL COMMENT '권한 승인 날짜',
    `rejection_reason`                  TEXT      DEFAULT NULL COMMENT '반려 이유',
    PRIMARY KEY (`owner_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='사업자 정보를 저장하는 테이블';

-- 4) 친구 관계 테이블 생성
CREATE TABLE IF NOT EXISTS `friend` (
    `requester_id`   INT        NOT NULL COMMENT '신청자 ID',
    `addressee_id`    INT        NOT NULL COMMENT '피요청자 ID',
    `status_id`      INT        NOT NULL COMMENT '상태 ID',
    `requested_at`   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청 일시',
    `responded_at`   DATETIME   DEFAULT NULL COMMENT '응답 일시',
    PRIMARY KEY (`requester_id`, `addressee_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='친구 관계 테이블';

-- 5) 선호 운동 테이블 생성
CREATE TABLE IF NOT EXISTS `user_sport` (
    `member_id`  INT      NOT NULL COMMENT '사용자 ID',
    `sport_id`   TINYINT  NOT NULL COMMENT '운동 ID',
    PRIMARY KEY (`member_id`, `sport_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='회원 관심 스포츠 연결 테이블';





-- 1-3. 포인트 테이블 생성

-- 1) 계좌 테이블 생성
CREATE TABLE IF NOT EXISTS `account` (
    `account_id`      INT           NOT NULL AUTO_INCREMENT COMMENT '계좌 ID',
    `user_id`         INT           NOT NULL COMMENT '사용자 ID',
    `status_id`       INT           NOT NULL COMMENT '상태 ID',
    `bank_name`       VARCHAR(255)  NOT NULL COMMENT '은행명',
    `account_number`  VARCHAR(255)  NOT NULL UNIQUE COMMENT '계좌 번호',
    `holder_name`     VARCHAR(50)   NOT NULL COMMENT '예금주 명',
    `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
    PRIMARY KEY (`account_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='사용자 또는 사업자의 계좌 정보 테이블';

-- 2) 포인트 거래 내역 테이블 생성
CREATE TABLE IF NOT EXISTS `point_transaction` (
    `point_transaction_id` BIGINT         NOT NULL AUTO_INCREMENT COMMENT '거래내역 ID',
    `user_id`              INT            NOT NULL COMMENT '사용자 ID',
    `amount`               INT            NOT NULL COMMENT '거래 포인트',
    `transaction_type`     ENUM('CHARGE', 'PAYMENT', 'REFUND', 'WITHDRAW') NOT NULL COMMENT '트랜잭션 유형',
    `created_at`           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    PRIMARY KEY (`point_transaction_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='사용자 또는 사업자의 포인트 거래 이력을 저장하는 테이블';
  
-- 3) 정산 테이블 생성
CREATE TABLE IF NOT EXISTS settlement (
    settlement_id INT AUTO_INCREMENT COMMENT 'PK: 정산 고유 ID',
    owner_id INT NOT NULL COMMENT 'FK: 정산 요청 사업자 ID',
    amount INT NOT NULL COMMENT '정산 금액 (원 단위)',
    completed_at DATETIME DEFAULT NULL COMMENT '정산 완료 일시 (nullable)',
    PRIMARY KEY (settlement_id)
) ENGINE=InnoDB COMMENT='사업자 정산 요청 및 처리 내역 테이블';





-- 1-4. 장소 관련 테이블 생성
-- 1) 장소 테이블 생성
CREATE TABLE IF NOT EXISTS `place` (
    `place_id`      INT          NOT NULL AUTO_INCREMENT COMMENT '장소 ID',
    `owner_id`      INT          NOT NULL COMMENT '사업자 ID',
    `sport_id`      TINYINT      NOT NULL COMMENT '운동 ID',
    `place_name`    VARCHAR(255) NOT NULL COMMENT '장소 이름',
    `address`       TEXT         NOT NULL COMMENT '주소',
    `description`   TEXT         DEFAULT NULL COMMENT '장소 설명',
    `equipment`     TEXT         DEFAULT NULL COMMENT '장비 정보',
    `min_user`		  INT				NOT NULL DEFAULT 2 COMMENT '최소 인원',
    `max_user`		  INT				NOT NULL DEFAULT 2 COMMENT '최대 인원',
    `latitude`      DECIMAL(10,6) NOT NULL COMMENT '위도',
    `longitude`     DECIMAL(10,6) NOT NULL COMMENT '경도', 
    `is_active`     ENUM('Y', 'N') NOT NULL DEFAULT 'Y' COMMENT '활성화 여부',
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `rental_cost`   INT          NOT NULL DEFAULT 0 COMMENT '장소 대여비',
    PRIMARY KEY (`place_id`),
    CHECK (`min_user` >= 2),
    CHECK (`min_user` <= `max_user`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='운동 모임을 위한 장소 정보를 저장하는 테이블';

-- 2) 장소 사진 테이블 생성
CREATE TABLE IF NOT EXISTS `place_image` (
    `image_id`   INT   NOT NULL AUTO_INCREMENT COMMENT '장소 사진 ID',
    `place_id`   INT   NOT NULL COMMENT '장소 ID',
    `image_url`  TEXT  NOT NULL DEFAULT '기본이미지URL' COMMENT '사진 URL',
    PRIMARY KEY (`image_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='장소에 연결된 사진 정보를 저장하는 테이블';

-- 3) 운영 시간 테이블 생성
CREATE TABLE IF NOT EXISTS `operation_time` (
    `operating_time_id`  INT   			 NOT NULL AUTO_INCREMENT COMMENT '운영 시간 ID',
    `place_id`           INT   			 NOT NULL COMMENT '장소 ID',
    `day_of_week`        ENUM('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'BREAK') NOT NULL COMMENT '요일',
    `start_time`         TIME  			 NOT NULL COMMENT '시작 시간',
    `end_time`           TIME  		 	 NOT NULL COMMENT '종료 시간',
    `unit_time`          INT  			 NOT NULL COMMENT '단위 시간',
    PRIMARY KEY (`operating_time_id`),
    CHECK (`unit_time` % 10 = 0)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='장소의 요일별 운영 시간을 저장하는 테이블';

-- 4) 장소 즐겨찾기 테이블 생성
CREATE TABLE IF NOT EXISTS `favorite` (
    `place_id`   INT  NOT NULL COMMENT '장소 ID',
    `member_id`  INT  NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (`place_id`, `member_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='회원이 즐겨찾기한 장소 정보를 저장하는 테이블';





-- 1-5. 모임 관련 테이블 생성
-- 1) 모임 테이블 생성
CREATE TABLE IF NOT EXISTS `meeting` (
    `meeting_id`              INT           NOT NULL AUTO_INCREMENT COMMENT '모임 ID',
    `leader_id`               INT           NOT NULL COMMENT '개설자 ID',
    `place_id`                INT           DEFAULT NULL COMMENT '장소 ID',
    `sport_id`                TINYINT       NOT NULL COMMENT '운동 ID',
    `status_id`               INT           NOT NULL COMMENT '상태 ID',
    `meeting_title`           VARCHAR(255)  NOT NULL COMMENT '모임 제목',
    `meeting_content`         TEXT          NOT NULL COMMENT '모임 내용',
    `date`                    DATE          NOT NULL COMMENT '모임 날짜',
    `start_time`              TIME          NOT NULL COMMENT '시작 시간',
    `end_time`                TIME          NOT NULL COMMENT '종료 시간',
    `min_user`                INT           NOT NULL COMMENT '최소 인원',
    `max_user`                INT           NOT NULL COMMENT '최대 인원',
    `created_at`              DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '개설 일시',
    `gender`                  ENUM('M', 'F', 'BOTH') NOT NULL DEFAULT 'BOTH' COMMENT '성별 제한',
    `age_group`               SET('10', '20', '30', '40', '50', '60', '70+') NOT NULL COMMENT '연령대 제한',
    `level`                   SET('LV1', 'LV2', 'LV3') NOT NULL COMMENT '실력 제한',
    `custom_place_address`    VARCHAR(255)  DEFAULT NULL COMMENT '사용자 설정 주소',
    `latitude`                DECIMAL(10,6) DEFAULT NULL COMMENT '사용자 설정 주소 위도',
    `longitude`               DECIMAL(10,6) DEFAULT NULL COMMENT '사용자 설정 주소 경도',
    PRIMARY KEY (`meeting_id`),
    CHECK (`min_user` >= 1),
    CHECK (`min_user` <= `max_user`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='개설된 모임 정보를 저장하는 테이블';
  
-- 2) 모임 찜 테이블 생성
CREATE TABLE IF NOT EXISTS `interested_meeting` (
    `meeting_id`  INT NOT NULL COMMENT '모임 ID',
    `member_id`   INT NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (`meeting_id`, `member_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='모임 찜 등록 내역을 저장하는 테이블';

-- 3) 모임 참여 이력 테이블 생성
CREATE TABLE `meeting_participation_history` (
    `participation_id`  BIGINT     NOT NULL AUTO_INCREMENT COMMENT '모임 참여 이력 ID',
    `member_id`         INT        NOT NULL COMMENT '사용자 ID',
    `meeting_id`        INT        NOT NULL COMMENT '모임 ID',
    `status_id`         INT        NOT NULL COMMENT '상태 ID',
    `participated_at`   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '참여 일시',
    PRIMARY KEY (`participation_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='모임 참여 이력을 저장하는 테이블';

-- 4) 장소 예약 테이블 생성
-- 장소 예약 테이블 생성
CREATE TABLE `reservation` (
    `reservation_id`    BIGINT     		NOT NULL AUTO_INCREMENT COMMENT '예약 ID',
    `place_id`          INT        		NOT NULL COMMENT '장소 ID',
    `meeting_id`        INT        					DEFAULT NULL COMMENT '모임 ID',
    `status_id`         INT        		NOT NULL COMMENT '상태 ID',
    `reservation_date`  DATE       		NOT NULL COMMENT '사용일',
    `start_time`        TIME       		NOT NULL COMMENT '시작 시간',
    `end_time`          TIME       		NOT NULL COMMENT '종료 시간',
    PRIMARY KEY (`reservation_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='장소 예약 정보를 저장하는 테이블';





-- 1-6. 후기 및 평가 테이블 생성
-- 1) 참가자 평가 테이블 생성
CREATE TABLE `participant_review` (
    `review_id`     BIGINT     NOT NULL AUTO_INCREMENT COMMENT '평가 ID',
    `reviewer_id`   INT        NOT NULL COMMENT '평가자 ID',
    `reviewee_id`   INT        NOT NULL COMMENT '평가 대상자 ID',
    `meeting_id`    INT        NOT NULL COMMENT '모임 ID',
    `score`         INT        NOT NULL DEFAULT 3 COMMENT '평점',
    `created_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '평가 일시',
    PRIMARY KEY (`review_id`),
    CHECK (`score` BETWEEN 1 AND 5)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='모임 참가자에 대한 평가 이력을 저장하는 테이블';

-- 2) 장소 후기 테이블 생성
CREATE TABLE `place_review` (
    `review_id`             INT     NOT NULL AUTO_INCREMENT COMMENT '장소 후기 ID',
    `member_id`             INT     NOT NULL COMMENT '사용자 ID',
    `place_id`              INT     NOT NULL COMMENT '장소 ID',
    `participation_id`      BIGINT  NOT NULL COMMENT '모임 참여 이력 ID',
    `status_id`         	 INT        NOT NULL COMMENT '상태 ID',
    `review_content`        TEXT    DEFAULT NULL COMMENT '후기 내용',
    `review_image_url`      TEXT    DEFAULT NULL COMMENT '후기 사진',
    `review_score`          INT     NOT NULL COMMENT '별점',
    PRIMARY KEY (`review_id`),
    CHECK (`review_score` BETWEEN 1 AND 5)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='회원이 작성한 장소 후기 정보를 저장하는 테이블';

-- 3) 베스트 플레이어 테이블 생성
CREATE TABLE `best_player` (
    `meeting_id`  INT NOT NULL COMMENT '모임 ID',
    `member_id`   INT NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (`meeting_id`, `member_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='모임별 베스트 플레이어 정보를 저장하는 테이블';


-- 1-7. 커뮤니티 테이블 생성
-- 1) 게시글 테이블 생성
CREATE TABLE `community_post` (
    `post_id`      INT           NOT NULL AUTO_INCREMENT COMMENT '게시글 ID',
    `user_id`      INT           NOT NULL COMMENT '사용자 ID',
    `title`        VARCHAR(30)   NOT NULL COMMENT '제목',
    `content`      TEXT          NOT NULL COMMENT '내용',
    `is_deleted`   ENUM('Y','N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
    `created_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    `updated_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    `deleted_at`   DATETIME      DEFAULT NULL COMMENT '삭제 일시',
    `is_notice`    ENUM('Y','N') NOT NULL DEFAULT 'N' COMMENT '공지사항 여부',
    PRIMARY KEY (`post_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='사용자 게시글을 저장하는 테이블';

-- 2) 게시글 좋아요 테이블 생성
CREATE TABLE `post_like` (
    `post_like_id`  INT NOT NULL AUTO_INCREMENT COMMENT '게시글 좋아요 ID',
    `post_id`       INT NOT NULL COMMENT '게시글 ID',
    `user_id`       INT NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (`post_like_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='게시글에 대한 사용자 좋아요 정보를 저장하는 테이블';

-- 3) 게시글 사진 테이블 생성
CREATE TABLE `post_image` (
    `image_id`   INT   NOT NULL AUTO_INCREMENT COMMENT '게시글 사진 ID',
    `post_id`    INT   NOT NULL COMMENT '게시글 ID',
    `image_url`  TEXT  NOT NULL COMMENT '사진 URL',
    PRIMARY KEY (`image_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='게시글에 첨부된 사진 정보를 저장하는 테이블';

-- 4) 댓글 테이블 생성
CREATE TABLE `community_comment` (
    `comment_id`   BIGINT        NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
    `post_id`      INT           NOT NULL COMMENT '게시글 ID',
    `user_id`      INT           NOT NULL COMMENT '사용자 ID',
    `content`      TEXT          NOT NULL COMMENT '댓글 내용',
    `is_deleted`   ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
    `created_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    `deleted_at`   DATETIME      DEFAULT NULL COMMENT '삭제 일시',
    PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='게시글에 작성된 댓글 정보를 저장하는 테이블';

-- 5) 댓글 좋아요 테이블 생성
CREATE TABLE `comment_like` (
    `comment_like_id`   INT     NOT NULL AUTO_INCREMENT COMMENT '댓글 좋아요 ID',
    `comment_id`        BIGINT  NOT NULL COMMENT '댓글 ID',
    `user_id`           INT     NOT NULL COMMENT '사용자 ID',
    PRIMARY KEY (`comment_like_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='댓글에 대한 사용자 좋아요 정보를 저장하는 테이블';





-- 1-8.  알림 테이블 생성
-- 1) 알림 유형 테이블 생성
CREATE TABLE `notification_type` (
    `notification_type_id`     TINYINT        NOT NULL COMMENT '알림 유형 ID',
    `notification_type`        VARCHAR(50)    NOT NULL UNIQUE COMMENT '알림 유형 코드',
    `send_sns`                 ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT 'SNS 알림 여부',
    `send_email`               ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '이메일 알림 여부',
    `notification_template`    TEXT           NOT NULL COMMENT '알림 템플릿 내용',
    PRIMARY KEY (`notification_type_id`)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_general_ci 
  COMMENT='다양한 알림 유형 정보를 관리하는 테이블';

-- 2) 알림 설정 테이블 생성
CREATE TABLE `notification_setting` (
    `notification_setting_id`   INT           NOT NULL AUTO_INCREMENT COMMENT '알림 설정 ID',
    `user_id`                   INT           NOT NULL COMMENT '사용자 ID',
    `notification_type_id`      TINYINT       NOT NULL COMMENT '알림 유형 ID',
    `is_enabled`               ENUM('Y','N') NOT NULL DEFAULT 'Y' COMMENT '알림 수신 여부',
    PRIMARY KEY (`notification_setting_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자의 알림 수신 여부 설정을 저장하는 테이블';

-- 3) 도메인 타입 테이블 생성
CREATE TABLE `domain_type` (
    `domain_type_id`  TINYINT       NOT NULL COMMENT '도메인 타입 ID',
    `domain_name`     VARCHAR(50)   NOT NULL UNIQUE COMMENT '도메인 명',
    PRIMARY KEY (`domain_type_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='알림이 발생한 도메인 정보를 관리하는 테이블';

-- 4) 알림 테이블 생성
CREATE TABLE `notification` (
    `notification_id`         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '알림 ID',
    `receiver_id`             INT            NOT NULL COMMENT '수신자 ID',
    `domain_type_id`          TINYINT        NOT NULL COMMENT '도메인 유형 ID',
    `notification_type_id`    TINYINT        NOT NULL COMMENT '알림 유형 ID',
    `domain_id`               INT                     COMMENT '알림 관련 리소스 ID',
    `title`                   VARCHAR(200)   NOT NULL COMMENT '알림 제목',
    `content`                 TEXT           NOT NULL COMMENT '알림 본문 내용',
    `is_read`                 ENUM('Y','N')  NOT NULL DEFAULT 'N' COMMENT '읽음 여부',
    `created_at`              DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '알림 생성 일시',
    `read_at`                 DATETIME                DEFAULT NULL COMMENT '알림 확인 시점',
    PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='시스템에서 발생한 알림 정보를 저장하는 테이블';





-- 1-9. 신고 및 제재 테이블 생성
-- 1) 신고 유형 테이블 생성
CREATE TABLE `report_type` (
    `report_type_id`   TINYINT       NOT NULL AUTO_INCREMENT COMMENT '신고 유형 ID',
    `report_type`      VARCHAR(20)   NOT NULL UNIQUE COMMENT '신고 유형 코드',
    `report_level`     TINYINT       NOT NULL COMMENT '신고 레벨',
    PRIMARY KEY (`report_type_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='서비스 내에서 이루어지는 신고 유형 정보를 저장하는 테이블';

-- 2) 신고 이력 테이블 생성
CREATE TABLE `report_history` (
    `report_id`            INT          NOT NULL AUTO_INCREMENT COMMENT '신고 ID',
    `reporter_member_id`   INT          NOT NULL COMMENT '신고자 ID',
    `target_member_id`     INT          NOT NULL COMMENT '신고 대상자 ID',
    `post_id`              INT                   DEFAULT NULL COMMENT '게시글 ID',
    `comment_id`           BIGINT                DEFAULT NULL COMMENT '댓글 ID',
    `report_type_id`       TINYINT      NOT NULL COMMENT '신고 유형 ID',
    `status_id`            INT          NOT NULL COMMENT '상태 ID',
    `reason`               TEXT         NOT NULL COMMENT '신고 사유',
    `created_at`           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '신고 일시',
    PRIMARY KEY (`report_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='회원이 회원, 게시글, 댓글을 신고한 이력 테이블';

-- 3) 사용자 제재 이력 테이블 생성
CREATE TABLE `user_penalty_history` (
    `penalty_id`     INT              NOT NULL AUTO_INCREMENT COMMENT '제재 ID',
    `user_id`      	INT                NOT NULL COMMENT '사용자 ID',
    `post_id`        INT                       DEFAULT NULL COMMENT '게시글 ID',
    `comment_id`     BIGINT                    DEFAULT NULL COMMENT '댓글 ID',
    `review_id`      INT                       DEFAULT NULL COMMENT '장소 후기 ID',
    `status_id`      INT              NOT NULL COMMENT '상태 ID',
    `penalty_type`   ENUM('POST', 'COMMENT', 'REVIEW') NOT NULL COMMENT '제재 유형',
    `reason`         TEXT             NOT NULL COMMENT '사유',
    `created_at`     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '제재 일시',
    PRIMARY KEY (`penalty_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='신고 혹은 관리자의 판단 하에 발생한 사용자 제재 이력 저장 테이블';


-- 4) 이의 제기 테이블 생성
CREATE TABLE `objection` (
    `objection_id`   INT        NOT NULL AUTO_INCREMENT COMMENT '이의 제기 ID',
    `penalty_id`     INT        NOT NULL COMMENT '제재 ID',
    `member_id`      INT        NOT NULL COMMENT '제기자 ID',
    `status_id`      INT        NOT NULL COMMENT '상태 ID',
    `reason`         TEXT       NOT NULL COMMENT '이의 제기 사유',
    `created_at`     DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '이의 제기 일시',
    `processed_at`   DATETIME            DEFAULT NULL COMMENT '처리 완료 일시',
    PRIMARY KEY (`objection_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자가 제재에 대해 이의를 제기한 이력 테이블';

-- 5) 블랙리스트 테이블 생성
CREATE TABLE `blacklist` (
    `member_id`   INT            NOT NULL COMMENT '사용자 ID',
    `reason`      VARCHAR(255)   NOT NULL COMMENT '블랙리스트 등록 사유',
    `created_at`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    PRIMARY KEY (`member_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='서비스 이용이 제한된 사용자 목록 테이블';






-- -------------------------------------------------------------------------------------------------------------------------

-- 인증토큰 테이블 생성
CREATE TABLE `verification_token` (
    `user_id` 		INT 				NOT NULL COMMENT '토큰 생성 요청자 ID',
    `email` 		VARCHAR(255) 	NOT NULL COMMENT '인증을 요청한 이메일 주소',
    `code` 			VARCHAR(20) 	NOT NULL COMMENT '인증 코드',
    `expiry_date` DATETIME 		NOT NULL COMMENT '인증 코드 만료 시간',
    `token_type` 	VARCHAR(30) 				DEFAULT 'SIGNUP' COMMENT '토큰 종류(SIGNUP, RESET 등)',
    
    PRIMARY KEY (`user_id`),
    CONSTRAINT fk_verification_user
        FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE
) COMMENT = '이메일 인증용 토큰 저장 테이블';							


-- 리프레시토큰 테이블 생성
CREATE TABLE `refresh_token` (
    `user_id` 		INT 				NOT NULL COMMENT '토큰 생성 요청자 ID',
    `user_email` 	VARCHAR(255) 	NOT NULL COMMENT '리프레시 토큰을 발급받은 유저 이메일',
    `token` 		TEXT 				NOT NULL COMMENT '리프레시 토큰 문자열',
    `expiry_date` DATETIME 		NOT NULL COMMENT '리프레시 토큰 만료 시간',

    PRIMARY KEY (`user_id`),
    CONSTRAINT fk_refresh_user_email
        FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE
) COMMENT = 'Refresh Token 저장 테이블';






--  ------------------------------------------------------------------------------------------------------------------------
-- 2. 테이블 참조 조건 추가

-- 1-1. 기초 테이블 참조 조건 없음
-- 1) 상태 테이블 참조 조건 없음
-- 2) 권한 테이블 참조 조건 없음
-- 3) 운동 종목 테이블 참조 조건 없음

-- 1-2. 사용자 관련 테이블 참조 조건 추가
-- 1) 사용자 테이블 참조 조건 추가
-- 권한 ID 외래 키 제약
ALTER TABLE `user`
    ADD CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_id`) 
    REFERENCES `role`(`role_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약
ALTER TABLE `user`
    ADD CONSTRAINT `fk_user_status`
    FOREIGN KEY (`status_id`) 
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;


-- 2) 회원 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (1:1 관계)
ALTER TABLE `member`
    ADD CONSTRAINT `fk_member_user`
    FOREIGN KEY (`member_id`) 
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 3) 사업자 테이블 참조 조건 추가
-- 사업자 ID 외래키 제약 (1:1 관계)
ALTER TABLE `owner`
    ADD CONSTRAINT `fk_owner_user`
    FOREIGN KEY (`owner_id`) 
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약
ALTER TABLE `owner`
    ADD CONSTRAINT `fk_owner_status`
    FOREIGN KEY (`status_id`) 
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 4) 친구 관계 테이블 참조 조건 추가
-- 신청자 ID 외래 키 제약
ALTER TABLE `friend`
    ADD CONSTRAINT `fk_friend_requester`
    FOREIGN KEY (`requester_id`) 
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 피요청자 ID 외래 키 제약
ALTER TABLE `friend`
    ADD CONSTRAINT `fk_friend_addressee`
    FOREIGN KEY (`addressee_id`) 
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 상태 ID  외래 키 제약
ALTER TABLE `friend`
    ADD CONSTRAINT `fk_friend_status`
    FOREIGN KEY (`status_id`) 
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 5) 선호 운동 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약
ALTER TABLE `user_sport`
    ADD CONSTRAINT `fk_user_sport_member`
    FOREIGN KEY (`member_id`) 
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 운동 종목 ID 외래 키 제약
ALTER TABLE `user_sport`
    ADD CONSTRAINT `fk_user_sport_sport_type`
    FOREIGN KEY (`sport_id`) 
    REFERENCES `sport_type`(`sport_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;





-- 1-3. 포인트 테이블 참조 조건 추가
-- 1) 계좌 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (user 테이블 참조, 계정 삭제 시 계좌도 삭제됨)
ALTER TABLE `account`
    ADD CONSTRAINT `fk_account_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE; -- 사용자 정보 수정 시 계좌도 반영

-- 상태 ID 외래 키 제약 (status 테이블 참조, 상태 삭제 제한)
ALTER TABLE `account`
    ADD CONSTRAINT `fk_account_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE; -- 상태 코드 수정 시 연동

-- 2) 포인트 거래 내역 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `point_transaction`
    ADD CONSTRAINT `fk_point_transaction_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 3) 정산 테이블 참조 조건 추가
-- 사업자 외래 키 제약조건
ALTER TABLE settlement
    ADD CONSTRAINT fk_settlement_owner
        FOREIGN KEY (owner_id)
        REFERENCES owner(owner_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;



-- 1-4. 장소 관련 테이블 참조 조건 추가
-- 1) 장소 테이블 참조 조건 추가
-- 사업자 ID 외래 키 제약
ALTER TABLE `place`
    ADD CONSTRAINT `fk_place_owner`
    FOREIGN KEY (`owner_id`)
    REFERENCES `owner`(`owner_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 운동 종목 ID 외래 키 제약
ALTER TABLE `place`
    ADD CONSTRAINT `fk_place_sport_type`
    FOREIGN KEY (`sport_id`)
    REFERENCES `sport_type`(`sport_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 2) 장소 사진 테이블 참조 조건 추가
-- 장소 ID 외래 키 제약 (place 테이블 참조)
ALTER TABLE `place_image`
    ADD CONSTRAINT `fk_place_image_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 3) 운영 시간 테이블 참조 조건 추가
-- 장소 ID 외래 키 제약 (place 테이블 참조)
ALTER TABLE `operation_time`
    ADD CONSTRAINT `fk_operation_time_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 4) 장소 즐겨찾기 테이블 참조 조건 추가
-- 장소 ID 외래 키 제약 (place 테이블 참조)
ALTER TABLE `favorite`
    ADD CONSTRAINT `fk_favorite_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `favorite`
    ADD CONSTRAINT `fk_favorite_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;





-- 1-5. 모임 관련 테이블 참조 조건 추가
-- 1) 모임 테이블 참조 조건 추가
-- 개설자 ID → member 테이블
ALTER TABLE `meeting`
    ADD CONSTRAINT `fk_meeting_leader`
    FOREIGN KEY (`leader_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 장소 ID → place 테이블 (옵션, NULL 가능)
ALTER TABLE `meeting`
    ADD CONSTRAINT `fk_meeting_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE SET NULL
    ON UPDATE RESTRICT;

-- 운동 ID → sport_type 테이블
ALTER TABLE `meeting`
    ADD CONSTRAINT `fk_meeting_sport`
    FOREIGN KEY (`sport_id`)
    REFERENCES `sport_type`(`sport_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 상태 ID → status 테이블
ALTER TABLE `meeting`
    ADD CONSTRAINT `fk_meeting_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 2) 모임 찜 테이블 참조 조건 추가
-- 모임 ID 외래 키 제약 (RESTRICT)
ALTER TABLE `interested_meeting`
    ADD CONSTRAINT `fk_interested_meeting_meeting`
    FOREIGN KEY (`meeting_id`)
    REFERENCES `meeting`(`meeting_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 사용자 ID 외래 키 제약 (CASCADE 삭제)
ALTER TABLE `interested_meeting`
    ADD CONSTRAINT `fk_interested_meeting_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT;

-- 3) 모임 참여 이력 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `meeting_participation_history`
    ADD CONSTRAINT `fk_participation_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT;

-- 모임 ID 외래 키 제약 (meeting 테이블 참조)
ALTER TABLE `meeting_participation_history`
    ADD CONSTRAINT `fk_participation_meeting`
    FOREIGN KEY (`meeting_id`)
    REFERENCES `meeting`(`meeting_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `meeting_participation_history`
    ADD CONSTRAINT `fk_participation_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 4) 장소 예약 테이블 참조 조건 추가
-- 장소 ID 외래 키 제약 (place 테이블 참조)
ALTER TABLE `reservation`
    ADD CONSTRAINT `fk_reservation_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 모임 ID 외래 키 제약 (meeting 테이블 참조, NULL 허용)
ALTER TABLE `reservation`
    ADD CONSTRAINT `fk_reservation_meeting`
    FOREIGN KEY (`meeting_id`)
    REFERENCES `meeting`(`meeting_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `reservation`
    ADD CONSTRAINT `fk_reservation_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;





-- 1-6. 후기 및 평가 테이블 참조 조건 추가
-- 1) 참가자 평가 테이블 참조 조건 추가
-- 평가자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `participant_review`
    ADD CONSTRAINT `fk_participant_review_reviewer`
    FOREIGN KEY (`reviewer_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 평가 대상자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `participant_review`
    ADD CONSTRAINT `fk_participant_review_reviewee`
    FOREIGN KEY (`reviewee_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT;

-- 모임 ID 외래 키 제약 (meeting 테이블 참조)
ALTER TABLE `participant_review`
    ADD CONSTRAINT `fk_participant_review_meeting`
    FOREIGN KEY (`meeting_id`)
    REFERENCES `meeting`(`meeting_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 2) 장소 후기 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `place_review`
    ADD CONSTRAINT `fk_place_review_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 장소 ID 외래 키 제약 (place 테이블 참조)
ALTER TABLE `place_review`
    ADD CONSTRAINT `fk_place_review_place`
    FOREIGN KEY (`place_id`)
    REFERENCES `place`(`place_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 모임 참여 이력 ID 외래 키 제약 (meeting_participation_history 테이블 참조)
ALTER TABLE `place_review`
    ADD CONSTRAINT `fk_place_review_participation`
    FOREIGN KEY (`participation_id`)
    REFERENCES `meeting_participation_history`(`participation_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
    
-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `place_review`
    ADD CONSTRAINT `fk_place_review_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 3) 베스트 플레이어 테이블 참조 조건 추가
-- 모임 ID 외래 키 제약 (meeting 테이블 참조)
ALTER TABLE `best_player`
    ADD CONSTRAINT `fk_best_player_meeting`
    FOREIGN KEY (`meeting_id`)
    REFERENCES `meeting`(`meeting_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 사용자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `best_player`
    ADD CONSTRAINT `fk_best_player_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT;






-- 1-7. 커뮤니티 테이블 참조 조건 추가
-- 1) 게시글 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `community_post`
    ADD CONSTRAINT `fk_community_post_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 2) 게시글 좋아요 테이블 참조 조건 추가
-- 게시글 ID 외래 키 제약 (community_post 테이블 참조)
ALTER TABLE `post_like`
    ADD CONSTRAINT `fk_post_like_post`
    FOREIGN KEY (`post_id`)
    REFERENCES `community_post`(`post_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `post_like`
    ADD CONSTRAINT `fk_post_like_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 3) 게시글 사진 테이블 참조 조건 추가
-- 게시글 ID 외래 키 제약 (community_post 테이블 참조)
ALTER TABLE `post_image`
    ADD CONSTRAINT `fk_post_image_post`
    FOREIGN KEY (`post_id`)
    REFERENCES `community_post`(`post_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 4) 댓글 테이블 참조 조건 추가
-- 게시글 ID 외래 키 제약 (community_post 테이블 참조)
ALTER TABLE `community_comment`
    ADD CONSTRAINT `fk_comment_post`
    FOREIGN KEY (`post_id`)
    REFERENCES `community_post`(`post_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `community_comment`
    ADD CONSTRAINT `fk_comment_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 5) 댓글 좋아요 테이블 참조 조건 추가
-- 댓글 ID 외래 키 제약 (community_comment 테이블 참조)
ALTER TABLE `comment_like`
    ADD CONSTRAINT `fk_comment_like_comment`
    FOREIGN KEY (`comment_id`)
    REFERENCES `community_comment`(`comment_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `comment_like`
    ADD CONSTRAINT `fk_comment_like_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;






-- 1-8.  알림 테이블 참조 조건 추가
-- 1) 알림 유형 테이블 참조 조건 없음

-- 2) 알림 설정 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (user 테이블 참조)
ALTER TABLE `notification_setting`
    ADD CONSTRAINT `fk_notification_setting_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 알림 유형 ID 외래 키 제약 (notification_type 테이블 참조)
ALTER TABLE `notification_setting`
    ADD CONSTRAINT `fk_notification_setting_type`
    FOREIGN KEY (`notification_type_id`)
    REFERENCES `notification_type`(`notification_type_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 3) 도메인 타입 테이블 참조 조건 없음

-- 4) 알림 테이블 참조 조건 추가
-- 수신자 ID 외래 키 제약
ALTER TABLE `notification`
    ADD CONSTRAINT `fk_notification_user`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 도메인 타입 ID 외래 키 제약
ALTER TABLE `notification`
    ADD CONSTRAINT `fk_notification_domain_type`
    FOREIGN KEY (`domain_type_id`)
    REFERENCES `domain_type`(`domain_type_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 알림 유형 ID 외래 키 제약
ALTER TABLE `notification`
    ADD CONSTRAINT `fk_notification_type`
    FOREIGN KEY (`notification_type_id`)
    REFERENCES `notification_type`(`notification_type_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;






-- 1-9. 신고 및 제재 테이블 참조 조건 추가
-- 1) 신고 유형 테이블 참조 조건 없음

-- 2) 신고 이력 테이블 참조 조건 추가
-- 신고자 ID 외래 키 제약
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_reporter`
    FOREIGN KEY (`reporter_member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 신고 대상자 ID 외래 키 제약
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_target`
    FOREIGN KEY (`target_member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 게시글 ID 외래 키 제약
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_post`
    FOREIGN KEY (`post_id`)
    REFERENCES `community_post`(`post_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 댓글 ID 외래 키 제약
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_comment`
    FOREIGN KEY (`comment_id`)
    REFERENCES `community_comment`(`comment_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 신고 유형 ID 외래 키 제약
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_type`
    FOREIGN KEY (`report_type_id`)
    REFERENCES `report_type`(`report_type_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `report_history`
    ADD CONSTRAINT `fk_report_history_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;


-- 3) 사용자 제재 이력 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약
ALTER TABLE `user_penalty_history`
    ADD CONSTRAINT `fk_penalty_member`
    FOREIGN KEY (`user_id`)
    REFERENCES `user`(`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 게시글 ID 외래 키 제약
ALTER TABLE `user_penalty_history`
    ADD CONSTRAINT `fk_penalty_post`
    FOREIGN KEY (`post_id`)
    REFERENCES `community_post`(`post_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 댓글 ID 외래 키 제약
ALTER TABLE `user_penalty_history`
    ADD CONSTRAINT `fk_penalty_comment`
    FOREIGN KEY (`comment_id`)
    REFERENCES `community_comment`(`comment_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 장소 후기 ID 외래 키 제약
ALTER TABLE `user_penalty_history`
    ADD CONSTRAINT `fk_penalty_review`
    FOREIGN KEY (`review_id`)
    REFERENCES `place_review`(`review_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `user_penalty_history`
    ADD CONSTRAINT `fk_penalty_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

-- 4) 이의 제기 테이블 참조 조건 추가
-- 제재 ID 외래 키 제약 (user_penalty_history 테이블 참조)
ALTER TABLE `objection`
    ADD CONSTRAINT `fk_objection_penalty`
    FOREIGN KEY (`penalty_id`)
    REFERENCES `user_penalty_history`(`penalty_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 제기자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `objection`
    ADD CONSTRAINT `fk_objection_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- 상태 ID 외래 키 제약 (status 테이블 참조)
ALTER TABLE `objection`
    ADD CONSTRAINT `fk_objection_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `status`(`status_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;


-- 5) 블랙리스트 테이블 참조 조건 추가
-- 사용자 ID 외래 키 제약 (member 테이블 참조)
ALTER TABLE `blacklist`
    ADD CONSTRAINT `fk_blacklist_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `member`(`member_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;


