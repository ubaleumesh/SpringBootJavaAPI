package com.swnirmit.rpo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swnirmit.ety.EtyCountery;

@Repository
public interface RpoCountry extends JpaRepository<EtyCountery, Integer> {

}
