package com.digitusrevolution.rideshare.billing.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.BillingService;
import com.digitusrevolution.rideshare.model.billing.dto.BillDTO;
import com.digitusrevolution.rideshare.model.billing.dto.RideDTO;

@Path("/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillingResource {
	
	@POST
	@Path("/generatebill")
	public Response generateBill(RideDTO rideDTO){
		BillingService billingService = new BillingService();
		int number = billingService.generateBill(rideDTO);
		return Response.ok(Integer.toString(number)).build();
	}
	
	@GET
	@Path("/{number}/approve")
	public Response approveBill(@PathParam("number") int billNumber){
		BillingService billingService = new BillingService();
		billingService.approveBill(billNumber);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{number}/reject")
	public Response rejectBill(@PathParam("number") int billNumber){
		BillingService billingService = new BillingService();
		billingService.rejectBill(billNumber);
		return Response.ok().build();
	}
	
	@POST
	@Path("/pay")
	public Response makePayment(BillDTO billDTO){
		BillingService billingService = new BillingService();
		billingService.makePayment(billDTO);
		return Response.ok().build();
	}

}

























