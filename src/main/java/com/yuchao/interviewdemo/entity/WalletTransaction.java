package com.yuchao.interviewdemo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  11:49
 */
public class WalletTransaction {

    private Long id;
    private Long userId;
    private String transactionType;
    private BigDecimal amount;

    private BigDecimal balance;

    private int status;

    private Date expired;

    private Date createdTime;

    public WalletTransaction() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "WalletTransaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                ", status=" + status +
                ", expired=" + expired +
                ", createdTime=" + createdTime +
                '}';
    }
}
