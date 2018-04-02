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
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Very Imp - Don't get child of Ride Request otherwise it will get into recursive loop
		billEntity.setRideRequest(rideRequestMapper.getEntity(bill.getRideRequest(), false));

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
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Very Imp - Don't get child of Ride Request otherwise it will get into recursive loop
		bill.setRideRequest(rideRequestMapper.getDomainModel(billEntity.getRideRequest(), false));
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
