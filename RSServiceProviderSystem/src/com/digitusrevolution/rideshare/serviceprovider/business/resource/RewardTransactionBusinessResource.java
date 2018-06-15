package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.ReimbursementStatus;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityResult;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.ReimbursementRequest;
import com.digitusrevolution.rideshare.serviceprovider.business.OfferBusinessService;
import com.digitusrevolution.rideshare.serviceprovider.business.RewardTransactionBusinessService;

@Path("/users/{userId}/rewardtransactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RewardTransactionBusinessResource {

	@Secured
	@GET
	@Path("/coupon/{id}")
	public Response getCouponTransaction(@PathParam("id") int id) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		RewardCouponTransaction rewardCouponTransaction = transactionBusinessService.getRewardCouponTransaction(id);
		return Response.ok(rewardCouponTransaction).build();
	}
	
	@Secured
	@GET
	@Path("/reimbursement/{id}")
	public Response getReimbursementTransaction(@PathParam("id") int id) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		RewardReimbursementTransaction rewardReimbursementTransaction = transactionBusinessService.getRewardReimbursementTransaction(id);
		return Response.ok(rewardReimbursementTransaction).build();
	}
	@Secured
	@GET
	@Path("/reimbursement")
	public Response getReimbursementTransactions(@PathParam("userId") long userId, @QueryParam("page") int page) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		List<RewardReimbursementTransaction> rewardReimbursementTransactions = transactionBusinessService.getRewardReimbursementTransactions(userId, page);
		return Response.ok(rewardReimbursementTransactions).build();
	}
	@Secured
	@GET
	@Path("/coupon")
	public Response getCouponTransactions(@PathParam("userId") long userId, @QueryParam("page") int page) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		List<RewardCouponTransaction> rewardCouponTransactions = transactionBusinessService.getRewardCouponTransactions(userId, page);
		return Response.ok(rewardCouponTransactions).build();
	}	
	
	@Secured
	@POST
	@Path("/offer/{offerId}/create")
	public Response createReimbursementTransactions(@PathParam("userId") long userId, 
			@PathParam("offerId") int offerId, ReimbursementRequest reimbursementRequest) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		transactionBusinessService.createRewardReimbursementTransaction(userId, offerId, reimbursementRequest);
		ResponseMessage message = new ResponseMessage();
		message.setResult(ResponseMessage.Code.OK.toString());
		return Response.ok(message).build();
	}
	@Secured
	@POST
	@Path("/reimbursement/{id}/checkeligibility")
	public Response isUserEligibleForOffer(@PathParam("userId") long userId, @PathParam("id") int rewardReimbursementTransactionId, OfferEligibilityInfo offerEligibilityInfo) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		OfferEligibilityResult eligibilityResult = transactionBusinessService.getUserEligibilityForOffer(userId, rewardReimbursementTransactionId, offerEligibilityInfo.getDateTime());
		return Response.ok(eligibilityResult).build();
	}

	@Secured
	@GET
	@Path("/reimbursement/{id}/approve/{amount}")
	public Response approveRewardReimbursementTransaction(@PathParam("userId") long userId, @PathParam("id") int reimbursementId, 
			@PathParam("amount") int approvedAmount, @QueryParam("remarks") String remarks) {
		RewardTransactionBusinessService transactionBusinessService = new RewardTransactionBusinessService();
		RewardReimbursementTransaction reimbursementTransaction = transactionBusinessService.approveRewardReimbursementTransaction(reimbursementId, approvedAmount, remarks);
		if (!reimbursementTransaction.getStatus().equals(ReimbursementStatus.Paid)) {
			//VERY IMP - Don't addmoney within the session e.g. in Service layer or DO etc. otherwise it will throw exception of transaction id not found while updating the transaction in reimbursement record
			//so by adding reward money here, we fully seperated the session layer
			Transaction walletTransaction = RESTClientUtil.addRewardToWallet(reimbursementTransaction.getUser().getId(), 
					reimbursementTransaction.getUser().getAccount(AccountType.Virtual).getNumber(), approvedAmount, reimbursementId);
			//Note - There is still a catch where payment is done but somehow below transaction of update has failed, in that case we need to handle the process payment seperately
			//TODO - Need to think on that. For the time being we can check manually all the status post running the REST request and for all Approved status, we need to check manually and update the status accordinlgy
			transactionBusinessService.processPaymentForRewardReimbursementTransaction(reimbursementId, walletTransaction);
		}
		return Response.ok().build();
	}
	
}
