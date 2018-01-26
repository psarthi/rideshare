package com.digitusrevolution.rideshare.user.domain.resource;

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
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.service.VehicleDomainService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleDomainResource implements DomainResourceLong<Vehicle>{
	
	@Override
	@Secured
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") long id, @QueryParam("fetchChild") String fetchChild){
		
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		Vehicle vehicle = vehicleDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(vehicle).build();
	}
	
	@Override
	@Secured
	@GET
	public Response getAll(){
		
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		List<Vehicle> vehicles = vehicleDomainService.getAll();
		GenericEntity<List<Vehicle>> entity = new GenericEntity<List<Vehicle>>(vehicles) {};
		return Response.ok(entity).build();
	}
}
