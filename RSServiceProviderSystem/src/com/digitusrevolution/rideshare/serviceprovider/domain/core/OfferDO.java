package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.OfferMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.OfferEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.serviceprovider.data.OfferDAO;

public class OfferDO implements DomainObjectPKInteger<Offer>{
	
	private Offer offer;
	private OfferEntity offerEntity;
	private final OfferDAO offerDAO;
	private OfferMapper offerMapper;
	private static final Logger logger = LogManager.getLogger(OfferDO.class.getName());
	
	public OfferDO() {
		offer = new Offer();
		offerEntity = new OfferEntity();
		offerDAO = new OfferDAO();
		offerMapper = new OfferMapper();
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
		offerEntity = offerMapper.getEntity(offer, true);
	}

	public void setOfferEntity(OfferEntity offerEntity) {
		this.offerEntity = offerEntity;
		offer = offerMapper.getDomainModel(offerEntity, false);
	}


	@Override
	public List<Offer> getAll() {
		List<Offer> companies = new ArrayList<>();
		List<OfferEntity> offerEntities = offerDAO.getAll();
		for (OfferEntity offerEntity : offerEntities) {
			setOfferEntity(offerEntity);
			companies.add(offer);
		}
		return companies;
	}

	@Override
	public void update(Offer offer) {
		if (offer.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+offer.getId());
		}
		setOffer(offer);
		offerDAO.update(offerEntity);
	}

	@Override
	public void fetchChild() {
		offer = offerMapper.getDomainModelChild(offer, offerEntity);
	}

	@Override
	public int create(Offer offer) {
		setOffer(offer);
		int id = offerDAO.create(offerEntity);
		return id;
	}

	@Override
	public Offer get(int id) {
		offerEntity = offerDAO.get(id);
		if (offerEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setOfferEntity(offerEntity);
		return offer;
	}

	@Override
	public Offer getAllData(int id) {
		get(id);
		fetchChild();
		return offer;
	}

	@Override
	public void delete(int id) {
		offer = get(id);
		setOffer(offer);
		offerDAO.delete(offerEntity);
	}
	
	public List<Offer> getOffers(int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount;
		List<OfferEntity> offerEntities = offerDAO.getOffers(startIndex);
		List<Offer> offers = new LinkedList<>();
		offers =  (List<Offer>) offerMapper.getDomainModels(offers, offerEntities, true);
		//This will sort the offers
		Collections.sort(offers);
		return offers;
	}

}

























