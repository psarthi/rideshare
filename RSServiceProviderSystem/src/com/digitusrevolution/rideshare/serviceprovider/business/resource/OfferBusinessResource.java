package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.serviceprovider.business.OfferBusinessService;

@Path("/offers")
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
	public Response getAll(@QueryParam("page") int page){	
		OfferBusinessService offerBusinessService = new OfferBusinessService();
		List<Offer> offers = offerBusinessService.getAll(page);
		GenericEntity<List<Offer>> entity = new GenericEntity<List<Offer>>(offers) {};
		return Response.ok(entity).build();
	}

}
