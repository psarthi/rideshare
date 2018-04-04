package com.digitusrevolution.rideshare.billing.business.resource;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.business.FinancialTransactionBusinessService;
import com.digitusrevolution.rideshare.common.auth.Secured;

@Path("/users/{userId}/financialtransaction/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FinancialTransactionBusinessResource {
	
	private static final Logger logger = LogManager.getLogger(FinancialTransactionBusinessResource.class.getName());
	
	@Secured
	@GET
	@Path("/{amount}/getorderinfo")
	public Response getPaytmOrderInfo(@PathParam("userId") long userId, @PathParam("amount") float amount) {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		Map<String, String> paytmOrderInfo = transactionBusinessService.getPaytmOrderInfo(userId, amount);
		logger.debug("Final Payload: "+ paytmOrderInfo);
		return Response.ok(paytmOrderInfo).build();
	}

}
