package com.yuchao.interviewdemo.mapper;

import com.yuchao.interviewdemo.entity.WalletTransaction;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  14:43
 */
public interface WalletTransactionMapper {

    int insertWalletTransaction(WalletTransaction walletTransaction);

    List<WalletTransaction> selectWalletTransactionByDate(@Param("userId") Long userId, @Param("type")String type, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
