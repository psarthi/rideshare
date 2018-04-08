package com.digitusrevolution.rideshare.billing.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.FinancialTransactionBusinessService;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmTransactionResponse;

@Path("/financialtransaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
public class ExternalCallBacksResource {

	@POST
	@Path("/pendingnotification")
	public Response handlePendingTransactionNotificationFromPaytm(PaytmTransactionResponse paytmTransactionResponse) {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		transactionBusinessService.handlePendingTransactionNotificationFromPaytm(paytmTransactionResponse);
		return Response.ok().build();
	}
	

}
