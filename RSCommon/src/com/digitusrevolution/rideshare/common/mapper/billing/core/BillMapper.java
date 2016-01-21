package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.CompanyMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;

public class BillMapper implements Mapper<Bill, BillEntity>{

	@Override
	public BillEntity getEntity(Bill bill, boolean fetchChild) {
		BillEntity billEntity = new BillEntity();
		billEntity.setNumber(bill.getAmount());
		billEntity.setAmount(bill.getAmount());
		
		if (fetchChild){
			billEntity = getEntityChild(bill, billEntity);
		}
		
		return billEntity;
	}

	@Override
	public BillEntity getEntityChild(Bill bill, BillEntity billEntity) {
		CompanyMapper companyMapper = new CompanyMapper();
		billEntity.setCompany(companyMapper.getEntity(bill.getCompany(), true));
		UserMapper userMapper = new UserMapper();
		billEntity.setPassenger(userMapper.getEntity(bill.getPassenger(), false));
		billEntity.setDriver(userMapper.getEntity(bill.getDriver(), false));
		RideMapper rideMapper = new RideMapper();
		billEntity.setRide(rideMapper.getEntity(bill.getRide(), false));

		return billEntity;
	}

	@Override
	public Bill getDomainModel(BillEntity billEntity, boolean fetchChild) {
		Bill bill = new Bill();
		bill.setNumber(billEntity.getAmount());
		bill.setAmount(billEntity.getAmount());
		if (fetchChild){
			bill = getDomainModelChild(bill, billEntity);
		}		
		return bill;
	}

	@Override
	public Bill getDomainModelChild(Bill bill, BillEntity billEntity) {
		CompanyMapper companyMapper = new CompanyMapper();
		bill.setCompany(companyMapper.getDomainModel(billEntity.getCompany(), true));
		UserMapper userMapper = new UserMapper();
		bill.setPassenger(userMapper.getDomainModel(billEntity.getPassenger(), false));
		bill.setDriver(userMapper.getDomainModel(billEntity.getDriver(), false));
		RideMapper rideMapper = new RideMapper();
		bill.setRide(rideMapper.getDomainModel(billEntity.getRide(), false));
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
