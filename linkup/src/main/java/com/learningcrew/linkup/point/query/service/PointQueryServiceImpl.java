package com.learningcrew.linkup.point.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
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
    public List<SettlementDetailResponse> getAllSettlements(int userId) {
        return pointTransactionMapper.findAllByOwnerId(userId);
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
    public PageResponse<UserPointTransactionResponse> getMyPointTransactions(int userId, YearMonth yearMonth, String transactionType, Pageable pageable) {

        /* 조건 파라미터 세팅 */
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("transactionType", transactionType);
        if (yearMonth != null) {
            params.put("year", yearMonth.getYear());
            params.put("month", yearMonth.getMonthValue());
        }

        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<UserPointTransactionResponse> result = pointTransactionMapper.findUserPointTransactions(params);
        PageInfo<UserPointTransactionResponse> pageInfo = new PageInfo<>(result);

        return PageResponse.from(pageInfo);
    }


}
