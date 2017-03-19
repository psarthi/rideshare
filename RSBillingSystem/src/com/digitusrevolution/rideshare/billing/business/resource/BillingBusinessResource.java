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
import com.digitusrevolution.rideshare.model.billing.dto.BillDTO;
import com.digitusrevolution.rideshare.model.billing.dto.RideDTO;

@Path("/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillingBusinessResource {
	
	/**
	 * 
	 * @param rideDTO This contains Ride and Ride Request domain model
	 * @return billNumber
	 */
	@POST
	@Path("/generatebill")
	public Response generateBill(RideDTO rideDTO){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		int number = billingBusinessService.generateBill(rideDTO);
		return Response.ok(Integer.toString(number)).build();
	}
	
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
	 * @param billDTO DTO containing bill number with account which is primarily Passenger Account
	 * @return status OK
	 */
	@POST
	@Path("/pay")
	public Response makePayment(BillDTO billDTO){
		BillingBusinessService billingBusinessService = new BillingBusinessService();
		billingBusinessService.makePayment(billDTO);
		return Response.ok().build();
	}

}

























