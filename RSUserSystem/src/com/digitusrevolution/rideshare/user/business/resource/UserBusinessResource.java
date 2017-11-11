package com.digitusrevolution.rideshare.user.business.resource;

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

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GoogleSignInDTO;
import com.digitusrevolution.rideshare.model.user.dto.SignInDTO;
import com.digitusrevolution.rideshare.model.user.dto.UserBasicInformationDTO;
import com.digitusrevolution.rideshare.model.user.dto.UserDTO;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserBusinessResource {

	/**
	 * 
	 * @param userDTO subset of User domain model
	 * @return userId
	 */
	@POST
	public Response registerUser(UserDTO userDTO){

		UserBusinessService userBusinessService = new UserBusinessService();
		int id = userBusinessService.registerUser(userDTO);
		return Response.ok().entity(Integer.toString(id)).build();
	}

	/**
	 * This will get VehcileBusinessResource Object which can be accessed via UserBusinessResource 
	 * and your path for all VehicleBusinessResources should have prefix of this function path
	 *  
	 * @return VehicleBusinessResource Object
	 */
	@Path("/{id}/vehicles")
	public VehicleBusinessResource getVehicleBusinessResource(){
		return new VehicleBusinessResource();
	}

	/**
	 * 
	 * @param userId Id of the user
	 * @param account account to be added
	 * @return Status OK
	 */
	@POST
	@Path("/{id}/accounts")
	public Response addAccount(@PathParam("id") int userId, Account account){

		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.addAccount(userId, account);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param userId Id of the user
	 * @param emailIds List of email ID's which needs to be searched for
	 * @param mobileNumbers List of mobile Numbers which needs to be searched for
	 * @return List of User Domain models
	 */
	@GET
	@Path("/{id}/potentialfriends")
	public Response findAllPotentialFriendsBasedOnEmailOrMobile(@PathParam("id") int userId, 
			@QueryParam("emailIds") List<String> emailIds, 
			@QueryParam("mobileNumbers") List<String> mobileNumbers){
		
		UserBusinessService userBusinessService = new UserBusinessService();
		List<User> users = userBusinessService.findAllPotentialFriendsBasedOnEmailOrMobile(userId, emailIds, mobileNumbers);
		
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();
	}

	/**
	 * 
	 * @param userId Id of the user
	 * @param friends List of Users whom friend requests needs to be sent
	 * @return status OK
	 */
	@POST
	@Path("/{id}/friendrequest")
	public Response sendFriendRequest(@PathParam("id") int userId, List<User> friends){
		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.sendFriendRequest(userId, friends);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param userId Id of the user
	 * @param friendUserId Id of the friend
	 * @return status OK
	 */
	@POST
	@Path("/{id}/acceptfriendRequest/{friendUserId}")
	public Response acceptFriendRequest(@PathParam("id") int userId, @PathParam("friendUserId") int friendUserId){
		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.acceptFriendRequest(userId, friendUserId);
		return Response.ok().build();		
	}
	
	/**
	 * 
	 * @param userId Id of the user
	 * @param friendUserId Id of the friend
	 * @return status OK
	 */
	@POST
	@Path("/{id}/rejectfriendrequest/{friendUserId}")
	public Response rejectFriendRequest(@PathParam("id") int userId, @PathParam("friendUserId") int friendUserId){
		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.rejectFriendRequest(userId, friendUserId);
		return Response.ok().build();
	}

	/**
	 * 
	 * @param email Id of the user 
	 * @param password of the user
	 * @return UserBasicInformation with token
	 */
	@POST
	@Path("/signin")
	public Response signIn(SignInDTO loginDTO){
		UserBusinessService userBusinessService = new UserBusinessService();
		UserBasicInformationDTO userBasicInformationDTO = userBusinessService.signIn(loginDTO);
		return Response.ok().entity(userBasicInformationDTO).build();
	}
	
	/**
	 * 
	 * @param email Id of the user
	 * @return UserBasicInformation with token
	 */
	@POST
	@Path("/googlesignin")
	public Response googleSignIn(GoogleSignInDTO googleLoginDTO){
		UserBusinessService userBusinessService = new UserBusinessService();
		UserBasicInformationDTO userBasicInformationDTO = userBusinessService.googleSignIn(googleLoginDTO);
		return Response.ok().entity(userBasicInformationDTO).build();
	}
	
	/**
	 * 
	 * @param email Id of the user
	 * @return boolean status if user exist or not
	 */
	@GET
	@Path("/checkuserexist/{userEmail}")
	public Response checkUserExist(@PathParam("userEmail") String userEmail){
		UserBusinessService userBusinessService = new UserBusinessService();
		boolean status = userBusinessService.checkUserExist(userEmail);
		return Response.ok().entity(status).build();
	}
	
	/**
	 * 
	 * @param Mobile number of the user
	 * @return OTP
	 */
	@GET
	@Path("/getotp/{mobileNumber}")
	public Response getOTP(@PathParam("mobileNumber") String mobileNumber){
		UserBusinessService userBusinessService = new UserBusinessService();
		String OTP = userBusinessService.getOTP(mobileNumber);
		return Response.ok().entity(OTP).build();
	}
	
	/**
	 * 
	 * @param Mobile number of the user
	 * @param OTP
	 * @return boolean status if otp validation is success or failed
	 */
	@GET
	@Path("/validateotp/{mobileNumber}/{otp}")
	public Response validateOTP(@PathParam("mobileNumber") String mobileNumber, 
			@PathParam("otp") String otp){
		UserBusinessService userBusinessService = new UserBusinessService();
		boolean status = userBusinessService.validateOTP(mobileNumber, otp);
		return Response.ok().entity(status).build();
	}


}
