package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.CompanyMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;

public class BillMapper implements Mapper<Bill, BillEntity>{

	@Override
	public BillEntity getEntity(Bill bill, boolean fetchChild) {
		BillEntity billEntity = new BillEntity();
		billEntity.setNumber(bill.getNumber());
		billEntity.setRate(bill.getRate());
		billEntity.setAmount(bill.getAmount());
		billEntity.setDiscountPercentage(bill.getDiscountPercentage());
		billEntity.setStatus(bill.getStatus());
		UserMapper userMapper = new UserMapper();
		billEntity.setPassenger(userMapper.getEntity(bill.getPassenger(), false));

		if (fetchChild){
			billEntity = getEntityChild(bill, billEntity);
		}
		
		return billEntity;
	}

	@Override
	public BillEntity getEntityChild(Bill bill, BillEntity billEntity) {
		return billEntity;
	}

	@Override
	public Bill getDomainModel(BillEntity billEntity, boolean fetchChild) {
		Bill bill = new Bill();
		bill.setNumber(billEntity.getNumber());
		bill.setRate(billEntity.getRate());
		bill.setAmount(billEntity.getAmount());
		bill.setDiscountPercentage(billEntity.getDiscountPercentage());
		bill.setStatus(billEntity.getStatus());
		UserMapper userMapper = new UserMapper();
		bill.setPassenger(userMapper.getDomainModel(billEntity.getPassenger(), false));

		if (fetchChild){
			bill = getDomainModelChild(bill, billEntity);
		}		
		return bill;
	}

	@Override
	public Bill getDomainModelChild(Bill bill, BillEntity billEntity) {
		return bill;
	}

	@Override
	public Collection<Bill> getDomainModels(Collection<Bill> bills, Collection<BillEntity> billEntities,
			boolean fetchChild) {
		for (BillEntity billEntity : billEntities) {
			Bill bill = new Bill();
			bill = getDomainModel(billEntity, fetchChild);
			bills.add(bill);
		}
		return bills;
	}

	@Override
	public Collection<BillEntity> getEntities(Collection<BillEntity> billEntities, Collection<Bill> bills,
			boolean fetchChild) {
		for (Bill bill : bills) {
			BillEntity billEntity = new BillEntity();
			billEntity = getEntity(bill, fetchChild);
			billEntities.add(billEntity);
		}
		return billEntities;
	}

}
