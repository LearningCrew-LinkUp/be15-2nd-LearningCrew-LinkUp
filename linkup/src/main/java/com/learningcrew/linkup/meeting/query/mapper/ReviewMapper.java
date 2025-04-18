package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    List<ParticipantReviewDTO> selectAllParticipantReviews();

    List<ParticipantReviewDTO> selectReviewsByReviewer(int memberId);
    List<ParticipantReviewDTO> selectReviewsByReviewee(int memberId);

}
