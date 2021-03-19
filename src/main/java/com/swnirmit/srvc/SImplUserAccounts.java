package com.swnirmit.srvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swnirmit.config.AES256;
import com.swnirmit.config.eMailBody;
import com.swnirmit.ety.EtyCity;
import com.swnirmit.ety.EtyCountery;
import com.swnirmit.ety.EtyStates;
import com.swnirmit.ety.EtyUsersMaster;
import com.swnirmit.rpo.RpoCity;
import com.swnirmit.rpo.RpoCountry;
import com.swnirmit.rpo.RpoState;
import com.swnirmit.rpo.RpoUsersAccounts;

@Service
public class SImplUserAccounts implements SrvcUsersAccounts {

	@Autowired
	private RpoCountry rpoCountry;

	@Autowired
	private RpoState rpoState;

	@Autowired
	private RpoCity rpoCity;

	@Autowired
	SrvcEMail srvceMail;

	private RpoUsersAccounts rpo_UserAccount;

	public SImplUserAccounts(RpoUsersAccounts rpo_UserAccount) {
		this.rpo_UserAccount = rpo_UserAccount;
	}

	@Override
	public Map<String, Integer> getCountries() {
		List<EtyCountery> countries = rpoCountry.findAll();
		System.out.println(countries);
		Map<String, Integer> map = new HashMap<>();
		for (EtyCountery Country : countries) {
			map.put(Country.getCountryName(), Country.getCountryID());
		}
		return map;
	}

	@Override
	public Map<String, Integer> getStatesByCountryId(Integer CountryId) {

		List<EtyStates> States = rpoState.getStateByCountryId(CountryId);
		Map<String, Integer> map = new HashMap<>();
		for (EtyStates states : States) {
			map.put(states.getStateName(), states.getStateID());
		}
		return map;

	}

	@Override
	public Map<String, Integer> getCitysByStateid(Integer StateId) {

		List<EtyCity> City = rpoCity.getCityByStateId(StateId);
		Map<String, Integer> map = new HashMap<>();
		for (EtyCity city : City) {
			map.put(city.getCityName(), city.getCityID());
		}
		return map;

	}

