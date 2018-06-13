package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardTransaction;
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
	
}
