package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import java.time.ZonedDateTime;
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
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityResult;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.UserOffer;
import com.digitusrevolution.rideshare.serviceprovider.business.OfferBusinessService;

@Path("/users/{userId}/offers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OfferBusinessResource {
	
	@Secured
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild) {
		OfferBusinessService offerBusinessService = new OfferBusinessService();
		Offer offer = offerBusinessService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(offer).build();
	}

	@Secured
	@GET
	public Response getAll(@PathParam("userId") long userId, @QueryParam("page") int page){	
		OfferBusinessService offerBusinessService = new OfferBusinessService();
		List<UserOffer> userOffers = offerBusinessService.getAll(userId, page);
		GenericEntity<List<UserOffer>> entity = new GenericEntity<List<UserOffer>>(userOffers) {};
		return Response.ok(entity).build();
	}

	@Secured
	@POST
	@Path("/{id}/checkeligibility")
	public Response isUserEligibleForOffer(@PathParam("userId") long userId, @PathParam("id") int offerId, OfferEligibilityInfo offerEligibilityInfo) {
		OfferBusinessService offerBusinessService = new OfferBusinessService();
		OfferEligibilityResult eligibilityResult = offerBusinessService.getUserEligibilityForOffer(userId, offerId, offerEligibilityInfo.getDateTime());
		return Response.ok(eligibilityResult).build();
	}
	
	@Secured
	@POST
	@Path("/create")
	public Response createOffer(Offer offer) {
		OfferBusinessService offerBusinessService = new OfferBusinessService();
		int id = offerBusinessService.createOffer(offer);
		return Response.ok(id).build();
	}

}
