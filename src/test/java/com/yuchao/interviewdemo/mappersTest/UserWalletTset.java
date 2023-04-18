package com.yuchao.interviewdemo.mappersTest;

import com.yuchao.interviewdemo.entity.UserWallet;
import com.yuchao.interviewdemo.mapper.UserWalletMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Date;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  13:16
 */
@SpringBootTest
public class UserWalletTset {

    @Resource
    private UserWalletMapper userWalletMapper;

    @Test
    public void testInsertUserWallet() {
//        userWalletMapper.insertUserWallet(new UserWallet(null, 1001l, new BigDecimal(1001), new Date(), new Date()));
//        userWalletMapper.insertUserWallet(new UserWallet(null, 1002l, new BigDecimal(1001), new Date(), new Date()));
//        userWalletMapper.insertUserWallet(new UserWallet(null, 1003l, new BigDecimal(1001), new Date(), new Date()));
//        userWalletMapper.insertUserWallet(new UserWallet(null, 1004l, new BigDecimal(1001), new Date(), new Date()));
//        userWalletMapper.insertUserWallet(new UserWallet(null, 1005l, new BigDecimal(1001), new Date(), new Date()));
        UserWallet userWallet = new UserWallet(null, 1007l, new BigDecimal(300), new Date(), new Date());
        userWalletMapper.insertUserWallet(userWallet);
        System.out.println(userWallet);

    }

    @Test
    public void testSelectUser() {
        System.out.println(userWalletMapper.selectBalanceById(1001l));
        System.out.println(userWalletMapper.selectUserWalletById(1001l));
    }

    @Test
    public void testUpdateBalance() {
        userWalletMapper.updateBalanceById(1005l,new BigDecimal(300));
        System.out.println(userWalletMapper.selectBalanceById(1005l));
    }


}
