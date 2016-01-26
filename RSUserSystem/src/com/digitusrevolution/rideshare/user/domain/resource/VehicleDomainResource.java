package com.digitusrevolution.rideshare.user.domain.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.inf.DomainResource;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.service.VehicleDomainService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleDomainResource implements DomainResource<Vehicle>{

	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id){
		
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		Vehicle vehicle = vehicleDomainService.get(id);
		return Response.ok(vehicle).build();
	}
	
	@Override
	@GET
	public Response getAll(){
		
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		List<Vehicle> vehicles = vehicleDomainService.getAll();
		GenericEntity<List<Vehicle>> entity = new GenericEntity<List<Vehicle>>(vehicles) {};
		return Response.ok(entity).build();
	}
}
