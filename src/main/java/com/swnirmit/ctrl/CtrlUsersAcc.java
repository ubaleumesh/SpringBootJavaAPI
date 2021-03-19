package com.swnirmit.ctrl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swnirmit.ety.EtyUsersMaster;
import com.swnirmit.srvc.SrvcUsersAccounts;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "*")
@RestController("/api/user")
public class CtrlUsersAcc {
//jenkin test
	private SrvcUsersAccounts srvc_UsersAccount;

	public CtrlUsersAcc(SrvcUsersAccounts srvc_UsersAccount) {
		this.srvc_UsersAccount = srvc_UsersAccount;
	}

	@PostMapping
	public ResponseEntity<String> RegisterUser(@RequestBody EtyUsersMaster userAccount) {

		boolean IsUnic = srvc_UsersAccount.isEmailUnic(userAccount.getUserMailId());
		if (IsUnic) {

			// Require to Genrate randem tempPassword;
			String genratedString = RandomString.make(10);
			userAccount.setUserPassword(genratedString);
			userAccount.setAccStatus("Locked");
			userAccount.setUpdatedDate(new Date());
			userAccount.setCreatedDate(new Date());

			boolean isSaved = srvc_UsersAccount.saveUser(userAccount);
			if (isSaved) {
				return new ResponseEntity<>("Registration Successfull Please Check Your Mail", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Registration Fail", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("E-Mail Allready Exits", HttpStatus.OK);
		}

	}

	@PostMapping("/registerUser")
	public ResponseEntity<String> RegisterUser1(@RequestParam Integer CountryId, Integer StateID, Integer CityId,
			Date Dob, String FristName, String LastName, String eMail, String MobileNo, String Gender) {

		boolean IsUnic = srvc_UsersAccount.isEmailUnic(eMail);
		if (IsUnic) {

			EtyUsersMaster userAccount = new EtyUsersMaster();
			// Require to Genrate randem tempPassword;
			String genratedString = RandomString.make(10);
			userAccount.setCountryID(CountryId);
			userAccount.setStateID(StateID);
			userAccount.setCityID(CityId);
			userAccount.setDob(Dob);
			userAccount.setFristName(FristName);
			userAccount.setLastName(LastName);
			userAccount.setUserMailId(eMail);
			userAccount.setUserMobile(MobileNo);
			userAccount.setUserPassword(genratedString);
			userAccount.setAccStatus("Locked");
			userAccount.setGender(Gender);
			userAccount.setUpdatedDate(new Date());
			userAccount.setCreatedDate(new Date());

			boolean isSaved = srvc_UsersAccount.saveUser(userAccount);
			if (isSaved) {
				return new ResponseEntity<>("Registration Successfull Please Check Your Mail", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Registration Fail", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("E-Mail Allready Exits", HttpStatus.OK);
		}

	}

	@PutMapping("/UnlockAccount")
	public ResponseEntity<String> UnlockAccount(@RequestParam String tmpPwd, String eMail, String newPwd) {
		System.out.println(tmpPwd);
		System.out.println(eMail);
		System.out.println(newPwd);

		System.out.println("--------------------------------");

		boolean tempPwdStatus = srvc_UsersAccount.isTempPwdValid(eMail, tmpPwd);
		System.out.println(tempPwdStatus);

		if (tempPwdStatus == true) {

			boolean unLockStatus = srvc_UsersAccount.unlockUserAccounts(eMail, newPwd);
			if (unLockStatus == true) {
				return new ResponseEntity<>("User Account Unlock Seccussfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Somethink went rong", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Temp Password is Invalid", HttpStatus.BAD_REQUEST);
		}

		// 1) Check isTempPwdValid
		// 2) update unlockUserAccounts

	}

	@PostMapping("/userSignIn")
	public ResponseEntity<String> userSignIn(@RequestParam String eMail, String uPassword) {
		String LoginMessage = srvc_UsersAccount.loginCheck(eMail, uPassword);
		return new ResponseEntity<>(LoginMessage, HttpStatus.OK);
	}

	// I Get only Trial
	@PostMapping("/isEmailUnic/{eMail}")
	public ResponseEntity<String> isEmailUnic(@PathVariable String eMail) {
		System.out.println(eMail);
		String eMailStatus = "false";
		boolean IsUnic = srvc_UsersAccount.isEmailUnic(eMail);
		if (IsUnic) {
			eMailStatus = "true";
		}
		return new ResponseEntity<>(eMailStatus, HttpStatus.OK);
	}

	@PostMapping("/forgotPassword/{eMail}")
	public ResponseEntity<String> forgotPassword(@PathVariable String eMail) {
		boolean isSend = srvc_UsersAccount.forgetPassword(eMail);
		if (isSend) {
			return new ResponseEntity<>("Your New Password Sent To Your Mail", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Please Enter Correct Registered Mail ID", HttpStatus.NOT_FOUND);
		// Send Password to User Mail.
	}

	@GetMapping("/countries")
	public Map<String, Integer> getCountries() {

		return srvc_UsersAccount.getCountries();
	}

	@GetMapping("/states/{counteryId}")
	public Map<String, Integer> getStatesByCountry(@PathVariable Integer counteryId) {
		return srvc_UsersAccount.getStatesByCountryId(counteryId);
	}

	@GetMapping("/city/{stateId}")
	public Map<String, Integer> getCitysByStateId(@PathVariable Integer stateId) {
		return srvc_UsersAccount.getCitysByStateid(stateId);
	}

	@PutMapping("/UnlockAccount1")
	public ResponseEntity<String> UnlockAccount1(@RequestParam String tmpPwd, String eMail, String newPwd) {
		System.out.println(tmpPwd);
		System.out.println(eMail);
		System.out.println(newPwd);

		System.out.println("--------------------------------");

		boolean tempPwdStatus = srvc_UsersAccount.isTempPwdValid(eMail, tmpPwd);
		System.out.println(tempPwdStatus);

		if (tempPwdStatus == true) {
			EtyUsersMaster userAccount = new EtyUsersMaster();
			userAccount = srvc_UsersAccount.findUserByEmail(eMail);
			userAccount.setAccStatus("Active");
			userAccount.setUpdatedDate(new Date());
			userAccount.setUserPassword(newPwd);

			boolean unLockStatus = srvc_UsersAccount.saveUser(userAccount);
			if (unLockStatus == true) {
				return new ResponseEntity<>("User Account Unlock Seccussfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Somethink went rong", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Temp Password is Invalid", HttpStatus.BAD_REQUEST);
		}

		// 1) Check isTempPwdValid
		// 2) update unlockUserAccounts

	}

	@GetMapping("/getAllUser")
	public List<EtyUsersMaster> getAllUser() {
		return srvc_UsersAccount.getAllUser();
	}

	@GetMapping("/getUserByID/{UserId}")
	public EtyUsersMaster getUserByID(Integer UserId) {
		EtyUsersMaster userAccount = new EtyUsersMaster();
		userAccount = srvc_UsersAccount.getUserByID(UserId);
		return userAccount;
	}

	@PostMapping("/TestMail/{eMail}")
	public ResponseEntity<String> sendTestmail(@PathVariable String eMail) {
		boolean isMailSent = srvc_UsersAccount.TestMail(eMail);
		if (isMailSent) {
			return new ResponseEntity<>("Test E-Mail Sent Seccessfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Somethink went rong", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/StaticPath")
	public String getStaticPath() {
		String userDirectory = System.getProperty("java.class.path");
		System.out.println("Current Directory Controler : " + userDirectory);
		return srvc_UsersAccount.getStaticPath() + " || Current Directory Controler java.class.path : " + userDirectory;
	}

}
