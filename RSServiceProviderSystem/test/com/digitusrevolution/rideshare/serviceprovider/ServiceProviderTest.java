package com.digitusrevolution.rideshare.serviceprovider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.CouponStatus;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RedemptionType;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RidesDuration;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.OfferDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.PartnerDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.RewardCouponTransactionDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.RewardReimbursementTransactionDO;

@Path("/domain/serviceprovidertest")
public class ServiceProviderTest {
	
	private static final Logger logger = LogManager.getLogger(ServiceProviderTest.class.getName());
	
	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			ServiceProviderTest serviceProviderTest = new ServiceProviderTest();
			serviceProviderTest.test();
			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}	
	}
	
	public void test() {
		/*		
		RewardCouponTransactionDO couponTransactionDO = new RewardCouponTransactionDO();
		Collection<RewardCouponTransaction> couponTransactions = couponTransactionDO.getCouponTransactions(1);
		
		RewardReimbursementTransactionDO reimbursementTransactionDO = new RewardReimbursementTransactionDO();
		Collection<RewardReimbursementTransaction> reimbursementTransactions = reimbursementTransactionDO.getReimbursementTransactions(1);
		*/
		
		Partner partner = new Partner();
		partner.setAddress("address");
		partner.setContactNumber("contactNumber");
		partner.setEmail("email");
		partner.setName("name");
		Photo photo = new Photo();
		photo.setImageLocation("imageLocation");
		partner.setPhoto(photo);
	
		PartnerDO partnerDO = new PartnerDO();
		int partnerId = partnerDO.create(partner);
		Partner partner1 = partnerDO.getAllData(partnerId);
		
		Offer offer = new Offer();
		offer.setName("name");
		offer.setDescription("description");
		offer.setRedemptionProcess("redemptionProcess");
		offer.setTermsAndCondition("termsAndCondition");
		Photo photo1 = new Photo();
		photo1.setImageLocation("imageLocation");
		offer.setPhoto(photo1);
		offer.setCompanyOffer(false);
		offer.setRidesDuration(RidesDuration.Week);
		offer.setRidesRequired(5);
		offer.setRedemptionType(RedemptionType.Coupon);
		offer.setPartner(partner1);
		
		OfferDO offerDO = new OfferDO();
		int id = offerDO.create(offer);
		Offer offer1 = offerDO.getAllData(id);
		
		CompanyDO  companyDO = new CompanyDO();
		Company company = companyDO.getAllData(1);
		company.getPartners().add(partner1);
		companyDO.update(company);
		
/*		RewardCouponTransaction couponTransaction = new RewardCouponTransaction();
		Offer offer = RESTClientUtil.getOffer(1);
		couponTransaction.setOffer(offer);
		couponTransaction.setRedemptionDateTime(DateTimeUtil.getCurrentTimeInUTC());
		couponTransaction.setStatus(CouponStatus.Active);
		couponTransaction.setUser(RESTClientUtil.getBasicUser(1));
	
		
		RewardCouponTransactionDO couponTransactionDO = new RewardCouponTransactionDO();
		int transactionId = couponTransactionDO.create(couponTransaction);
		
		RewardReimbursementTransaction reimbursementTransaction = new RewardReimbursementTransaction();
		Offer offer = RESTClientUtil.getOffer(1);
		reimbursementTransaction.setOffer(offer);
		Photo photo = new Photo();
		photo.setImageLocation("imageLocation");
		Photo photo1 = new Photo();
		photo1.setImageLocation("imageLocation");
		reimbursementTransaction.getPhotos().add(photo);
		reimbursementTransaction.getPhotos().add(photo1);
		reimbursementTransaction.setRedemptionDateTime(DateTimeUtil.getCurrentTimeInUTC());
		reimbursementTransaction.setStatus(ReimbursementStatus.Submitted);
		UserDO userDO = new UserDO();
		User user = userDO.get(1);
		reimbursementTransaction.setUser(user);
		
		RewardReimbursementTransactionDO reimbursementTransactionDO = new RewardReimbursementTransactionDO();
		int transactionId = reimbursementTransactionDO.create(reimbursementTransaction);
	*/	
	}

}


















