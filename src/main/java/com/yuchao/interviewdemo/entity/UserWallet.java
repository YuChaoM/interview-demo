package com.yuchao.interviewdemo.entity;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  11:40
 */
public class UserWallet {

    private Long id;
    private Long userId;
    private BigDecimal balance;
    private Date createdTime;
    private Date updatedTime;

    public UserWallet() {
    }

    public UserWallet(Long id, Long userId, BigDecimal balance, Date createdTime, Date updatedTime) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdateTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "UserWallet{" +
                "id=" + id +
                ", user_id=" + userId +
                ", balance=" + balance +
                ", createTime=" + createdTime +
                ", updateTime=" + updatedTime +
                '}';
    }
}
