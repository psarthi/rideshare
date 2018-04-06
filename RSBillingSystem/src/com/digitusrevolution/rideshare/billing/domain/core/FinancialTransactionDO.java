package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.FinancialTransactionDAO;
import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.billing.core.FinancialTransactionMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.FinancialTransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.FinancialTransaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.PaymentGateway;
import com.digitusrevolution.rideshare.model.billing.domain.core.Purpose;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmGratificationRequest;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmGratificationRequest.Request;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmGratificationStatusRequest;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmTransactionResponse;
import com.digitusrevolution.rideshare.model.billing.dto.PaytmTransactionStatus;
import com.digitusrevolution.rideshare.model.billing.dto.TopUpResponse;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.common.ResponseMessage.Code;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.paytm.pg.merchant.CheckSumServiceHelper;

public class FinancialTransactionDO implements DomainObjectPKLong<FinancialTransaction>{

	private FinancialTransaction financialTransaction;
	private FinancialTransactionEntity financialTransactionEntity;
	private final FinancialTransactionDAO financialTransactionDAO;
	private FinancialTransactionMapper financialTransactionMapper;
	private static final Logger logger = LogManager.getLogger(FinancialTransactionDO.class.getName());


	public FinancialTransactionDO() {
		financialTransaction = new FinancialTransaction();
		financialTransactionEntity = new FinancialTransactionEntity();
		financialTransactionDAO = new FinancialTransactionDAO();
		financialTransactionMapper = new FinancialTransactionMapper();
	}

	public void setFinancialTransaction(FinancialTransaction financialTransaction) {
		this.financialTransaction = financialTransaction;
		financialTransactionEntity = financialTransactionMapper.getEntity(financialTransaction, true);
	}

	public void setFinancialTransactionEntity(FinancialTransactionEntity financialTransactionEntity) {
		this.financialTransactionEntity = financialTransactionEntity;
		financialTransaction = financialTransactionMapper.getDomainModel(financialTransactionEntity, false);
	}

	@Override
	public List<FinancialTransaction> getAll() {
		List<FinancialTransaction> financialTransactions = new ArrayList<>();
		List<FinancialTransactionEntity> financialTransactionEntities = financialTransactionDAO.getAll();
		for (FinancialTransactionEntity financialTransactionEntity : financialTransactionEntities) {
			setFinancialTransactionEntity(financialTransactionEntity);
			financialTransactions.add(financialTransaction);
		}
		return financialTransactions;
	}

