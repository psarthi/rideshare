package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.PhotoMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.PartnerEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;

public class PartnerMapper implements Mapper<Partner, PartnerEntity> {

	@Override
	public PartnerEntity getEntity(Partner partner, boolean fetchChild) {
		PartnerEntity partnerEntity = new PartnerEntity();
		partnerEntity.setId(partner.getId());
		partnerEntity.setName(partner.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		partnerEntity.setPhoto(photoMapper.getEntity(partner.getPhoto(), fetchChild));
		partnerEntity.setAddress(partner.getAddress());
		partnerEntity.setContactNumber(partner.getContactNumber());
		partnerEntity.setEmail(partner.getEmail());

		if (fetchChild){
			partnerEntity = getEntityChild(partner, partnerEntity);
		}

		return partnerEntity;
	}

	@Override
	public PartnerEntity getEntityChild(Partner partner, PartnerEntity partnerEntity) {
		
		OfferMapper offerMapper = new OfferMapper();
		//Don't fetch child of offer as offer contain partner and partner has offers
		partnerEntity.setOffers(offerMapper.getEntities(partnerEntity.getOffers(), partner.getOffers(), false));

		return partnerEntity;
	}

	@Override
	public Partner getDomainModel(PartnerEntity partnerEntity, boolean fetchChild) {
		Partner partner = new Partner();
		partner.setId(partnerEntity.getId());
		partner.setName(partnerEntity.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		partner.setPhoto(photoMapper.getDomainModel(partnerEntity.getPhoto(), fetchChild));
		partner.setAddress(partnerEntity.getAddress());
		partner.setContactNumber(partnerEntity.getContactNumber());
		partner.setEmail(partnerEntity.getEmail());

		if (fetchChild){
			partner = getDomainModelChild(partner, partnerEntity);
		}

		return partner;
	}

	@Override
	public Partner getDomainModelChild(Partner partner, PartnerEntity partnerEntity) {
		OfferMapper offerMapper = new OfferMapper();
		//Don't fetch child of offer as offer contain partner and partner has offers
		partner.setOffers(offerMapper.getDomainModels(partner.getOffers(), partnerEntity.getOffers(), false));
		
		return partner;
	}

	@Override
	public Collection<Partner> getDomainModels(Collection<Partner> partners, Collection<PartnerEntity> partnerEntities,
			boolean fetchChild) {
		for (PartnerEntity partnerEntity: partnerEntities) {
			Partner partner = new Partner();
			partner = getDomainModel(partnerEntity, fetchChild);
			partners.add(partner);
		}
		return partners;

	}

	@Override
	public Collection<PartnerEntity> getEntities(Collection<PartnerEntity> partnerEntities, Collection<Partner> partners,
			boolean fetchChild) {
		for (Partner partner: partners) {
			PartnerEntity partnerEntity = new PartnerEntity();
			partnerEntity = getEntity(partner, fetchChild);
			partnerEntities.add(partnerEntity);
		}
		return partnerEntities;	
	}

}
