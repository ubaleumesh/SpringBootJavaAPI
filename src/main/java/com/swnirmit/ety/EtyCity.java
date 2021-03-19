package com.swnirmit.ety;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "City_Master")
@Data
public class EtyCity {

	
	@Id
	@GeneratedValue
	Integer CityID;
	String CityName;
	Integer StateID;
}
