package com.yuchao.interviewdemo.util;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  15:03
 */
public interface Constants {

    String DEPOSIT = "充值";
    String WITHDRAWAL = "提现";
    String PURCHASE = "消费";
    String REFUND = "退款";

    int PENDING = 1; // 待处理

    int APPROVED = 2; // 以批准

    int REJECTED = 3; // 已拒绝

    String CODE_200 = "200";//成功

    String CODE_500 = "500";//系统错误

    String CODE_600 = "600";//其他业务异常

}
