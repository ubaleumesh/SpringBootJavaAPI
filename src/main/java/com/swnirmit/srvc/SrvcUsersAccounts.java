package com.swnirmit.srvc;

import java.util.List;
import java.util.Map;

import com.swnirmit.ety.EtyUsersMaster;

public interface SrvcUsersAccounts {

	// Location Methods
	public Map<String, Integer> getCountries();

	public Map<String, Integer> getStatesByCountryId(Integer CountryId);

	public Map<String, Integer> getCitysByStateid(Integer StateId);

	// for Check Mail is Tunic
	public boolean isEmailUnic(String eMailId);

	// For Save Or Update Record
	public boolean saveUser(EtyUsersMaster ety_UsersAcc);

	// for check provided temp password is valid or not
	public boolean isTempPwdValid(String eMail, String tempPwd);

	// for unlock user account
	public boolean unlockUserAccounts(String eMail, String newPwd);

	// For Get Single Record
	public EtyUsersMaster findUserByEmail(String eMail);

	// for check UserId and Password is correct
	public String loginCheck(String eMail, String pwd);

	// Forget Password
	public boolean forgetPassword(String eMail);

	// TestMail Send
	public boolean TestMail(String eMail);
	
	//Get all User List
	public List<EtyUsersMaster> getAllUser();
	
	//Get all UserDetails by Id
	public EtyUsersMaster getUserByID(Integer UserId);
	
	//Test Static Path path in java
	public String getStaticPath ();

}