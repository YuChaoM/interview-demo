package com.yuchao.interviewdemo.service;

import com.yuchao.interviewdemo.entity.UserWallet;
import com.yuchao.interviewdemo.entity.WalletTransaction;
import com.yuchao.interviewdemo.mapper.UserWalletMapper;
import com.yuchao.interviewdemo.mapper.WalletTransactionMapper;
import com.yuchao.interviewdemo.util.Constants;
import com.yuchao.interviewdemo.util.Result;
import org.springframework.scheduling.annotation.Scheduled;
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

    // 查询余额
    public BigDecimal findUserWalletBalance(Long userId) {
        return userWalletMapper.selectBalanceById(userId);
    }

    // 消费
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

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setUserId(userId);
        walletTransaction.setTransactionType(Constants.PURCHASE); // 消费记录
        walletTransaction.setAmount(amount);
        walletTransaction.setBalance(newBalance);
        walletTransaction.setStatus(0); // 不是退款的交易记录设置为0
        walletTransaction.setExpired(new Date());
        walletTransactionMapper.insertWalletTransaction(walletTransaction);
        return Result.success();
    }

    // 退款
    @Transactional
    public Result refund(Long userId, BigDecimal amount) {
        UserWallet userWallet = userWalletMapper.selectUserWalletById(userId);
        if (userWallet == null) {
            return Result.error(Constants.CODE_600, "User wallet not found");
        }
        // 添加一条退款交易记录
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setUserId(userId);
        walletTransaction.setTransactionType(Constants.REFUND);
        walletTransaction.setAmount(amount); // 退款金额
        walletTransaction.setBalance(userWallet.getBalance());//钱包余额
        walletTransaction.setStatus(Constants.PENDING); // 待处理状态
        walletTransaction.setExpired(new Date(System.currentTimeMillis() + 3 * 24 * 3600 * 1000l)); //三天过期
        walletTransaction.setCreatedTime(new Date());
        walletTransactionMapper.insertWalletTransaction(walletTransaction);

        /*
         * 商家同意退款后，将该交易记录的状态更新为APPROVED(批准)，并新增一条类型为DEPOSIT（充值），
         * 状态为APPROVED的交易记录，表示将退款金额转入用户的钱包中，钱包余额增加
         */

        return Result.success();
    }

    // 查询用户钱包金额变动明细
    public List<WalletTransaction> findWalletTransactions(Long userId, String type, Date startTime, Date endTime) {
        List<WalletTransaction> list = walletTransactionMapper.selectWalletTransactionByDate(userId, type, startTime, endTime);
        return list;
    }

    /**
     * 定时任务处理商家超时未处理的退款交易记录，自动退款
     */
    @Scheduled(initialDelay = 10000, fixedRate = 30 * 60 * 1000)
    @Transactional
    public void execute() {
        List<WalletTransaction> list = walletTransactionMapper.selectWalletTransactionByStatus(Constants.REFUND, Constants.PENDING);
        for (WalletTransaction transaction : list) {
            if (transaction.getExpired().before(new Date())) {
                // 同意退款
                walletTransactionMapper.updateWalletTransactionStatus(transaction.getId(), transaction.getUserId(), Constants.APPROVED);
                // 更新钱包余额
                userWalletMapper.updateBalanceById(transaction.getUserId(), transaction.getAmount());
                // 新增一条充值记录
                WalletTransaction walletTransaction = new WalletTransaction();
                walletTransaction.setUserId(transaction.getUserId());
                walletTransaction.setTransactionType(Constants.DEPOSIT);
                walletTransaction.setAmount(transaction.getAmount());
                walletTransaction.setBalance(transaction.getBalance().add(transaction.getAmount()));//钱包余额
                walletTransaction.setStatus(0);
                walletTransaction.setExpired(new Date());
                walletTransaction.setCreatedTime(new Date());
                walletTransactionMapper.insertWalletTransaction(walletTransaction);

            }
        }
    }

}
