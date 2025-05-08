package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.query.dto.response.ReservedTimeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MeetingQueryRepository {

    @Select("""
        SELECT start_time, end_time
        FROM meeting m
        WHERE m.place_id = #{placeId}
          AND m.date = #{date}
          AND m.status_id IN (1, 2)
        ORDER BY start_time
    """)
    List<ReservedTimeResponse> findReservedTimes(@Param("placeId") int placeId,
                                                 @Param("date") LocalDate date);
}

