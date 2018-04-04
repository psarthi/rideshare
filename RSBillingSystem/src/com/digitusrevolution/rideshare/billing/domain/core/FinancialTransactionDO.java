package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.billing.core.FinancialTransactionMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.FinancialTransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.FinancialTransaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.PaymentGateway;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.paytm.pg.merchant.CheckSumServiceHelper;

public class FinancialTransactionDO implements DomainObjectPKLong<FinancialTransaction>{

	private FinancialTransaction financialTransaction;
	private FinancialTransactionEntity financialTransactionEntity;
	private final GenericDAO<FinancialTransactionEntity, Long> genericDAO;
	private FinancialTransactionMapper financialTransactionMapper;
	private static final Logger logger = LogManager.getLogger(FinancialTransactionDO.class.getName());


	public FinancialTransactionDO() {
		financialTransaction = new FinancialTransaction();
		financialTransactionEntity = new FinancialTransactionEntity();
		genericDAO = new GenericDAOImpl<>(FinancialTransactionEntity.class);
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
		List<FinancialTransactionEntity> financialTransactionEntities = genericDAO.getAll();
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
		genericDAO.update(financialTransactionEntity);
	}

	@Override
	public void fetchChild() {
		financialTransaction = financialTransactionMapper.getDomainModelChild(financialTransaction, financialTransactionEntity);
	}

	@Override
	public long create(FinancialTransaction financialTransaction) {
		setFinancialTransaction(financialTransaction);
		long id = genericDAO.create(financialTransactionEntity);
		return id;
	}

	@Override
	public FinancialTransaction get(long number) {
		financialTransactionEntity = genericDAO.get(number);
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
		genericDAO.delete(financialTransactionEntity);
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
        paramMap.put( "MOBILE_NO" , "7777777777");
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

	public boolean validatePaytmResponse(Map<String, String> paytmResponseHashMap) {
		String paytmChecksum = "";
		TreeMap<String, String> paytmParamsTreeMap = new  TreeMap<String,String>();
		//We are doing like this instead of putAll as we need to iterate anyhow to get response checksum value
		//to compare with the calculated value using our merchant key
		for (Map.Entry<String, String> entry : paytmResponseHashMap.entrySet())
		{   
			if(entry.getKey().equals("CHECKSUMHASH")){
				paytmChecksum = entry.getKey();
			}else{
				paytmParamsTreeMap.put(entry.getKey(), entry.getValue());
			}
		}

		boolean isValideChecksum = false;
		try{

			isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.verifycheckSum(PropertyReader.getInstance().getProperty("PAYTM_PG_MERCHANT_KEY"), paytmParamsTreeMap, paytmChecksum);

			logger.debug("Checksum status:"+isValideChecksum);

			// if checksum is validated Kindly verify the amount and status 
			// if transaction is successful 
			// kindly call Paytm Transaction Status API and verify the transaction amount and status.
			// If everything is fine then mark that transaction as successful into your DB.
			
			return true;

		}catch(Exception e){
			throw new WebApplicationException("Invalid checksum");
		}

	}

}





















