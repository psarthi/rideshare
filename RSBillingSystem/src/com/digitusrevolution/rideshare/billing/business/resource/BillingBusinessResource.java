package com.digitusrevolution.rideshare.billing.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.BillingBusinessService;
import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;

@Path("/users/{userId}/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillingBusinessResource {
		
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/{number}/approve")
	public Response approveBill(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("number") long billNumber){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			BillingBusinessService billingBusinessService = new BillingBusinessService();
			billingBusinessService.approveBill(billNumber);
			return Response.ok().build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/{number}/reject")
	public Response rejectBill(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("number") long billNumber){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			BillingBusinessService billingBusinessService = new BillingBusinessService();
			billingBusinessService.rejectBill(billNumber);
			return Response.ok().build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param billInfo DTO containing bill number with account which is primarily Passenger Account
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/pay")
	public Response makePayment(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			BillInfo billInfo){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			BillingBusinessService billingBusinessService = new BillingBusinessService();
			Bill bill = billingBusinessService.makePayment(billInfo);
			return Response.ok(bill).build();
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param Passenger
	 * @return List of pending bills
	 */
	@Secured
	@POST
	@Path("/pending")
	public Response getPendingBills(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			BasicUser passenger){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			BillingBusinessService billingBusinessService = new BillingBusinessService();
			List<Bill> pendingBills = billingBusinessService.getPendingBills(passenger);
			GenericEntity<List<Bill>> entity = new GenericEntity<List<Bill>>(pendingBills) {};
			return Response.ok(entity).build();
		}else {
			throw new NotAuthorizedException();
		}
	}
}

