	@Override
	public void update(FinancialTransaction financialTransaction) {
		if (financialTransaction.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+financialTransaction.getId());
		}
		setFinancialTransaction(financialTransaction);
		financialTransactionDAO.update(financialTransactionEntity);
	}

	@Override
	public void fetchChild() {
		financialTransaction = financialTransactionMapper.getDomainModelChild(financialTransaction, financialTransactionEntity);
	}

	@Override
	public long create(FinancialTransaction financialTransaction) {
		setFinancialTransaction(financialTransaction);
		long id = financialTransactionDAO.create(financialTransactionEntity);
		return id;
	}

	@Override
	public FinancialTransaction get(long number) {
		financialTransactionEntity = financialTransactionDAO.get(number);
		if (financialTransactionEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setFinancialTransactionEntity(financialTransactionEntity);
		return financialTransaction;
	}

	@Override
	public FinancialTransaction getAllData(long number) {
		get(number);
		fetchChild();
		return financialTransaction;
	}

	@Override
	public void delete(long number) {
		financialTransaction = get(number);
		setFinancialTransaction(financialTransaction);
		financialTransactionDAO.delete(financialTransactionEntity);
	}

	public Map<String, String> getPaytmOrderInfo(long userId, float amount) {

		User user = RESTClientUtil.getBasicUser(userId);
		FinancialTransaction financialTransaction = new FinancialTransaction();
		financialTransaction.setUser(user);
		financialTransaction.setDateTime(DateTimeUtil.getCurrentTimeInUTC());
		financialTransaction.setPaymentGateway(PaymentGateway.PayTM);
		financialTransaction.setStatus(TransactionStatus.Initiated);
		financialTransaction.setType(TransactionType.Credit);
		financialTransaction.setAmount(amount);
		financialTransaction.setRemark(Purpose.TopUp.toString());
		long orderId = create(financialTransaction);

		//Kindly create complete Map and checksum on your server side and then put it here in paramMap.
		Map<String, String> paramMap = new HashMap<String,String>();

		//Constant Params
		paramMap.put( "MID" , PropertyReader.getInstance().getProperty("PAYTM_PG_MID"));
		paramMap.put( "INDUSTRY_TYPE_ID" , PropertyReader.getInstance().getProperty("PAYTM_PG_INDUSTRY_TYPE_ID"));
		paramMap.put( "CHANNEL_ID" , PropertyReader.getInstance().getProperty("PAYTM_PG_CHANNEL_ID"));
		paramMap.put( "WEBSITE" , PropertyReader.getInstance().getProperty("PAYTM_PG_WEBSITE"));
		paramMap.put( "CALLBACK_URL" , PropertyReader.getInstance().getProperty("PAYTM_PG_CALLBACK_URL"));

		//Order Specific Params
		paramMap.put( "CUST_ID" , Long.toString(userId));
		paramMap.put( "TXN_AMOUNT" , Float.toString(amount));
		paramMap.put( "EMAIL" , user.getEmail());
		paramMap.put( "MOBILE_NO" , user.getMobileNumber().substring(3));
		paramMap.put( "ORDER_ID", Long.toString(orderId));

		String checksum = generateCheckSum(paramMap);
		paramMap.put("CHECKSUMHASH", checksum);

		return paramMap;
	}
	
	public String generateCheckSum(Map<String, String> paramHashMap) {

		TreeMap<String, String> paramTreeMap = new TreeMap<>();
		//Its mandatory to convert hashmap to treemap for checksum generation 
		//else we need to sort this alphabetically etc. (Refer documentation of payTM for that)
		paramTreeMap.putAll(paramHashMap);

		try{
			String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper()
					.genrateCheckSum(PropertyReader.getInstance().getProperty("PAYTM_PG_MERCHANT_KEY"), paramTreeMap);
			logger.debug("Paytm Payload for checksum generation: "+ paramTreeMap);
			return checkSum;

		}catch(Exception e) {
			throw new WebApplicationException("Unable to generate checksum");
		}
	}

	public String validatePaytmResponseAndProcessPayment(Map<String, String> paytmResponseHashMap) {
		if (verifyChecksumOfPaytmResponse(paytmResponseHashMap)) {
			PaytmTransactionResponse paytmResponse = JsonObjectMapper.getMapper().convertValue(paytmResponseHashMap, PaytmTransactionResponse.class);
			String orderId = paytmResponse.getORDERID();
			PaytmTransactionStatus transactionStatus = getPaytmTransactionStatusAPIResponse(orderId);
			FinancialTransaction financialTransaction = get(Long.parseLong(orderId));
			String responseMsg = null; 
			long accountNumber = financialTransaction.getUser().getAccount(AccountType.Virtual).getNumber();

			if (validatePaytmResponseWithTransactionStatusAPI(paytmResponse, transactionStatus, financialTransaction)) {
				//This is a defensive check to ensure that payment is not already processed earlier 
				if (transactionStatus.getSTATUS().equals("TXN_SUCCESS") && !financialTransaction.getStatus().equals(TransactionStatus.Success)) {
					VirtualAccountDO accountDO= new VirtualAccountDO();
					Transaction walletTransaction = accountDO.addMoneyToWallet(accountNumber, financialTransaction.getAmount());

					//Updated wallet transaction in financial transaction
					financialTransaction.setWalletTransaction(walletTransaction);	
					financialTransaction.setStatus(TransactionStatus.Success);
					responseMsg = PropertyReader.getInstance().getProperty("WALLET_TOPUP_SUCCESS");
				}
				if (transactionStatus.getSTATUS().equals("TXN_FAILURE")) {
					financialTransaction.setStatus(TransactionStatus.Failed);
					responseMsg = PropertyReader.getInstance().getProperty("WALLET_TOPUP_FAILURE");
				}
				if (transactionStatus.getSTATUS().equals("PENDING")) {
					financialTransaction.setStatus(TransactionStatus.Pending);
					responseMsg = PropertyReader.getInstance().getProperty("WALLET_TOPUP_PENDING");
				}
				if (transactionStatus.getSTATUS().equals("OPEN")) {
					financialTransaction.setStatus(TransactionStatus.Open);
					responseMsg = PropertyReader.getInstance().getProperty("WALLET_TOPUP_OPEN");
				}
				//Common for all status 
				financialTransaction.setPgTransactionId(transactionStatus.getTXNID());
				financialTransaction.setPgTransactionStatus(transactionStatus.getSTATUS());
				financialTransaction.setPgResponseCode(transactionStatus.getRESPCODE());
				financialTransaction.setPgResponseMsg(transactionStatus.getRESPMSG());
				update(financialTransaction);
				return responseMsg;
			} else {
				logger.error("Invalid Paytm response - Mismatch with Transaction Status API");
				throw new WebApplicationException(PropertyReader.getInstance().getProperty("TECHNICAL_ERROR_MESSAGE"));
			}
		} else {
			logger.error("Invalid Paytm response - Checksum Failed");
			throw new WebApplicationException(PropertyReader.getInstance().getProperty("TECHNICAL_ERROR_MESSAGE"));
		}
	}
	
	public void processAllPendingOrders() {
		Set<FinancialTransactionEntity> pendingTransactions = financialTransactionDAO.getPendingTransactions();
		Iterator<FinancialTransactionEntity> iterator = pendingTransactions.iterator();
		LinkedList<Long> list = new LinkedList<>(); 
		while (iterator.hasNext()) {
			FinancialTransactionEntity financialTransactionEntity = iterator.next();
			list.add(financialTransactionEntity.getId());
		}
		long[] orderIds = new long[list.size()];
		for (int i=0; i<orderIds.length; i++) {
			orderIds[i] = list.get(i);
		}
		processPendingOrders(orderIds);
	}

	public void processPendingOrders(long[] orderIds) {
		for (int i=0; i<orderIds.length; i++) {
			PaytmTransactionStatus transactionStatus = getPaytmTransactionStatusAPIResponse(Long.toString(orderIds[i]));
			FinancialTransaction financialTransaction = get(orderIds[i]); 
			long accountNumber = financialTransaction.getUser().getAccount(AccountType.Virtual).getNumber();

			//This is a defensive check to ensure that payment is not already processed earlier 
			if (transactionStatus.getSTATUS().equals("TXN_SUCCESS") && !financialTransaction.getStatus().equals(TransactionStatus.Success)) {
				VirtualAccountDO accountDO= new VirtualAccountDO();
				Transaction walletTransaction = accountDO.addMoneyToWallet(accountNumber, financialTransaction.getAmount());

				//Updated wallet transaction in financial transaction
				financialTransaction.setWalletTransaction(walletTransaction);	
				financialTransaction.setStatus(TransactionStatus.Success);
			}
			//Note - IMP For invalid order Id also we get TXN_FAILURE, so this will take care of all orders which was initiated but never reached paytm
			if (transactionStatus.getSTATUS().equals("TXN_FAILURE")) {
				financialTransaction.setStatus(TransactionStatus.Failed);
			}
			if (transactionStatus.getSTATUS().equals("PENDING")) {
				financialTransaction.setStatus(TransactionStatus.Pending);
			}
			if (transactionStatus.getSTATUS().equals("OPEN")) {
				financialTransaction.setStatus(TransactionStatus.Open);
			}
			//Common for all status 
			financialTransaction.setPgTransactionStatus(transactionStatus.getSTATUS());
			financialTransaction.setPgResponseCode(transactionStatus.getRESPCODE());
			financialTransaction.setPgResponseMsg(transactionStatus.getRESPMSG());
			update(financialTransaction);
		}
	}

	public void cancelFinancialTransaction(long orderId) {
		FinancialTransaction financialTransaction = get(orderId);
		financialTransaction.setStatus(TransactionStatus.Cancelled);
		financialTransaction.setRemark("Cancelled by User, No Transaction initiated");
		update(financialTransaction);
	}

	public void handlePendingTransactionNotificationFromPaytm(PaytmTransactionResponse paytmResponse) {
		Map<String, String> paramMap = JsonObjectMapper.getMapper().convertValue(paytmResponse, new TypeReference<Map<String, Object>>() {});
		if (verifyChecksumOfPaytmResponse(paramMap)) {
			String orderId = paytmResponse.getORDERID();
			PaytmTransactionStatus transactionStatus = getPaytmTransactionStatusAPIResponse(orderId);
			FinancialTransaction financialTransaction = get(Long.parseLong(orderId)); 

			long accountNumber = financialTransaction.getUser().getAccount(AccountType.Virtual).getNumber();

			if (validatePaytmResponseWithTransactionStatusAPI(paytmResponse, transactionStatus, financialTransaction)) {
				//This is a defensive check to ensure that we are only processing request for pending transactions only		
				if (financialTransaction.getPgTransactionStatus().equals("PENDING") && !financialTransaction.getStatus().equals(TransactionStatus.Success)) {
					logger.debug("Processing Pending Transaction of id:"+financialTransaction.getId());
					if (transactionStatus.getSTATUS().equals("TXN_SUCCESS")){
						VirtualAccountDO accountDO= new VirtualAccountDO();
						Transaction walletTransaction = accountDO.addMoneyToWallet(accountNumber, financialTransaction.getAmount());

						//Updated wallet transaction in financial transaction
						financialTransaction.setWalletTransaction(walletTransaction);	
						financialTransaction.setStatus(TransactionStatus.Success);
					}
					if (transactionStatus.getSTATUS().equals("TXN_FAILURE")) {
						financialTransaction.setStatus(TransactionStatus.Failed);
					}
					//Common for all status 
					financialTransaction.setPgTransactionStatus(transactionStatus.getSTATUS());
					financialTransaction.setPgResponseCode(transactionStatus.getRESPCODE());
					financialTransaction.setPgResponseMsg(transactionStatus.getRESPMSG());
					update(financialTransaction);
				}
				logger.debug("Pending Transaction is already processed for id:"+financialTransaction.getId());
			}else {
				logger.error("Invalid Paytm response - Mismatch with Transaction Status API");
				throw new WebApplicationException(PropertyReader.getInstance().getProperty("TECHNICAL_ERROR_MESSAGE"));
			}
		} else {
			logger.error("Invalid Paytm response - Checksum Failed");
			throw new WebApplicationException(PropertyReader.getInstance().getProperty("TECHNICAL_ERROR_MESSAGE"));
		}
	}

	private PaytmTransactionStatus getPaytmTransactionStatusAPIResponse(String orderId) {
		Map <String, String> paramMap = new HashMap<>();
		String mid = PropertyReader.getInstance().getProperty("PAYTM_PG_MID");
		paramMap.put( "MID" , mid);
		paramMap.put( "ORDER_ID", orderId);
		String checksum = generateCheckSum(paramMap);
		paramMap.put( "CHECKSUMHASH", checksum);
		PaytmTransactionStatus transactionStatus = RESTClientUtil.
				getFinancialTransactionStatus(paramMap);
		return transactionStatus;
	}

	//Keeping this just for reference purpose
	private PaytmTransactionResponse getPaytmTransactionResponseFromMap(Map<String, String> paytmResponseHashMap) {
		return JsonObjectMapper.getMapper().convertValue(paytmResponseHashMap, PaytmTransactionResponse.class);
		/*
		String json = null;
		try {
			json = JsonObjectMapper.getMapper().writeValueAsString(paytmResponseHashMap);
		} catch (JsonProcessingException e) {
			logger.error("Unable to convert to Json");
			throw new WebApplicationException(PropertyReader.getInstance().getProperty("TECHNICAL_ERROR_MESSAGE"));
		}
		JSONUtil<PaytmTransactionResponse> jsonUtil = new JSONUtil<>(PaytmTransactionResponse.class);
		PaytmTransactionResponse paytmResponse = jsonUtil.getModel(json);
		return paytmResponse;
		 */
	}


	public boolean verifyChecksumOfPaytmResponse(Map<String, String> paytmResponseHashMap) {
		String paytmChecksum = "";
		TreeMap<String, String> paytmParamsTreeMap = new  TreeMap<String,String>();
		//We are doing like this instead of putAll as we need to iterate anyhow to get response checksum value
		//to compare with the calculated value using our merchant key
		for (Map.Entry<String, String> entry : paytmResponseHashMap.entrySet())
		{   
			if(entry.getKey().equals("CHECKSUMHASH")){
				paytmChecksum = entry.getValue();
			}else{
				paytmParamsTreeMap.put(entry.getKey(), entry.getValue());
			}
		}

		boolean isValideChecksum = false;
		try{

			logger.debug("Response Payload:"+paytmParamsTreeMap);
			logger.debug("Response Checksum:"+paytmChecksum);

			isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.verifycheckSum(PropertyReader.getInstance().getProperty("PAYTM_PG_MERCHANT_KEY"), paytmParamsTreeMap, paytmChecksum);

			logger.debug("Checksum status:"+isValideChecksum); 
			return isValideChecksum;

		}catch(Exception e){
			throw new WebApplicationException("Invalid checksum");
		}
	}

	private boolean validatePaytmResponseWithTransactionStatusAPI(PaytmTransactionResponse paytmResponse,
			PaytmTransactionStatus transactionStatus, FinancialTransaction financialTransaction) {

		logger.debug("Txn Id:"+paytmResponse.getTXNID()+"/"+transactionStatus.getTXNID());
		logger.debug("Status:"+paytmResponse.getSTATUS()+"/"+transactionStatus.getSTATUS());
		logger.debug("Amount:"+financialTransaction.getAmount()+"/"+Float.parseFloat(transactionStatus.getTXNAMOUNT()));
		logger.debug("OrderId:"+financialTransaction.getId()+"/"+ Long.parseLong(transactionStatus.getORDERID()));

		if (paytmResponse.getTXNID().equals(transactionStatus.getTXNID())
				&& paytmResponse.getSTATUS().equals(transactionStatus.getSTATUS())
				&& Float.parseFloat(transactionStatus.getTXNAMOUNT()) == financialTransaction.getAmount()
				&& Long.parseLong(transactionStatus.getORDERID()) == financialTransaction.getId()) {
			return true;	
		} 
		return false;
	}
	
	
	public void sendMoneyToUserPayTMWallet(long financialTransactionId) {
		
		FinancialTransaction financialTransaction = getAllData(financialTransactionId);
		
		//Create GratificationRequest
		//Generate Checksum
		//Send Post Request
		
		PaytmGratificationRequest gratificationRequest = new PaytmGratificationRequest();
		Request request = gratificationRequest.getRequest();
		request.setRequestType("null");
		request.setMerchantGuid(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_MERCHANT_GUID"));
		request.setSalesWalletGuid(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_SALES_WALLET_GUID"));
		request.setMerchantOrderId(Long.toString(financialTransaction.getId()));
		request.setSalesWalletName(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_SALES_WALLET_NAME"));
		request.setPayeeEmailId(financialTransaction.getUser().getEmail());
		request.setPayeePhoneNumber("7777777777");
		request.setPayeeSsoId("");
		request.setAppliedToNewUsers("Y");
		request.setAmount(Float.toString(financialTransaction.getAmount()));
		request.setCurrencyCode(financialTransaction.getUser().getCountry().getCurrency().getName());
		
		gratificationRequest.setIpAddress(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_IP"));
		gratificationRequest.setMetadata(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_METADATA"));
		gratificationRequest.setPlatformName(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_PLATFORM_NAME"));
		gratificationRequest.setOperationType("SALES_TO_USER_CREDIT");
		gratificationRequest.setIpAddress(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_IP_ADDRESS"));
		
		Map<String, String> paramMap = JsonObjectMapper.getMapper().convertValue(request, new TypeReference<Map<String, Object>>() {});
		String checksum = generateCheckSum(paramMap);
		JSONUtil<PaytmGratificationRequest> jsonUtil = new JSONUtil<>(PaytmGratificationRequest.class);
		logger.debug("PayTM Gratification payload is:"+jsonUtil.getJson(gratificationRequest));
		
		String status = RESTClientUtil.postPaytmGratificationRequest(gratificationRequest, checksum);
		getPaytmGratificationRequestStatus(financialTransactionId);
		
		financialTransaction.setPaymentGateway(PaymentGateway.PayTM);
		financialTransaction.setPgResponseCode("");
		financialTransaction.setPgResponseMsg("");
		financialTransaction.setPgTransactionId("");
		
		if (status.equals("success")) {
			financialTransaction.setStatus(TransactionStatus.Success);
		}
		if (status.equals("failure")) {
			financialTransaction.setStatus(TransactionStatus.Failed);
			financialTransaction.setRemark("Reversal Successful");
			VirtualAccountDO accountDO = new VirtualAccountDO();
			accountDO.rollbackRedemptionRequest(financialTransaction);
		}
		if (status.equals("pending")) {
			financialTransaction.setStatus(TransactionStatus.Pending);
		}
		
		update(financialTransaction);
	}
	
	public void getPaytmGratificationRequestStatus(long financialTransactionId) {
		
		FinancialTransaction financialTransaction = getAllData(financialTransactionId);
		PaytmGratificationStatusRequest gratificationStatusRequest = new PaytmGratificationStatusRequest();
		com.digitusrevolution.rideshare.model.billing.dto.PaytmGratificationStatusRequest.Request request = 
				gratificationStatusRequest.getRequest();
		
		request.setRequestType("merchanttxnid");
		request.setTxnType("withdraw");
		//What would happen to those cases where there is no response recieved from server initally itself 
		request.setTxnId(Long.toString(financialTransaction.getId()));
		request.setMerchantGuid(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_MERCHANT_GUID"));
		
		gratificationStatusRequest.setPlatformName(PropertyReader.getInstance().getProperty("PAYTM_GRATIFICATION_PLATFORM_NAME"));
		gratificationStatusRequest.setOperationType("CHECK_TXN_STATUS");

		Map<String, String> paramMap = JsonObjectMapper.getMapper().convertValue(request, new TypeReference<Map<String, Object>>() {});
		String checksum = generateCheckSum(paramMap);
		JSONUtil<PaytmGratificationStatusRequest> jsonUtil = new JSONUtil<>(PaytmGratificationStatusRequest.class);
		logger.debug("PayTM Gratification payload is:"+jsonUtil.getJson(gratificationStatusRequest));
		
		RESTClientUtil.getPaytmGratificationRequestStatus(gratificationStatusRequest, checksum);
		
	}
}





















