package com.yuchao.interviewdemo.mappersTest;

import com.yuchao.interviewdemo.entity.WalletTransaction;
import com.yuchao.interviewdemo.mapper.WalletTransactionMapper;
import com.yuchao.interviewdemo.util.WalletTransactionConstant;
import javafx.print.PageOrientation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2023-04-18  14:52
 */
@SpringBootTest
public class WalletTransactionTest implements WalletTransactionConstant {

    @Resource
    private WalletTransactionMapper walletTransactionMapper;

    @Test
    public void testInsert() {
        WalletTransaction walletTransaction = new WalletTransaction(null, 1001l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date());
        walletTransactionMapper.insertWalletTransaction(walletTransaction);
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1002l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1003l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1004l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1005l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1006l, DEPOSIT, new BigDecimal(1001), new BigDecimal(1001), new Date()));
    }

    @Test
    public void testInsert2() {
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1005l, WITHDRAWAL, new BigDecimal(100), new BigDecimal(901), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1005l, PURCHASE, new BigDecimal(200), new BigDecimal(701), new Date()));
        walletTransactionMapper.insertWalletTransaction(new WalletTransaction(null, 1005l, REFUND, new BigDecimal(200), new BigDecimal(901), new Date()));
    }

    @Test
    public void testSelect() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse("2023-04-10 15:16:54");
        Date endTime = sdf.parse("2023-04-18 15:16:55");

        List<WalletTransaction> list = walletTransactionMapper.selectWalletTransactionByDate(1005l, startTime, endTime);
        for (WalletTransaction walletTransaction : list) {
            System.out.println(walletTransaction);
        }
    }

}
