package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.PartnerMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.PartnerEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.serviceprovider.data.PartnerDAO;

public class PartnerDO implements DomainObjectPKInteger<Partner>{
	
	private Partner partner;
	private PartnerEntity partnerEntity;
	private final PartnerDAO partnerDAO;
	private PartnerMapper partnerMapper;
	private static final Logger logger = LogManager.getLogger(PartnerDO.class.getName());
	
	public PartnerDO() {
		partner = new Partner();
		partnerEntity = new PartnerEntity();
		partnerDAO = new PartnerDAO();
		partnerMapper = new PartnerMapper();
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
		partnerEntity = partnerMapper.getEntity(partner, true);
	}

	public void setPartnerEntity(PartnerEntity partnerEntity) {
		this.partnerEntity = partnerEntity;
		partner = partnerMapper.getDomainModel(partnerEntity, false);
	}


	@Override
	public List<Partner> getAll() {
		List<Partner> companies = new ArrayList<>();
		List<PartnerEntity> partnerEntities = partnerDAO.getAll();
		for (PartnerEntity partnerEntity : partnerEntities) {
			setPartnerEntity(partnerEntity);
			companies.add(partner);
		}
		return companies;
	}

	@Override
	public void update(Partner partner) {
		if (partner.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+partner.getId());
		}
		setPartner(partner);
		partnerDAO.update(partnerEntity);
	}

	@Override
	public void fetchChild() {
		partner = partnerMapper.getDomainModelChild(partner, partnerEntity);
	}

	@Override
	public int create(Partner partner) {
		setPartner(partner);
		int id = partnerDAO.create(partnerEntity);
		return id;
	}

	@Override
	public Partner get(int id) {
		partnerEntity = partnerDAO.get(id);
		if (partnerEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setPartnerEntity(partnerEntity);
		return partner;
	}

	@Override
	public Partner getAllData(int id) {
		get(id);
		fetchChild();
		return partner;
	}

	@Override
	public void delete(int id) {
		partner = get(id);
		setPartner(partner);
		partnerDAO.delete(partnerEntity);
	}

}

























