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

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GoogleSignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.SignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserSignInResult;
import com.digitusrevolution.rideshare.model.user.dto.UserRegistration;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserBusinessResource {

	/**
	 * 
	 * @param userDTO subset of User domain model
	 * @return UserBasicInformation with token
	 */
	@POST
	public Response registerUser(UserRegistration userRegistration){
		
		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.registerUser(userRegistration);
		//This is very important - Below code can't be placed in registerUser function in business service class as until n unless transaction is committed, 
		//user information is not available from RideSystem which is trying to fetch upcoming ride related information on REST call. So by putting the statement below
		//we are ensure first above transaction is completed and new transaction would be initiated for sign in
		SignInInfo signInInfo = new SignInInfo();
		signInInfo.setEmail(userRegistration.getEmail());
		signInInfo.setPassword(userRegistration.getPassword());
		UserSignInResult userSignInResult = userBusinessService.signIn(signInInfo);
		return Response.ok().entity(userSignInResult).build();
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
	public Response signIn(SignInInfo loginDTO){
		UserBusinessService userBusinessService = new UserBusinessService();
		UserSignInResult userSignInResult = userBusinessService.signIn(loginDTO);
		return Response.ok().entity(userSignInResult).build();
	}
	
	/**
	 * 
	 * @param email Id of the user
	 * @return UserBasicInformation with token
	 */
	@POST
	@Path("/googlesignin")
	public Response googleSignIn(GoogleSignInInfo googleSignInInfo){
		UserBusinessService userBusinessService = new UserBusinessService();
		UserSignInResult userSignInResult = userBusinessService.googleSignIn(googleSignInInfo);
		return Response.ok().entity(userSignInResult).build();
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
	@Secured
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
	@Secured
	@Path("/validateotp/{mobileNumber}/{otp}")
	public Response validateOTP(@PathParam("mobileNumber") String mobileNumber, 
			@PathParam("otp") String otp){
		UserBusinessService userBusinessService = new UserBusinessService();
		boolean status = userBusinessService.validateOTP(mobileNumber, otp);
		return Response.ok().entity(status).build();
	}


}
