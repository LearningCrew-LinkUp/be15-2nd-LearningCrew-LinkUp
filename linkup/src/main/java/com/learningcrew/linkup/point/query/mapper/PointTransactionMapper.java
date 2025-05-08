package com.learningcrew.linkup.point.query.mapper;

import com.learningcrew.linkup.point.query.dto.query.SettlementTransactionSearchCondition;
import com.learningcrew.linkup.point.query.dto.query.PointTransactionSearchCondition;
import com.learningcrew.linkup.point.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PointTransactionMapper {
    List<PointHistoryResponse> findPointHistoriesByUserId(int userId);

    MonthlySettlementResponse findMonthlySettlementAmount(int ownerId, int year, int month);


    List<PointTransactionResponse> findPointTransactions(PointTransactionSearchCondition condition);

    List<UserPointTransactionResponse> findUserPointTransactions(Map<String, Object> params);

    SettlementHistory findSettlementByOwnerId(int userId);

    List<SettlementHistory> findSettlements(SettlementTransactionSearchCondition condition);
}
