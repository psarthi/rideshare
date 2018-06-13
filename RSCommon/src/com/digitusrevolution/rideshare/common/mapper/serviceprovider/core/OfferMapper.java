package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.PhotoMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.OfferEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;

public class OfferMapper implements Mapper<Offer, OfferEntity>{

	@Override
	public OfferEntity getEntity(Offer offer, boolean fetchChild) {
		OfferEntity offerEntity = new OfferEntity();
		offerEntity.setId(offer.getId());
		offerEntity.setName(offer.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		offerEntity.setPhoto(photoMapper.getEntity(offer.getPhoto(), fetchChild));
		offerEntity.setDescription(offer.getDescription());
		offerEntity.setTermsAndCondition(offer.getTermsAndCondition());
		offerEntity.setRedemptionProcess(offer.getRedemptionProcess());
		offerEntity.setRedemptionType(offer.getRedemptionType());
		offerEntity.setRidesRequired(offer.getRidesRequired());
		offerEntity.setRidesDuration(offer.getRidesDuration());
		offerEntity.setCompanyOffer(offer.isCompanyOffer());
		PartnerMapper partnerMapper = new PartnerMapper();
		//Don't fetch child as offer has partner and partner has offers
		//Keeping it here so that we can always get partner details with offers
		//On the PartnerMapper offer is in child method which is break the recursive loop
		if (offer.getPartner()!=null) offerEntity.setPartner(partnerMapper.getEntity(offer.getPartner(), false));

		if (fetchChild){
			offerEntity = getEntityChild(offer, offerEntity);
		}

		return offerEntity;
	}

	@Override
	public OfferEntity getEntityChild(Offer offer, OfferEntity offerEntity) {
		return offerEntity;
	}

	@Override
	public Offer getDomainModel(OfferEntity offerEntity, boolean fetchChild) {
		Offer offer = new Offer();
		offer.setId(offerEntity.getId());
		offer.setName(offerEntity.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		offer.setPhoto(photoMapper.getDomainModel(offerEntity.getPhoto(), fetchChild));
		offer.setDescription(offerEntity.getDescription());
		offer.setTermsAndCondition(offerEntity.getTermsAndCondition());
		offer.setRedemptionProcess(offerEntity.getRedemptionProcess());
		offer.setRedemptionType(offerEntity.getRedemptionType());
		offer.setRidesRequired(offerEntity.getRidesRequired());
		offer.setRidesDuration(offerEntity.getRidesDuration());
		offer.setCompanyOffer(offerEntity.isCompanyOffer());
		PartnerMapper partnerMapper = new PartnerMapper();
		//Don't fetch child as offer has partner and partner has offers
		//Keeping it here so that we can always get partner details with offers
		//On the PartnerMapper offer is in child method which is break the recursive loop
		if (offerEntity.getPartner()!=null) offer.setPartner(partnerMapper.getDomainModel(offerEntity.getPartner(), false));

		if (fetchChild){
			offer = getDomainModelChild(offer, offerEntity);
		}

		return offer;
	}

	@Override
	public Offer getDomainModelChild(Offer offer, OfferEntity offerEntity) {
		return offer;
	}

	@Override
	public Collection<Offer> getDomainModels(Collection<Offer> offers, Collection<OfferEntity> offerEntities,
			boolean fetchChild) {
		for (OfferEntity offerEntity: offerEntities) {
			Offer offer = new Offer();
			offer = getDomainModel(offerEntity, fetchChild);
			offers.add(offer);
		}
		return offers;

	}

	@Override
	public Collection<OfferEntity> getEntities(Collection<OfferEntity> offerEntities, Collection<Offer> offers,
			boolean fetchChild) {
		for (Offer offer: offers) {
			OfferEntity offerEntity = new OfferEntity();
			offerEntity = getEntity(offer, fetchChild);
			offerEntities.add(offerEntity);
		}
		return offerEntities;	
	}

}
