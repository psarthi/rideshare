package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.dto.UserRidesDurationInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.OfferEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RidesDuration;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityResult;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.UserOffer;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.UserRidesStats;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
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
	
	public List<UserOffer> getOffers(long userId, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount;
		List<OfferEntity> offerEntities = offerDAO.getOffers(startIndex);
		List<Offer> offers = new LinkedList<>();
		offers =  (List<Offer>) offerMapper.getDomainModels(offers, offerEntities, true);
		//This will sort the offers
		Collections.sort(offers);
		ZonedDateTime dateTime = DateTimeUtil.getCurrentTimeInUTC();
		UserRidesStats ridesStats = getUserRidesStats(userId, dateTime);
		List<UserOffer> userOffers = new LinkedList<>();
		for (Offer offer: offers) {
			UserOffer userOffer = JsonObjectMapper.getMapper().convertValue(offer, UserOffer.class);
			userOffer.setUserEligible(isUserEligibleForOffer(ridesStats, offer));
			userOffer.setBalanceRideRequirement(getRidesBalanceRequirement(ridesStats, userOffer));
			userOffers.add(userOffer);
		}
		return userOffers;
	}
	
	public int getRidesBalanceRequirement(UserRidesStats ridesStats, Offer offer) {
		if (offer.getRidesDuration().equals(RidesDuration.Week)) {
			if (ridesStats.getThisWeekCount() <= offer.getRidesRequired()) {
				return offer.getRidesRequired() - ridesStats.getThisWeekCount();
			}
		}
		if (offer.getRidesDuration().equals(RidesDuration.Month)) {
			if (ridesStats.getThisMonthCount() <= offer.getRidesRequired()) {
				return offer.getRidesRequired() - ridesStats.getThisMonthCount();
			}
		}
		return 0;
	}

	public boolean isUserEligibleForOffer(UserRidesStats ridesStats, Offer offer) {

		//High Level Logic
		//Get User Total rides for Weekly and Monthly duration for last week / month and current week / month
		//Check offer eligibility rides requirement with user rides
		if (offer.getRidesDuration().equals(RidesDuration.Week)) {
			if (ridesStats.getThisWeekCount() >= offer.getRidesRequired() || ridesStats.getLastWeekCount() >= offer.getRidesRequired()) {
				return true;
			} else {
				return false;
			}
		}
		if (offer.getRidesDuration().equals(RidesDuration.Month)) {
			if (ridesStats.getThisMonthCount() >= offer.getRidesRequired() || ridesStats.getLastMonthCount() >= offer.getRidesRequired()) {
				return true;
			} else {
				return false;
			}
		}	
		return false;
	}
	
	public boolean isUserEligibleForOffer(long userId, int offerId) {
		UserRidesStats ridesStats = getUserRidesStats(userId, DateTimeUtil.getCurrentTimeInUTC());
		offer = get(offerId);
		return isUserEligibleForOffer(ridesStats, offer);
	}
	
	public OfferEligibilityResult getUserEligibilityForOffer(long userId, int offerId, ZonedDateTime dateTime) {
		UserRidesStats ridesStats = getUserRidesStats(userId, dateTime);
		offer = get(offerId);
		boolean status = false;
		
		//High Level Logic
		//Get User Total rides for Weekly and Monthly duration for last week / month and current week / month
		//Check offer eligibility rides requirement with user rides
		if (offer.getRidesDuration().equals(RidesDuration.Week)) {
			if (ridesStats.getThisWeekCount() >= offer.getRidesRequired() || ridesStats.getLastWeekCount() >= offer.getRidesRequired()) {
				status = true;
			}
		}
		if (offer.getRidesDuration().equals(RidesDuration.Month)) {
			if (ridesStats.getThisMonthCount() >= offer.getRidesRequired() || ridesStats.getLastMonthCount() >= offer.getRidesRequired()) {
				status = true;
			}
		}	
		OfferEligibilityResult eligibilityResult = new OfferEligibilityResult();
		eligibilityResult.setUserEligible(status);
		eligibilityResult.setUserRidesStats(ridesStats);
		eligibilityResult.setOffer(offer);
		return eligibilityResult;
	}
	
	public UserRidesStats getUserRidesStats(long userId, ZonedDateTime dateTime) {
		
		UserRidesDurationInfo durationInfo = new UserRidesDurationInfo();
		durationInfo.setDailyMaxLimit(Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_COMBINED_RIDE_AND_RIDE_REQUEST_DAILY_LIMIT")));
		durationInfo.setRidesDuration(RidesDuration.Week);
		durationInfo.setUserId(userId);
		durationInfo.setWeekDayDate(dateTime);
		int currentWeekRideCount = RESTClientUtil.getRidesAndRidesRequestsCombinedCount(durationInfo);
		
		durationInfo.setWeekDayDate(dateTime.minusWeeks(1));
		int lastWeekRideCount = RESTClientUtil.getRidesAndRidesRequestsCombinedCount(durationInfo);
		
		durationInfo.setRidesDuration(RidesDuration.Month);
		durationInfo.setWeekDayDate(dateTime);
		int currentMonthRideCount = RESTClientUtil.getRidesAndRidesRequestsCombinedCount(durationInfo);
		
		durationInfo.setWeekDayDate(dateTime.minusMonths(1));
		int lastMonthRideCount = RESTClientUtil.getRidesAndRidesRequestsCombinedCount(durationInfo);
		
		UserRidesStats userRidesStats = new UserRidesStats();
		userRidesStats.setThisWeekCount(currentWeekRideCount);
		userRidesStats.setLastWeekCount(lastWeekRideCount);
		userRidesStats.setThisMonthCount(currentMonthRideCount);
		userRidesStats.setLastMonthCount(lastMonthRideCount);
		
		JSONUtil<UserRidesStats> jsonUtil = new JSONUtil<>(UserRidesStats.class);
		logger.debug(jsonUtil.getJson(userRidesStats));
		
		return userRidesStats;
	}
		
	
	public int createOffer(Offer offer) {
		int id = create(offer);
		offer = getAllData(id);
		if (offer.isCompanyOffer()) {
			CompanyDO companyDO = new CompanyDO();
			Company company = companyDO.getAllData(1);
			company.getOffers().add(offer);
			companyDO.update(company);
		}
		return id;
	}
	
	//IMP - Don't move this function to Partner as updating partner would not associate partner with offers
	//as mapping is by offer (mappedBy)ild
	public void addPartnerOffer(int partnerId, Offer offer) {
		PartnerDO partnerDO = new PartnerDO();
		Partner partner = partnerDO.getAllData(partnerId);
		offer.setPartner(partner);
		update(offer);
	}


}

























