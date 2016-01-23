package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.BillDAO;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.billing.core.BillMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class BillDO implements DomainObjectPKInteger<Bill>{
	
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
	public int create(Bill bill) {
		setBill(bill);
		int id = billDAO.create(billEntity);
		return id;
	}

	@Override
	public Bill get(int id) {
		billEntity = billDAO.get(id);
		if (billEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setBillEntity(billEntity);
		return bill;
	}

	@Override
	public Bill getChild(int id) {
		get(id);
		fetchChild();
		return bill;
	}

	@Override
	public void delete(int id) {
		bill = get(id);
		setBill(bill);
		billDAO.delete(billEntity);
	}
	
	/*
	 * Purpose - Generate bill for specific ride request
	 * 
	 * High level logic -
	 * 
	 * - Get ride and ride request
	 * - Calculate bill based of fair/car etc.
	 * - Generate bill with all details
	 * 
	 */
	public int generateBill(Ride ride, RideRequest rideRequest){
		
		User passenger = rideRequest.getPassenger();
		User driver = ride.getDriver();
		float price = getFare(ride.getVehicle().getVehicleSubCategory(), driver);
		float distance = rideRequest.getTravelDistance();
		float amount = price * distance;
		Company company = RESTClientUtil.getCompany(1);
		float serviceChargePercentage = company.getServiceChargePercentage();
		//Set Bill properties
		Bill bill = new Bill();
		bill.setAmount(amount);
		bill.setServiceChargePercentage(serviceChargePercentage);
		bill.setCompany(company);
		bill.setDriver(driver);
		bill.setPassenger(passenger);
		bill.setRide(ride);
		bill.setRideRequest(rideRequest);
		//Create Bill
		int id = create(bill);
		return id;
	}
	
	/*
	 * 
	 */
	private float getFare(VehicleSubCategory vehicleSubCategory, User driver){
		FuelType fuelType = vehicleSubCategory.getFuelType();
		int averageMileage = vehicleSubCategory.getAverageMileage();
		Collection<Fuel> fuels = driver.getCountry().getFuels(); 
		for (Fuel fuel : fuels) {
			if (fuel.getType().name().equals(fuelType.name())){
				//This will get fare per meter and not by Km
				float fare = fuel.getPrice() / (averageMileage * 1000);
				return fare;
			}
		}
		throw new NotFoundException("Fuel type is not found. Fuel type is:"+fuelType);
	}
	
	public void payBill(){
		
	}

}










































