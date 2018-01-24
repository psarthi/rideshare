package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.BillDAO;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.billing.core.BillMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.Purpose;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class BillDO implements DomainObjectPKLong<Bill>{
	
	private Bill bill;
	private BillEntity billEntity;
	private final BillDAO billDAO;
	private BillMapper billMapper;
	private static final Logger logger = LogManager.getLogger(BillDO.class.getName());
	
	public BillDO() {
		bill = new Bill();
		billEntity = new BillEntity();
		billDAO = new BillDAO();
		billMapper = new BillMapper();
	}

	public void setBill(Bill bill) {
		this.bill = bill;
		billEntity = billMapper.getEntity(bill, true);
	}

	public void setBillEntity(BillEntity billEntity) {
		this.billEntity = billEntity;
		bill = billMapper.getDomainModel(billEntity, false);
	}

	@Override
	public List<Bill> getAll() {
		List<Bill> bills = new ArrayList<>();
		List<BillEntity> billEntities = billDAO.getAll();
		for (BillEntity billEntity : billEntities) {
			setBillEntity(billEntity);
			bills.add(bill);
		}
		return bills;
	}

	@Override
	public void update(Bill bill) {
		if (bill.getNumber()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+bill.getNumber());
		}
		setBill(bill);
		billDAO.update(billEntity);
	}

	@Override
	public void fetchChild() {
		bill = billMapper.getDomainModelChild(bill, billEntity);
	}

	@Override
	public long create(Bill bill) {
		setBill(bill);
		long id = billDAO.create(billEntity);
		return id;
	}

	@Override
	public Bill get(long number) {
		billEntity = billDAO.get(number);
		if (billEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setBillEntity(billEntity);
		return bill;
	}

	@Override
	public Bill getAllData(long number) {
		get(number);
		fetchChild();
		return bill;
	}

	@Override
	public void delete(long number) {
		bill = get(number);
		setBill(bill);
		billDAO.delete(billEntity);
	}
	
	private BillStatus getStatus(long billNumber){
		return billDAO.getStatus(billNumber);
	}
	
	public void approveBill(long billNumber){
		bill = getAllData(billNumber);
		if (bill.getStatus().equals(BillStatus.Pending) || bill.getStatus().equals(BillStatus.Rejected)){
			bill.setStatus(BillStatus.Approved);
			update(bill);
		} else {
			throw new NotAcceptableException("Bill can't be approved as its not in valid state (Pending/Rejected). Bill current status:"+bill.getStatus());			
		}

	}
	
	public void rejectBill(long billNumber){
		bill = getAllData(billNumber);
		if (bill.getStatus().equals(BillStatus.Pending)){
			bill.setStatus(BillStatus.Rejected);
			update(bill);
		} else {
			throw new NotAcceptableException("Bill can't be rejected as its not in valid state (Pending). Bill current status:"+bill.getStatus());			
		}
	}

	/*
	 * Purpose - Make payment to driver and company from Passenger account
	 * 
	 */
	public Bill makePayment(long billNumber){
		bill = getAllData(billNumber);
		if (bill.getStatus().equals(BillStatus.Approved)){
			float amount = bill.getAmount();
			float serviceChargePercentage = bill.getServiceChargePercentage();
			float serviceCharge = amount * serviceChargePercentage / 100;
			float driverAmount = amount - serviceCharge;
			//Get AccountDO for specific account type
			//This is more from future usage if there are multiple types of account associated with user, 
			//so we can use it for the current usage, we always need to withdraw money from Virtual account
			AccountDO accountDO = getAccountDO(AccountType.Virtual);
			Remark remark = new Remark();
			remark.setBillNumber(billNumber);
			remark.setPaidBy(bill.getPassenger().getFirstName()+" "+bill.getPassenger().getLastName());
			remark.setPaidTo(bill.getDriver().getFirstName()+ " "+ bill.getDriver().getLastName());
			remark.setPurpose(Purpose.Ride);
			remark.setRideId(bill.getRide().getId());
			remark.setRideRequestId(bill.getRideRequest().getId());
			String message = "Bill:"+billNumber+",Ride:"+bill.getRide().getId()+",RideRequest:"+bill.getRideRequest().getId();
			remark.setMessage(message);
			//This will ensure that we don't do any transaction if the bill amount is ZERO which would be applicable for lets say Free Rides or 100% discounted rides
			if (amount!=0) {
				accountDO.debit(bill.getPassenger().getAccount(AccountType.Virtual).getNumber(), amount, remark);
				accountDO.credit(bill.getDriver().getAccount(AccountType.Virtual).getNumber(), driverAmount, remark);
				//This will ensure that paidTo field is updated for service charge
				remark.setPaidTo(bill.getCompany().getName());
				accountDO.credit(bill.getCompany().getAccount(AccountType.Virtual).getNumber(), serviceCharge, remark);						
			}
			bill.setStatus(BillStatus.Paid);
			update(bill);
		} else {
			throw new NotAcceptableException("Can't make payment as bill is either not Approved or Paid. Bill current status:"+bill.getStatus());
		}
		return bill;
	}
	
	/*
	 * Purpose - It should return appropriate Account DO Impl based on Account type
	 */
	private AccountDO getAccountDO(AccountType accountType){
		if (accountType.equals(AccountType.Virtual)){
			return new VirtualAccountDO();
		}
		throw new NotFoundException("No appropriate account DO found for account type:"+accountType);
	}
	
	public List<Bill> getPendingBills(User passenger){
		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = userMapper.getEntity(passenger, false);
		Set<BillEntity> billEntities = billDAO.getPendingBills(userEntity);
		List<Bill> bills = new ArrayList<>();
		for (BillEntity entity: billEntities) {
			setBillEntity(entity);
			bills.add(bill);
		}
		return bills;
	}

	public float getPendingBillsAmount(User passenger) {
		List<Bill> pendingBills = getPendingBills(passenger);
		float pendingAmount = 0;
		for (Bill bill: pendingBills) {
			pendingAmount+=bill.getAmount();
		}
		return pendingAmount;
	}
}










































