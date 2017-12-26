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

import com.digitusrevolution.rideshare.common.inf.DomainResource;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.user.domain.service.VehicleSubCategoryDomainService;

@Path("/domain/vehiclesubcategories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleSubCategoryDomainResource implements DomainResource<VehicleSubCategory>{

	@GET
	@Path("/{id}")
	@Override
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild) {
		VehicleSubCategoryDomainService vehicleSubCategoryDomainService = new VehicleSubCategoryDomainService();
		VehicleSubCategory vehicleSubCategory = vehicleSubCategoryDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(vehicleSubCategory).build();
	}

	@Override
	@GET
	public Response getAll() {
		VehicleSubCategoryDomainService vehicleSubCategoryDomainService = new VehicleSubCategoryDomainService();
		List<VehicleSubCategory> vehicleSubCategories = vehicleSubCategoryDomainService.getAll();
		GenericEntity<List<VehicleSubCategory>> entity = new GenericEntity<List<VehicleSubCategory>>(vehicleSubCategories) {};
		return Response.ok(entity).build();
	}
}
