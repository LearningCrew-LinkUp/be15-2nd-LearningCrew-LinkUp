package com.learningcrew.linkupuser.command.domain.aggregate;

import ch.qos.logback.core.util.StringUtil;

import com.learningcrew.linkupuser.command.application.dto.ProfileUpdateRequest;
import com.learningcrew.linkupuser.command.domain.constants.LinkerGender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "UserMember")
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    private int memberId;

    @Enumerated(EnumType.STRING)
    private LinkerGender gender;

    private String nickname;

    private LocalDate birthDate;

    private String introduction;
    private BigDecimal mannerTemperature = BigDecimal.ZERO;
    private String profileImageUrl;

    public void setProfileImageUrl(String defaultProfileImage) {
        this.profileImageUrl = defaultProfileImage;
    }

    public void setMemberId(int userId) {
        this.memberId = userId;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public void setMannerTemperature(BigDecimal mannerTemperature) {
        this.mannerTemperature = mannerTemperature;
    }

    public void updateProfile(ProfileUpdateRequest request){
        if(StringUtils.hasText(request.getNickname())){
            this.nickname = request.getNickname();
        }
        if(StringUtils.hasText(request.getProfileImageUrl())){
            this.profileImageUrl = request.getProfileImageUrl();
        }
        if(StringUtils.hasText(request.getIntroduction())){
            this.introduction = request.getIntroduction();
        }
    }
}
