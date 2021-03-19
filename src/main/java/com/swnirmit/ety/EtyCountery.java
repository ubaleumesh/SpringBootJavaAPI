package com.swnirmit.ety;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Country_Master")
public class EtyCountery {

	@Id
	@GeneratedValue
	Integer CountryID;
	String CountryCode;
	String CountryName;
	
	
}
