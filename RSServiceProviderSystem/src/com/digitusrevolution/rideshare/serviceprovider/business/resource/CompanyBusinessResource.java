package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.service.NotificationService;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.HelpQuestionAnswer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.AppInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.CompanyAccount;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.NotificationData;
import com.digitusrevolution.rideshare.serviceprovider.business.CompanyBusinessService;
import com.digitusrevolution.rideshare.serviceprovider.business.OfferBusinessService;

@Path("/serviceprovider")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyBusinessResource {
	
	/**
	 * 
	 * @param companyAccount Account of the company which is different than normal user Account domain model
	 * @return status OK
	 */
	@Secured
	@POST
	@Path("/{id}/accounts")
	public Response addAccount(CompanyAccount companyAccount){

		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		companyBusinessService.addAccount(companyAccount);
		return Response.ok().build();
	}

	/**
	 * 
	 * @param id Id of the company
	 * @param fetchChild value should be either true or false, this value should be passed as query parameter e.g. url?fetchchild=true. True value would return all data and false would return basic data of the model
	 * @return Company domain model
	 */
	@Secured
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild) {
		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		Company company = companyBusinessService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(company).build();
	}
	
	@Secured
	@GET
	@Path("/help")
	public Response getAllHelpQuestionAnswer() {
		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		List<HelpQuestionAnswer> helpQuestionAnswers = companyBusinessService.getAllHelpQuestionAnswer();
		GenericEntity<List<HelpQuestionAnswer>> entity = new GenericEntity<List<HelpQuestionAnswer>>(helpQuestionAnswers) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/appinfo")
	public Response getAppInfo() {
		AppInfo appInfo = new AppInfo();
		appInfo.setMinAppVersionCode(Integer.parseInt(PropertyReader.getInstance().getProperty("MIN_APP_VERSION_CODE")));
		appInfo.setAppUrl(PropertyReader.getInstance().getProperty("APP_URL"));
		appInfo.setShareMsg(PropertyReader.getInstance().getProperty("APP_SHARE_MSG"));
		return Response.ok(appInfo).build();
	}
	
	@Secured
	@POST
	@Path("/sendnotificationtoall")
	public Response sendNotificationToAll(NotificationData notificationData) {
		NotificationService.sendNotificationToAllUsers(notificationData.getTitle(), notificationData.getBody(), notificationData.getImageUrl());
		return Response.ok().build();
	}
	
	@Secured
	@POST
	@Path("/partner/create")
	public Response createPartner(Partner partner) {
		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		int id = companyBusinessService.createPartner(partner);
		return Response.ok(id).build();
	}
}
