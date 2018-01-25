package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.user.business.VehicleBusinessService;
import com.digitusrevolution.rideshare.user.domain.service.UserDomainService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleBusinessResource {

	/**
	 * 
	 * This function should be accessed via UserBusinessResource and not directly as this require userId along with Vehicle
	 * 
	 * @param userId Id of the user
	 * @param vehicle Vehicle to be added
	 * @return status OK
	 */

	@Secured
	@POST
	public Response addVehicle(@Context ContainerRequestContext requestContext,
			@PathParam("userId") long userId, Vehicle vehicle){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			VehicleBusinessService vehicleBusinessService = new VehicleBusinessService();
			vehicleBusinessService.addVehicle(userId, vehicle);
			//Reason for invoking in separate transaction so that previous data gets committed and then we get the proper data
			//else there is chances that you may not get updated data
			UserDomainService userDomainService = new UserDomainService();
			User user = userDomainService.get(userId, false);
			BasicUser basicUser = JsonObjectMapper.getMapper().convertValue(user, BasicUser.class);			
			return Response.ok(basicUser).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}

}
