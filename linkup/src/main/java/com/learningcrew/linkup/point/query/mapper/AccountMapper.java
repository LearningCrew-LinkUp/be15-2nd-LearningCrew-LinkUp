package com.learningcrew.linkup.point.query.mapper;

import com.learningcrew.linkup.point.query.dto.query.AccountFindDto;
import com.learningcrew.linkup.point.query.dto.query.AccountSearchCondition;
import com.learningcrew.linkup.point.query.dto.response.AccountHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {
    Optional<AccountFindDto> findByUserId(int userId);

    List<AccountHistory> findAccounts(AccountSearchCondition condition);
}
