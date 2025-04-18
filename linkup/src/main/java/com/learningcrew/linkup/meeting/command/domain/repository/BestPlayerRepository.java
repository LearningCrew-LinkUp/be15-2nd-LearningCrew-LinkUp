package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayer;

public interface BestPlayerRepository {
    BestPlayer save(BestPlayer bestPlayer);
}
