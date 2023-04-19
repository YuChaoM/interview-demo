业务背景：电商业务中，需要给电商app设计一个用户钱包，用户可以往钱包中充值，购买商品时用户可以使用钱包中的钱消费，商品申请退款成功后钱会退回钱包中，用户也可以申请提现把钱提到银行卡中

用程序实现如下api接口

1. 查询用户钱包余额

1. 用户消费100元的接口
2. 用户退款20元接口
3. 查询用户钱包金额变动明细的接口

请给出建表语句和对应的代码（只要能实现上面api接口要求即可，不相关的表和代码不用写）



考虑到用户申请退款后，不能立即增加钱包的钱，需要商家同意才能增加金额，因此需要两个表。

一个用户钱包表，一个交易记录表。

1. 在钱包交易记录表（WalletTransaction）中新增一个字段status，用于标识交易状态，例如：PENDING（待处理）、APPROVED（已批准）、REJECTED（已拒绝）等。新增expired字段为交易记录的有效期，如果退款超时未处理，认为拒绝退款。
2. 当用户申请退款时，新增一条类型为REFUND（退款），状态为PENDING（待处理）的交易记录。
3. 商家同意退款后，将该交易记录的状态更新为APPROVED（已批准），并新增一条类型为DEPOSIT(充值)，状态为APPROVED（已批准）的交易记录，表示将退款金额转入用户的钱包中。（使用定时任务定期检查所有退款未处理的交易记录，检查商家超时未处理的，自动退款）
4. 如果商家拒绝退款，将该交易记录的状态更新为REJECTED，不会新增任何交易记录，钱包余额不变。



#### 建表

用户钱包表

```sql
CREATE TABLE user_wallet (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  balance DECIMAL(18,2) DEFAULT 0.00,
  created_time DATETIME NOT NULL,
  updated_time DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (user_id)
);
```

交易记录

```sql
CREATE TABLE wallet_transaction (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    status INT,
  	expired timestamp NOT NULL,
    created_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);
```

#### 数据访问层

```Java
public interface UserWalletMapper {

    UserWallet selectUserWalletById(Long userId);

    int insertUserWallet(UserWallet userWallet);

    // 查询钱包余额
    BigDecimal selectBalanceById(Long userId);

    // 更新钱余额
    void updateBalanceById(@Param("userId") Long userId,@Param("newBalance") BigDecimal newBalance);

}
```

```Java
public interface WalletTransactionMapper {

    int insertWalletTransaction(WalletTransaction walletTransaction);

    List<WalletTransaction> selectWalletTransactionByDate(@Param("userId") Long userId, @Param("type")String type, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    // 更新状态
    void updateWalletTransactionStatus(@Param("Id") Long Id,@Param("userId") Long userId,@Param("status") int status);

    // 根据类型和状态查询交易记录
    List<WalletTransaction> selectWalletTransactionByStatus(@Param("type")String type, @Param("status") int status);
}
```



#### service层

```java
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
```



#### Controller层

```java
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
```

