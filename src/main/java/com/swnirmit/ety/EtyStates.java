package com.swnirmit.ety;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Data
@Table (name = "State_Master")
public class EtyStates {

	@Id
	@GeneratedValue
	Integer StateID;
	String StateName;
	Integer CountryID;
	
}
