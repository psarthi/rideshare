package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.RewardReimbursementTransactionMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardReimbursementTransactionEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.serviceprovider.data.RewardReimbursementTransactionDAO;

public class RewardReimbursementTransactionDO implements DomainObjectPKInteger<RewardReimbursementTransaction>{
	
	private RewardReimbursementTransaction rewardReimbursementTransaction;
	private RewardReimbursementTransactionEntity rewardReimbursementTransactionEntity;
	private RewardReimbursementTransactionMapper rewardReimbursementTransactionMapper;
	private final RewardReimbursementTransactionDAO reimbursementTransactionDAO;
	
	public RewardReimbursementTransactionDO() {
		rewardReimbursementTransaction = new RewardReimbursementTransaction();
		rewardReimbursementTransactionEntity = new RewardReimbursementTransactionEntity();
		rewardReimbursementTransactionMapper = new RewardReimbursementTransactionMapper();
		reimbursementTransactionDAO = new RewardReimbursementTransactionDAO();
	}

	public void setRewardReimbursementTransaction(RewardReimbursementTransaction rewardReimbursementTransaction) {
		this.rewardReimbursementTransaction = rewardReimbursementTransaction;
		rewardReimbursementTransactionEntity = rewardReimbursementTransactionMapper.getEntity(rewardReimbursementTransaction, true);
		
	}

	private void setRewardReimbursementTransactionEntity(RewardReimbursementTransactionEntity rewardReimbursementTransactionEntity) {
		this.rewardReimbursementTransactionEntity = rewardReimbursementTransactionEntity;
		rewardReimbursementTransaction = rewardReimbursementTransactionMapper.getDomainModel(rewardReimbursementTransactionEntity, false);
	}

	@Override
	public void fetchChild() {
		rewardReimbursementTransaction = rewardReimbursementTransactionMapper.getDomainModelChild(rewardReimbursementTransaction, rewardReimbursementTransactionEntity);
		
	}

	@Override
	public int create(RewardReimbursementTransaction rewardReimbursementTransaction) {
		setRewardReimbursementTransaction(rewardReimbursementTransaction);
		int id = reimbursementTransactionDAO.create(rewardReimbursementTransactionEntity);
		return id;
	}

	@Override
	public RewardReimbursementTransaction get(int id) {
		rewardReimbursementTransactionEntity = reimbursementTransactionDAO.get(id);
		if (rewardReimbursementTransactionEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRewardReimbursementTransactionEntity(rewardReimbursementTransactionEntity);
		return rewardReimbursementTransaction;
	}

	@Override
	public RewardReimbursementTransaction getAllData(int id) {
		get(id);
		fetchChild();
		return rewardReimbursementTransaction;
	}

	@Override
	public List<RewardReimbursementTransaction> getAll() {
		List<RewardReimbursementTransaction> cities = new ArrayList<>();
		List<RewardReimbursementTransactionEntity> rewardReimbursementTransactionEntities = reimbursementTransactionDAO.getAll();
		for (RewardReimbursementTransactionEntity rewardReimbursementTransactionEntity : rewardReimbursementTransactionEntities) {
			setRewardReimbursementTransactionEntity(rewardReimbursementTransactionEntity);
			cities.add(rewardReimbursementTransaction);
		}
		return cities;
	}

	@Override
	public void update(RewardReimbursementTransaction rewardReimbursementTransaction) {
		if (rewardReimbursementTransaction.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rewardReimbursementTransaction.getId());
		}
		setRewardReimbursementTransaction(rewardReimbursementTransaction);
		reimbursementTransactionDAO.update(rewardReimbursementTransactionEntity);				
	}

	@Override
	public void delete(int id) {
		rewardReimbursementTransaction = get(id);
		setRewardReimbursementTransaction(rewardReimbursementTransaction);
		reimbursementTransactionDAO.delete(rewardReimbursementTransactionEntity);			
	}
	
	public List<RewardReimbursementTransaction> getReimbursementTransactions(long userId, int page){
		User user = RESTClientUtil.getBasicUser(userId);
		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = userMapper.getEntity(user, false);
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount;
		List<RewardReimbursementTransactionEntity> transactionEntities = reimbursementTransactionDAO.getReimbursementTransactions(userEntity, startIndex);
		List<RewardReimbursementTransaction> transactions = new LinkedList<>();
		transactions = (List<RewardReimbursementTransaction>) rewardReimbursementTransactionMapper.getDomainModels(transactions, transactionEntities, false);
		Collections.sort(transactions);
		return transactions;
		
	}
}
