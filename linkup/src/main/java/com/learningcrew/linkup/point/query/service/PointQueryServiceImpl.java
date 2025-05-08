package com.learningcrew.linkup.point.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.point.query.dto.query.AccountSearchCondition;
import com.learningcrew.linkup.point.query.dto.query.SettlementTransactionSearchCondition;
import com.learningcrew.linkup.point.query.dto.query.PointTransactionSearchCondition;
import com.learningcrew.linkup.point.query.dto.query.AccountFindDto;
import com.learningcrew.linkup.point.query.dto.response.*;
import com.learningcrew.linkup.point.query.mapper.AccountMapper;
import com.learningcrew.linkup.point.query.mapper.PointTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PointQueryServiceImpl implements PointQueryService {

    private final AccountMapper accountMapper;
    private final PointTransactionMapper pointTransactionMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(int userId) {
        AccountFindDto account = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        return AccountResponse.builder()
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .build();
    }

    @Override
    public List<PointHistoryResponse> getPointHistory(int userId) {
        return pointTransactionMapper.findPointHistoriesByUserId(userId);
    }

    @Override
    public SettlementHistory getSettlement(int userId) {
        return pointTransactionMapper.findSettlementByOwnerId(userId);
    }

    @Override
    public PageResponse<SettlementHistory> getSettlements(SettlementTransactionSearchCondition condition, Pageable pageable) {
        System.out.println("userId" + condition.getUserId());
        System.out.println("useEnd" + condition.getEndDate());
        System.out.println("useStart" + condition.getStartDate());
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<SettlementHistory> transactions = pointTransactionMapper.findSettlements(condition);
        PageInfo<SettlementHistory> pageInfo = new PageInfo<>(transactions);
        return PageResponse.from(pageInfo);
    }

    @Override
    public MonthlySettlementResponse getMonthlySettlement(int ownerId, int year, int month) {
        return pointTransactionMapper.findMonthlySettlementAmount(ownerId, year, month);
    }

    @Override
    public PageResponse<PointTransactionResponse> getUsersPointTransactions(PointTransactionSearchCondition condition, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<PointTransactionResponse> transactions = pointTransactionMapper.findPointTransactions(condition);
        PageInfo<PointTransactionResponse> pageInfo = new PageInfo<>(transactions);
        return PageResponse.from(pageInfo);
    }

    @Override
    public PageResponse<UserPointTransactionResponse> getMyPointTransactions(int userId, String yearMonth, String transactionType, Pageable pageable) {

        YearMonth ym = null;
        if (yearMonth != null && !yearMonth.isBlank()) {
            ym = YearMonth.parse(yearMonth); // "2025-04"를 바로 YearMonth로 변환
        }

        /* 조건 파라미터 세팅 */
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("transactionType", transactionType);
        if (yearMonth != null) {
            params.put("year", ym.getYear());
            params.put("month", ym.getMonthValue());
        }

        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<UserPointTransactionResponse> result = pointTransactionMapper.findUserPointTransactions(params);
        PageInfo<UserPointTransactionResponse> pageInfo = new PageInfo<>(result);

        return PageResponse.from(pageInfo);
    }

    @Override
    public PageResponse<AccountHistory> getAccounts(AccountSearchCondition condition, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<AccountHistory> transactions = accountMapper.findAccounts(condition);
        PageInfo<AccountHistory> pageInfo = new PageInfo<>(transactions);
        return PageResponse.from(pageInfo);
    }
}