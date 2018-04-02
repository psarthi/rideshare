package com.digitusrevolution.rideshare.billing.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.BillingBusinessService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
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
	public Response approveBill(@PathParam("number") long billNumber){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.approveBill(billNumber);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/{number}/reject")
	public Response rejectBill(@PathParam("number") long billNumber){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.rejectBill(billNumber);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param Ride ride
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/pay")
	public Response makePayment(Ride ride){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.makePayment(ride);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param Passenger
	 * @return List of pending bills
	 */
	@Secured
	@POST
	@Path("/pending")
	public Response getPendingBills(BasicUser passenger){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		List<Bill> pendingBills = billingBusinessService.getPendingBills(passenger);
		GenericEntity<List<Bill>> entity = new GenericEntity<List<Bill>>(pendingBills) {};
		return Response.ok(entity).build();
	}
}

























