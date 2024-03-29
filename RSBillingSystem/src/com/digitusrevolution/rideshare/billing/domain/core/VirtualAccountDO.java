package com.digitusrevolution.rideshare.billing.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.AccountDAO;
import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.common.service.NotificationService;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.common.util.RSUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.FinancialTransaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.Purpose;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;
import com.digitusrevolution.rideshare.model.billing.dto.WalletInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

//Need to implement Account Interface which is standard for any AccountDO implementation so that all type of account has same behavior
public class VirtualAccountDO implements DomainObjectPKLong<Account>, AccountDO{

	private Account account;
	private AccountEntity accountEntity;
	private AccountMapper accountMapper;
	private AccountDAO accountDAO;
	private static final Logger logger = LogManager.getLogger(VirtualAccountDO.class.getName());

	public VirtualAccountDO() {
		account = new Account();
		accountEntity = new AccountEntity();
		accountMapper = new AccountMapper();
		accountDAO = new AccountDAO();
	}

	public void setAccount(Account account) {
		this.account = account;
		accountEntity = accountMapper.getEntity(account, true);
	}

	public void setAccountEntity(AccountEntity accountEntity) {
		this.accountEntity = accountEntity;
		account = accountMapper.getDomainModel(accountEntity, false);
	}

	@Override
	public List<Account> getAll() {
		List<Account> accounts = new ArrayList<>();
		List<AccountEntity> accountEntities = accountDAO.getAll();
		for (AccountEntity accountEntity : accountEntities) {
			setAccountEntity(accountEntity);
			accounts.add(account);
		}
		return accounts;
	}

