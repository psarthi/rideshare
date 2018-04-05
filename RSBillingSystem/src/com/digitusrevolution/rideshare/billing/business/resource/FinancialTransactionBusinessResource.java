package com.digitusrevolution.rideshare.billing.business.resource;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.business.FinancialTransactionBusinessService;
import com.digitusrevolution.rideshare.billing.domain.service.AccountDomainService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmTransactionResponse;
import com.digitusrevolution.rideshare.model.billing.dto.TopUpResponse;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.common.ResponseMessage.Code;

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
	
	@Secured
	@POST
	@Path("/{accountNumber}/validateandprocesspayment")
	public Response validatePaytmResponseAndProcessPayment(@PathParam("accountNumber") long accountNumber, Map<String, String> paytmResponseHashMap) {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		String responseMessage = transactionBusinessService.validatePaytmResponseAndProcessPayment(paytmResponseHashMap);
		AccountDomainService accountDomainService = new AccountDomainService();
		Account account = accountDomainService.get(accountNumber, false);
		TopUpResponse topUpResponse = new TopUpResponse();
		topUpResponse.setAccount(account);
		topUpResponse.setMessage(responseMessage);
		return Response.ok(topUpResponse).build();
	}

	@Secured
	@GET
	@Path("/cancel/{orderId}")
	public Response cancelFinancialTransaction(@PathParam("orderId") long orderId) {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		transactionBusinessService.cancelFinancialTransaction(orderId);
		ResponseMessage message = new ResponseMessage();
		message.setStatus(Code.OK);
		return Response.ok(message).build();
	}	
	
	@Secured
	@POST
	@Path("/processpendingorders")
	public Response processPendingOrder(long[] orderIds) {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		transactionBusinessService.processPendingOrders(orderIds);			
		return Response.ok().build();		
	}
	
	@Secured
	@GET
	@Path("/processallpendingorders")
	public Response processAllPendingOrders() {
		FinancialTransactionBusinessService transactionBusinessService = new FinancialTransactionBusinessService();
		transactionBusinessService.processAllPendingOrders();			
		return Response.ok().build();		
	}

}