	@Override
	public boolean isEmailUnic(String eMailId) {

		Integer RcdCount = rpo_UserAccount.isEmailUnicByEmail(eMailId);
		if (RcdCount == null) {
			return true;
		}
		if (RcdCount > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean saveUser(EtyUsersMaster ety_UsersAcc) {

		String PwdNormal = ety_UsersAcc.getUserPassword();
		System.out.println("Normal Password :" + ety_UsersAcc.getUserPassword());
		String EcyPass = AES256.encrypt(ety_UsersAcc.getUserPassword());
		ety_UsersAcc.setUserPassword(EcyPass);
		System.out.println("Encrypted Password :" + ety_UsersAcc.getUserPassword());
		EtyUsersMaster savedAccount = rpo_UserAccount.save(ety_UsersAcc);
		String EcyDecPass = AES256.decrypt(ety_UsersAcc.getUserPassword());
		System.out.println("Dicrypted Password :" + EcyDecPass);
		ety_UsersAcc.setUserPassword(PwdNormal);
		String eMailBody = getUnlockAccMailBody(ety_UsersAcc);

		String subject = "Unlock Your Account | SwaNirmit Technologies";
		boolean isMailSent = srvceMail.unlockUserAcSendMail(subject, eMailBody, ety_UsersAcc.getUserMailId());
		return savedAccount.getUserID() != null && isMailSent;
	}

	@Override
	public boolean isTempPwdValid(String eMail, String tempPwd) {
		Integer RcdCount = 0;
		String EcyPass = AES256.encrypt(tempPwd);
		System.out.println(EcyPass);
		RcdCount = rpo_UserAccount.isTempPwdValid(eMail, EcyPass);
		if (RcdCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean unlockUserAccounts(String eMail, String newPwd) {
		Integer RcdCount = 0;
		String EcyPass = AES256.encrypt(newPwd);
		RcdCount = rpo_UserAccount.unlockUserAccount(eMail, EcyPass);
		if (RcdCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public EtyUsersMaster findUserByEmail(String eMail) {

		EtyUsersMaster userAccount = rpo_UserAccount.findUserByEmail(eMail);
		return userAccount;
	}

	@Override
	public String loginCheck(String eMail, String pwd) {
		String EcyPass = AES256.encrypt(pwd);
		EtyUsersMaster userAccount = rpo_UserAccount.loginCheck(eMail, EcyPass);
		if (userAccount == null) {
			return "Bad User Id Or Password";
		}

		if (userAccount.getAccStatus().equals("Active")) {
			return "Login Seccussfully";
		} else {
			return "User have in Locked Status Please Check your Mail";
		}
	}

	@Override
	public boolean forgetPassword(String eMail) {
		EtyUsersMaster userAccount = new EtyUsersMaster();
		userAccount = findUserByEmail(eMail);
		if (userAccount != null) {
			String EcyDecPass = AES256.decrypt(userAccount.getUserPassword());
			System.out.println("Dicrypted Password :" + EcyDecPass);
			userAccount.setUserPassword(EcyDecPass);
			String eMailBody = getForgetPassMailBody(userAccount);

			String subject = "Your Passowrd | SwaNirmit Technologies";
			boolean isMailSent = srvceMail.unlockUserAcSendMail(subject, eMailBody, userAccount.getUserMailId());
			return isMailSent;
		}
		return false;
	}

	public String getForgetPassMailBody(EtyUsersMaster userAc) {
		String fileName = "";
		/*
		 * String path = new File(".").getCanonicalPath();
		 * System.out.println("Current Directory Get Cononical Path : "+ path);
		 */
		String userDirectory = System.getProperty("user.dir");
		System.out.println("Current Directory : " + userDirectory);

		fileName = userDirectory + "\\src\\main\\resources\\static\\ForgetPassMailBody.txt";

		File f = new File(fileName);
		// Check if the specified file
		// Exists or not
		if (f.exists()) {
			System.out.println("Exists path : " + fileName);
		} else {
			System.out.println("Does not Exists");

			fileName = userDirectory + "\\webapps\\13JRTPM2\\WEB-INF\\classes\\static\\ForgetPassMailBody.txt";
			System.out.println("Check file have on this path : " + fileName);
		}

		// fileName = "ForgetPassMailBody.txt";
		List<String> replaceLines = null;
		String mailBody = null;
		try {
			Path path = Paths.get(fileName, "");
			Stream<String> lines = Files.lines(path);
			replaceLines = lines
					.map(line -> line.replace("{FNAME}", userAc.getFristName()).replace("{LNAME}", userAc.getLastName())
							.replace("{CUR-PWD}", userAc.getUserPassword()).replace("{EMAIL}", userAc.getUserMailId()))
					.collect(Collectors.toList());
			mailBody = String.join("", replaceLines);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;

	}

	public String getUnlockAccMailBody(EtyUsersMaster userAc) {

		String userDirectory = System.getProperty("user.dir");
		System.out.println("Current Directory Unlock Account : " + userDirectory);

		String fileName = userDirectory + "\\src\\main\\resources\\static\\unlock-acc-email-body.txt";
		File f = new File(fileName);
		// Check if the specified file
		// Exists or not
		if (f.exists()) {
			System.out.println("Exists path : " + fileName);
		} else {
			System.out.println("Does not Exists");

			fileName = userDirectory + "\\webapps\\13JRTPM2\\WEB-INF\\classes\\static\\unlock-acc-email-body.txt";
			System.out.println("Check file have on this path : " + fileName);
		}

		List<String> replaceLines = null;
		String mailBody = null;
		try {
			Path path = Paths.get(fileName, "");
			Stream<String> lines = Files.lines(path);
			replaceLines = lines
					.map(line -> line.replace("{FNAME}", userAc.getFristName()).replace("{LNAME}", userAc.getLastName())
							.replace("{TEMP-PWD}", userAc.getUserPassword()).replace("{EMAIL}", userAc.getUserMailId()))
					.collect(Collectors.toList());
			mailBody = String.join("", replaceLines);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;

	}

	@Override
	public boolean TestMail(String eMail) {
		String eMailBody = "This Is Test Mail From 13-JRTP-Project-2 <br/><br/> SwaNirmit Technologies Team <br/>";

		String subject = "Test Mail 13JRTPM2 | SwaNirmit Technologies";
		boolean isMailSent = srvceMail.unlockUserAcSendMail(subject, eMailBody, eMail);
		if (isMailSent) {
			return true;
		}
		return false;
	}

	@Override
	public List<EtyUsersMaster> getAllUser() {
		List<EtyUsersMaster> users = rpo_UserAccount.findAll();
		return users;
	}

	@Override
	public EtyUsersMaster getUserByID(Integer UserId) {
		return rpo_UserAccount.findAllByUserId(UserId);

	}

	@Override
	public String getStaticPath() {
		String userDirectory = System.getProperty("user.dir");
		System.out.println("Current Directory Static Path: " + userDirectory);
		String fileName = userDirectory + "\\src\\main\\resources\\static\\unlock-acc-email-body.txt";

		File f = new File(fileName);
		// Check if the specified file
		// Exists or not
		if (f.exists()) {
			System.out.println("Exists path : " + fileName);
		} else {
			System.out.println("Does not Exists");

			fileName = userDirectory + "\\webapps\\13JRTPM2\\WEB-INF\\classes\\static\\unlock-acc-email-body.txt";
			System.out.println("Check file have on this path : " + fileName);
		}
		return fileName;
	}

}
