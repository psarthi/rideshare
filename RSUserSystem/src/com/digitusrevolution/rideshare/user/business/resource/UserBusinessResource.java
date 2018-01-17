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
import javax.ws.rs.core.Response.Status;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.common.ResponseMessage.Code;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Preference;
import com.digitusrevolution.rideshare.model.user.domain.RegistrationType;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.FullUser;
import com.digitusrevolution.rideshare.model.user.dto.GoogleSignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupListType;
import com.digitusrevolution.rideshare.model.user.dto.SignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserProfile;
import com.digitusrevolution.rideshare.model.user.dto.UserSignInResult;
import com.digitusrevolution.rideshare.model.user.dto.UserStatus;
import com.digitusrevolution.rideshare.model.user.dto.UserRegistration;
import com.digitusrevolution.rideshare.user.business.GroupBusinessService;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;
import com.digitusrevolution.rideshare.user.domain.service.UserDomainService;

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
		UserSignInResult userSignInResult;
		if (userRegistration.getRegistrationType().equals(RegistrationType.Google)) {
			GoogleSignInInfo googleSignInInfo = new GoogleSignInInfo();
			googleSignInInfo.setEmail(userRegistration.getEmail());
			userSignInResult = userBusinessService.googleSignIn(googleSignInInfo);
		}
		else {
			SignInInfo signInInfo = new SignInInfo();
			signInInfo.setEmail(userRegistration.getEmail());
			signInInfo.setPassword(userRegistration.getPassword());
			userSignInResult = userBusinessService.signIn(signInInfo);			
		}		
		return Response.ok().entity(userSignInResult).build();
	}

	/**
	 * This will get VehcileBusinessResource Object which can be accessed via UserBusinessResource 
	 * and your path for all VehicleBusinessResources should have prefix of this function path
	 *  
	 * @return VehicleBusinessResource Object
	 */
	@Path("/{userId}/vehicles")
	public VehicleBusinessResource getVehicleBusinessResource(){
		return new VehicleBusinessResource();
	}
	
	/**
	 * This will get GroupBusinessResource Object which can be accessed via UserBusinessResource 
	 * and your path for all GroupBusinessResource should have prefix of this function path
	 *  
	 * @return GroupBusinessResource Object
	 */
	@Path("/{userId}/groups")
	public GroupBusinessResource getGroupBusinessResource(){
		return new GroupBusinessResource();
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
	 * @param access token
	 * @return UserBasicInformation with token
	 */
	@POST
	@Secured
	@Path("/signinwithtoken")
	public Response signInWithToken(String token){
		UserBusinessService userBusinessService = new UserBusinessService();
		UserSignInResult userSignInResult = userBusinessService.signInWithToken(token);
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
		UserStatus userStatus = userBusinessService.checkUserExist(userEmail);
		return Response.ok().entity(userStatus).build();
	}
	
	/**
	 * 
	 * @param Mobile number of the user
	 * @return OTP
	 */
	@GET
	//@Secured
	@Path("/getotp/{mobileNumber}")
	public Response getOTP(@PathParam("mobileNumber") String mobileNumber){
		UserBusinessService userBusinessService = new UserBusinessService();
		String OTP = userBusinessService.getOTP(mobileNumber);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setResult(OTP);
		return Response.ok().entity(responseMessage).build();			
	}
	
	/**
	 * 
	 * @param Mobile number of the user
	 * @param OTP
	 * @return boolean status if otp validation is success or failed
	 */
	@GET
	//@Secured
	@Path("/validateotp/{mobileNumber}/{otp}")
	public Response validateOTP(@PathParam("mobileNumber") String mobileNumber, 
			@PathParam("otp") String otp){
		UserBusinessService userBusinessService = new UserBusinessService();
		boolean status = userBusinessService.validateOTP(mobileNumber, otp);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setResult(Boolean.toString(status));
		return Response.ok().entity(responseMessage).build();
	}

	/**
	 * 
	 * @param userId Id of the user
	 * @param Preference to be updated
	 * @return Updated User with its preference
	 */
	@POST
	@Path("/{id}/preference")
	public Response updateUserPrefernce(@PathParam("id") int userId, Preference preference){

		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.updateUserPreference(userId, preference);
		UserDomainService userDomainService = new UserDomainService();
		User user = userDomainService.get(userId, false);
		BasicUser basicUser = JsonObjectMapper.getMapper().convertValue(user, BasicUser.class);
		return Response.ok().entity(basicUser).build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild){
		UserBusinessService userBusinessService = new UserBusinessService();
		FullUser user = userBusinessService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(user).build();
	}
	
	/*
	 * userId is the Id of the user for which feedback is given
	 * userFeedbackInfo will contain the information about who has given the feedback, ride, ride request etc.
	 * 
	 */
	@POST
	@Path("/{userId}/feedback")
	public Response addUserFeedback(@PathParam("userId") int userId, UserFeedbackInfo userFeedbackInfo, @QueryParam("rideType") RideType rideType) {
		UserBusinessService userBusinessService = new UserBusinessService();
		userBusinessService.addUserFeedback(userId, userFeedbackInfo);
		//We are fetching the updated Ride / Ride Request so that we can refresh the page accordingly
		if (rideType.equals(RideType.OfferRide)) {
			Ride ride = RESTClientUtil.getRide(userFeedbackInfo.getRide().getId());
			FullRide fullRide = JsonObjectMapper.getMapper().convertValue(ride, FullRide.class);
			return Response.ok(fullRide).build();
		} else {
			RideRequest rideRequest = RESTClientUtil.getRideRequest(userFeedbackInfo.getRideRequest().getId());
			FullRideRequest fullRideRequest = JsonObjectMapper.getMapper().convertValue(rideRequest, FullRideRequest.class);
			return Response.ok(fullRideRequest).build();
		}
	}
	
	@GET
	@Path("/{userId}/profile")
	public Response getUserProfile(@PathParam("userId") int userId) {
		UserBusinessService userBusinessService = new UserBusinessService();
		UserProfile userProfile = userBusinessService.getUserProfile(userId);
		return Response.ok(userProfile).build();
	}
	
	@GET
	@Path("/{userId}/search")
	public Response searchUserByName(@QueryParam("name") String name, @QueryParam("page") int page){
		UserBusinessService userBusinessService = new UserBusinessService();
		List<BasicUser> users = userBusinessService.searchUserByName(name, page);
		GenericEntity<List<BasicUser>> entity = new GenericEntity<List<BasicUser>>(users) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/{userId}/membershiprequests")
	public Response getUserMembershipRequests(@PathParam("userId") int userId, @QueryParam("page") int page){
		UserBusinessService userBusinessService = new UserBusinessService();
		List<BasicMembershipRequest> membershipRequests = userBusinessService.getUserMembershipRequests(userId, page);
		GenericEntity<List<BasicMembershipRequest>> entity = new GenericEntity<List<BasicMembershipRequest>>(membershipRequests) {};
		return Response.ok(entity).build();
	}
}















