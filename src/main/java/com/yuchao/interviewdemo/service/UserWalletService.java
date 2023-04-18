package com.yuchao.interviewdemo.service;

import com.yuchao.interviewdemo.entity.UserWallet;
import com.yuchao.interviewdemo.entity.WalletTransaction;
import com.yuchao.interviewdemo.mapper.UserWalletMapper;
import com.yuchao.interviewdemo.mapper.WalletTransactionMapper;
import com.yuchao.interviewdemo.util.Constants;
import com.yuchao.interviewdemo.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  15:32
 */
@Service
public class UserWalletService implements Constants {

    @Resource
    private UserWalletMapper userWalletMapper;

    @Resource
    private WalletTransactionMapper walletTransactionMapper;

    public UserWallet findUserWallet(Long userId) {
        return userWalletMapper.selectUserWalletById(userId);
    }


    public BigDecimal findUserWalletBalance(Long userId) {
        return userWalletMapper.selectBalanceById(userId);
    }

    @Transactional
    public Result purchase(Long userId, BigDecimal amount) {
        UserWallet userWallet = userWalletMapper.selectUserWalletById(userId);
        if (userWallet == null) {
            return Result.error(Constants.CODE_600, "User wallet not found");
        }

        if (userWallet.getBalance().compareTo(amount) < 0) {
            return Result.error(Constants.CODE_600, "余额不足");
        }
        BigDecimal newBalance = userWallet.getBalance().subtract(amount);
        userWallet.setBalance(newBalance);
        userWalletMapper.updateBalanceById(userId, newBalance);

        WalletTransaction walletTransaction = new WalletTransaction(null, userId, Constants.PURCHASE, amount, newBalance, new Date());
        walletTransactionMapper.insertWalletTransaction(walletTransaction);
        return Result.success();
    }

    @Transactional
    public Result refund(Long userId, BigDecimal amount) {
        UserWallet userWallet = userWalletMapper.selectUserWalletById(userId);
        if (userWallet == null) {
            return Result.error(Constants.CODE_600, "User wallet not found");
        }
        BigDecimal newBalance = userWallet.getBalance().add(amount);
        userWallet.setBalance(newBalance);
        userWalletMapper.updateBalanceById(userId, newBalance);
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, userId, Constants.REFUND, amount, newBalance, new Date()));
        return Result.success();
    }

    public List<WalletTransaction> findWalletTransactions(Long userId, String type, Date startTime, Date endTime) {
        List<WalletTransaction> list = walletTransactionMapper.selectWalletTransactionByDate(userId, type,startTime, endTime);
        return list;
    }

}
