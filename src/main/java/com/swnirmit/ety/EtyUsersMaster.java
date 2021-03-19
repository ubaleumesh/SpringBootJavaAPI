package com.swnirmit.ety;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
@Table(name = "User_Master")
public class EtyUsersMaster {

	@Id
	@GeneratedValue
	Integer UserID;
	String AccStatus = "Locked";
	Integer CityID;
	Integer CountryID;

	@Temporal(TemporalType.TIMESTAMP)
	Date CreatedDate;

	Date Dob;
	String FristName;
	String Gender;
	Integer StateID;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date UpdatedDate;
	
	String UserMailId;
	String LastName;
	String UserPassword;
	String UserMobile;

}
