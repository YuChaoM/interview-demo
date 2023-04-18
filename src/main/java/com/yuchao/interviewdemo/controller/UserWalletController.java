package com.yuchao.interviewdemo.controller;

import com.yuchao.interviewdemo.entity.WalletTransaction;
import com.yuchao.interviewdemo.service.UserWalletService;

import com.yuchao.interviewdemo.util.Result;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  16:00
 */
@RestController
public class UserWalletController {

    @Resource
    private UserWalletService userWalletService;

    @GetMapping("/balance")
    public Result getUserWalletBalance(@RequestParam("userId") Long userId) {
        BigDecimal balance = userWalletService.findUserWalletBalance(userId);
        return Result.success(balance);
    }

    @PostMapping("/purchase")
    public Result purchase(@RequestParam("userId") Long userId,
                           @RequestParam("amount") BigDecimal amount) {

        return userWalletService.purchase(userId, amount);
    }

    @PostMapping("/refund")
    public Result refund(@RequestParam("userId") Long userId,
                         @RequestParam("amount") BigDecimal amount) {
        return userWalletService.refund(userId, amount);
    }

    @GetMapping("/transactions")
    public Result getWalletTransactions(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {

        List<WalletTransaction> list = userWalletService.findWalletTransactions(userId, type, start, end);
        return Result.success(list);
    }
}
