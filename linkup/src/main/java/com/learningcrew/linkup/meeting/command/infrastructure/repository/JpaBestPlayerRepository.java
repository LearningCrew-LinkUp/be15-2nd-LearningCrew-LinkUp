package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayer;
import com.learningcrew.linkup.meeting.command.domain.aggregate.BestPlayerId;
import com.learningcrew.linkup.meeting.command.domain.repository.BestPlayerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaBestPlayerRepository extends BestPlayerRepository, JpaRepository<BestPlayer, BestPlayerId> {
}
