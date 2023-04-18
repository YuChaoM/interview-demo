package com.yuchao.interviewdemo.mapper;

import com.yuchao.interviewdemo.entity.UserWallet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  12:46
 */
public interface UserWalletMapper {

    UserWallet selectUserWalletById(Long userId);

    int insertUserWallet(UserWallet userWallet);

    // 查询钱包余额
    BigDecimal selectBalanceById(Long userId);

    // 更新钱余额
    void updateBalanceById(@Param("userId") Long userId,@Param("newBalance") BigDecimal newBalance);


}