	@Override
	public void update(Account account) {
		if (account.getNumber()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+account.getNumber());
		}
		setAccount(account);
		accountDAO.update(accountEntity);
	}

	@Override
	public void fetchChild() {
		account = accountMapper.getDomainModelChild(account, accountEntity);
	}

	@Override
	public long create(Account account) {
		setAccount(account);
		long id = accountDAO.create(accountEntity);
		return id;
	}

	@Override
	public Account get(long number) {
		accountEntity = accountDAO.get(number);
		if (accountEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setAccountEntity(accountEntity);
		return account;
	}

	@Override
	public Account getAllData(long number) {
		get(number);
		fetchChild();
		return account;
	}

	@Override
	public void delete(long number) {
		account = get(number);
		setAccount(account);
		accountDAO.delete(accountEntity);
	}

	@Override
	public Transaction debit(User user, long accountNumber, float amount, Remark remark){
		//Its important to get child, else old transaction would get deleted as transactions is part of child
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all child before updating
		account = getAllData(accountNumber);
		float balance = getCalculatedBalance(account);
		if (balance >= amount){
			account.setBalance(balance - amount);
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			ZonedDateTime dateTime = DateTimeUtil.getCurrentTimeInUTC();
			transaction.setDateTime(dateTime);
			transaction.setType(TransactionType.Debit);
			transaction.setRemark(remark);
			transaction.setAccount(account);
			TransactionDO transactionDO = new TransactionDO();
			long id = transactionDO.create(transaction);
			transaction.setId(id);
			//This will take care of updating the balance
			//IMP - No need to add transaction in the account again as it would not add transaction entry 
			update(account);
			//Reason for checking that null for user as for company this value is null
			if (user!=null) {
				NotificationService.sendDebitNotification(user, transaction);	
			}
			return transaction;
		} else {
			throw new InSufficientBalanceException("Not enough balance in the account. Current balance is:"+balance);			
		}
	}

	@Override
	public Transaction credit(User user, long accountNumber, float amount, Remark remark){
		//Its important to get child, else old transaction would get deleted as transactions is part of child
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all child before updating
		account = getAllData(accountNumber);
		float balance = getCalculatedBalance(account);
		account.setBalance(balance + amount);
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		ZonedDateTime dateTime = DateTimeUtil.getCurrentTimeInUTC();
		transaction.setDateTime(dateTime);
		transaction.setType(TransactionType.Credit);
		transaction.setRemark(remark);
		transaction.setAccount(account);
		TransactionDO transactionDO = new TransactionDO();
		long id = transactionDO.create(transaction);
		transaction.setId(id);
		//This will take care of updating the balance
		//IMP - No need to add transaction in the account again as it would not add transaction entry
		update(account);
		//Reason for checking that null for user as for company this value is null
		if (user!=null) {
			NotificationService.sendCreditNotification(user, transaction);	
		}
		return transaction;
	}

	public Transaction addMoneyToWallet(long userId, long accountNumber, float amount) {
		User user = RESTClientUtil.getBasicUser(userId);
		Remark remark = new Remark();
		remark.setPurpose(Purpose.TopUp);
		remark.setPaidBy("Self");
		remark.setPaidTo("Self");
		remark.setMessage(Purpose.TopUp.toString());
		return credit(user, accountNumber, amount, remark);
	}
	
	public Transaction addRewardToWallet(long userId, long accountNumber, float amount, int rewardReimbursementTransactionId) {
		User user = RESTClientUtil.getBasicUser(userId);
		Company company = RESTClientUtil.getCompany(1);
		Remark remark = new Remark();
		remark.setPurpose(Purpose.Reward);
		remark.setPaidBy(company.getName());
		remark.setPaidTo(user.getFirstName()+ " "+user.getLastName());
		remark.setMessage("RewardReimbursementTransactionId:"+rewardReimbursementTransactionId);
		return credit(user, accountNumber, amount, remark);
	}


	public float getCalculatedBalance(Account account) {
		float balance = 0;
		for (Transaction transaction: account.getTransactions()) {
			if (transaction.getType().equals(TransactionType.Credit)) {
				balance = balance + transaction.getAmount();
			} else {
				balance = balance - transaction.getAmount();
			}
		}
		return balance;
	}

	public long redeemFromWallet(long userId, float amount) {
		User user = RESTClientUtil.getBasicUser(userId);
		BillDO billDO = new BillDO();
		List<Bill> pendingBills = billDO.getPendingBills(user);
		
		float pendingBillAmount = 0;
		for (Bill bill: pendingBills) {
			pendingBillAmount = pendingBillAmount + bill.getAmount();
		}
		
		float totalRewardCreditMoney = getTotalRewardCredit(user);
		float totalRideDebitMoney = getTotalRideDebit(user);
		float balanceRewardMoney = totalRewardCreditMoney - totalRideDebitMoney;
		if (balanceRewardMoney <= 0) balanceRewardMoney = 0;
		
		float maxRedemtionAmount = user.getAccount(AccountType.Virtual).getBalance() - pendingBillAmount - balanceRewardMoney;
		if (Float.compare(amount, maxRedemtionAmount) <= 0) {
		
			Remark debitRemark = new Remark();
			debitRemark.setPurpose(Purpose.Redeem);
			debitRemark.setPaidBy("Self");
			debitRemark.setPaidTo("Self");
			debitRemark.setMessage(Purpose.Redeem.toString());
			//Debit money from user wallet
			Transaction debitTransaction = debit(user, user.getAccount(AccountType.Virtual).getNumber(), amount, debitRemark);
			FinancialTransactionDO financialTransactionDO = new FinancialTransactionDO();
			FinancialTransaction financialTransaction = new FinancialTransaction();
			financialTransaction.setAmount(amount);
			financialTransaction.setDateTime(DateTimeUtil.getCurrentTimeInUTC());
			financialTransaction.setStatus(TransactionStatus.Initiated);
			financialTransaction.setType(TransactionType.Debit);	
			financialTransaction.setUser(user);
			financialTransaction.setWalletTransaction(debitTransaction);
			financialTransaction.setRemark(Purpose.Redeem.toString());
			//Add Financial Transaction entry for the PayTM transaction to transfer money to user account
			long financialTransactionId = financialTransactionDO.create(financialTransaction);
			return financialTransactionId;
			
		} else {
			
			throw new WebApplicationException("Maximum redemption available amount is: "
			+ RSUtil.getCurrencySymbol(user.getCountry()) + maxRedemtionAmount + " excluding your pending bills and rewards credit");
			
		}
	}

	private float getTotalRewardCredit(User user) {
		account = getAllData(user.getAccount(AccountType.Virtual).getNumber());
		Collection<Transaction> transactions = account.getTransactions();
		float totalRewardMoney = 0;
		for (Transaction transaction: transactions) {
			if (transaction.getType().equals(TransactionType.Credit) && transaction.getRemark().getPurpose().equals(Purpose.Reward)) {
				totalRewardMoney = totalRewardMoney + transaction.getAmount();
			}
		}
		return totalRewardMoney;
	}
	
	private float getTotalRideDebit(User user) {
		account = getAllData(user.getAccount(AccountType.Virtual).getNumber());
		Collection<Transaction> transactions = account.getTransactions();
		float totalRideDebitMoney = 0;
		for (Transaction transaction: transactions) {
			if (transaction.getType().equals(TransactionType.Debit) && transaction.getRemark().getPurpose().equals(Purpose.Ride)) {
				totalRideDebitMoney = totalRideDebitMoney + transaction.getAmount();
			}
		}
		return totalRideDebitMoney;
	}
	
	public Transaction rollbackRedemptionRequest(FinancialTransaction financialTransaction) {
		
		//Debit Company Account
		//Credit User Account
		Company company = RESTClientUtil.getCompany(1);
		User user = financialTransaction.getUser();
		Remark remark = new Remark();
		remark.setPurpose(Purpose.Reversal);
		remark.setPaidBy(company.getName());
		remark.setPaidTo(user.getFirstName() + " "+user.getLastName());
		remark.setMessage(Purpose.Reversal.toString()+" #Financial Transaction Id:"+financialTransaction.getId());
		//Credit money to user wallet
		Transaction creditTransaction = credit(user, user.getAccount(AccountType.Virtual).getNumber(), financialTransaction.getAmount(), remark);
		return creditTransaction;
		//TODO Send Notification to user
	}

	//This will get transaction sublist in ascending order
	public List<Transaction> getTransactions(long accountNumber, int page){

		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<TransactionEntity> transactionEntities = accountDAO.getTransactions(accountNumber, startIndex);
		TransactionMapper transactionMapper = new TransactionMapper();
		LinkedList<Transaction> transactions = new LinkedList<>();
		transactionMapper.getDomainModels(transactions, transactionEntities, true);
		//this will sort the list further
		Collections.sort(transactions);
		return transactions;
	}
	
	public WalletInfo getWalletInfo(long userId) {
		User user = RESTClientUtil.getBasicUser(userId);

		BillDO billDO = new BillDO();
		List<Bill> pendingBills = billDO.getPendingBills(user);
		
		float pendingBillAmount = 0;
		for (Bill bill: pendingBills) {
			pendingBillAmount = pendingBillAmount + bill.getAmount();
		}
		
		float totalRewardCreditMoney = getTotalRewardCredit(user);
		float totalRideDebitMoney = getTotalRideDebit(user);
		float balanceRewardMoney = totalRewardCreditMoney - totalRideDebitMoney;
		if (balanceRewardMoney <= 0) balanceRewardMoney = 0;
		
		WalletInfo walletInfo = new WalletInfo();
		walletInfo.setPendingBillsAmount(pendingBillAmount);
		walletInfo.setRewardMoneyBalance(balanceRewardMoney);
		
		return walletInfo;
	}
}







































