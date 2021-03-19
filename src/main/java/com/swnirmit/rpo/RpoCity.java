package com.swnirmit.rpo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swnirmit.ety.EtyCity;

@Repository
public interface RpoCity extends JpaRepository<EtyCity, Integer>{

	@Query("FROM EtyCity cm WHERE cm.StateID = ?1")
	List<EtyCity> getCityByStateId(Integer stateId);

}
