package com.digitusrevolution.rideshare.billing.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.BillingBusinessService;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;

@Path("/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillingBusinessResource {
		
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@POST
	@Path("/{number}/approve")
	public Response approveBill(@PathParam("number") int billNumber){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.approveBill(billNumber);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@POST
	@Path("/{number}/reject")
	public Response rejectBill(@PathParam("number") int billNumber){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.rejectBill(billNumber);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param billInfo DTO containing bill number with account which is primarily Passenger Account
	 * @return status OK
	 */
	@POST
	@Path("/pay")
	public Response makePayment(BillInfo billInfo){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.makePayment(billInfo);
		return Response.ok().build();
	}

}

























