package com.learningcrew.linkup.point.query.service;

import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.point.query.dto.query.PointTransactionSearchCondition;
import com.learningcrew.linkup.point.query.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface PointQueryService {
    AccountResponse getAccount(int userId);

    List<PointHistoryResponse> getPointHistory(int i);

    List<SettlementDetailResponse> getAllSettlements(int i);

    MonthlySettlementResponse getMonthlySettlement(int i, int year, int month);

    PageResponse<PointTransactionResponse> getUsersPointTransactions(PointTransactionSearchCondition condition, Pageable pageable);

    PageResponse<UserPointTransactionResponse> getMyPointTransactions(int i, YearMonth yearMonth, String transactionType, Pageable pageable);
}