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
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.user.domain.service.VehicleCategoryDomainService;

@Path("/domain/vehiclecategories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleCategoryDomainResource implements DomainResource<VehicleCategory>{

	@GET
	@Path("/{id}")
	@Override
	public Response get(@PathParam("id") int id) {
		VehicleCategoryDomainService vehicleCategoryDomainService = new VehicleCategoryDomainService();
		VehicleCategory vehicleCategory = vehicleCategoryDomainService.get(id);
		return Response.ok(vehicleCategory).build();
	}

	@Override
	@GET
	public Response getAll() {
		VehicleCategoryDomainService vehicleCategoryDomainService = new VehicleCategoryDomainService();
		List<VehicleCategory> vehicleCategories = vehicleCategoryDomainService.getAll();
		GenericEntity<List<VehicleCategory>> entity = new GenericEntity<List<VehicleCategory>>(vehicleCategories) {};
		return Response.ok(entity).build();
	}

}
