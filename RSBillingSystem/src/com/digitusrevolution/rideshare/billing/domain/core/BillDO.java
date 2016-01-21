package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.BillDAO;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.billing.core.BillMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;

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

}
