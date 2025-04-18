package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository {
    Meeting save(Meeting meeting);

    Optional<Meeting> findById(int meetingId);

    void flush();

    List<Meeting> findAllByDate(LocalDate today);

}
