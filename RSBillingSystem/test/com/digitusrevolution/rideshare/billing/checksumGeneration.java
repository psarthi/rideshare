package com.digitusrevolution.rideshare.billing;
import com.paytm.pg.merchant.*;
import java.util.TreeMap;

public class checksumGeneration {
	
	//Below parameters provided by Paytm
	
	private static String MID = "Digitu20940997232495"; 
	private static String MercahntKey = "jap!22aj00dM9s9X";
	private static String INDUSTRY_TYPE_ID = "Retail";
	private static String CHANNLE_ID = "WAP";
	private static String WEBSITE = "WEB_STAGING";
	private static String CALLBACK_URL = "https://pguat.paytm.com/oltp-web/processTransaction?ORDER_ID=ORDER0000000004";
	
	public static void main(String[] a){
		
		TreeMap<String,String> paramMap = new TreeMap<String,String>();
		paramMap.put("MID" , MID);
		paramMap.put("ORDER_ID" , "ORDER0000000004");
		paramMap.put("CUST_ID" , "CUST00001");
		paramMap.put("INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
		paramMap.put("CHANNEL_ID" , CHANNLE_ID);
		paramMap.put("TXN_AMOUNT" , "1");
		paramMap.put("WEBSITE" , WEBSITE);
		paramMap.put("EMAIL" , "partha.sarthi@gmail.com");
		paramMap.put("MOBILE_NO" , "7777777777");
		paramMap.put("CALLBACK_URL" , CALLBACK_URL);
		
		try{
		String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MercahntKey, paramMap);
		paramMap.put("CHECKSUMHASH" , checkSum);
		
		System.out.println("CheckSum:"+checkSum);
		System.out.println("Paytm Payload: "+ paramMap);

		paramMap.put("MOBILE_NO", "123456789");
		String newCheckSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MercahntKey, paramMap);
		paramMap.put("CHECKSUMHASH" , newCheckSum);
		System.out.println("New CheckSum:"+newCheckSum);
		System.out.println("New Paytm Payload: "+ paramMap);
		
		boolean isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(MercahntKey, paramMap, checkSum);
		
		System.out.println(isValideChecksum);

		
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
