package com.swnirmit.rpo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.swnirmit.ety.EtyUsersMaster;

@Repository
public interface RpoUsersAccounts extends JpaRepository<EtyUsersMaster, String> {

	@Query("SELECT COUNT(*) FROM EtyUsersMaster WHERE UserMailId = ?1")
	Integer isEmailUnicByEmail(String eMailId);

	@Query("SELECT COUNT(*) FROM EtyUsersMaster WHERE UserMailId = ?1 AND UserPassword =?2")
	Integer isTempPwdValid(String eMail, String tempPwd);

	//Below Query not working
	@Modifying()
	@Transactional
	@Query(value = "UPDATE EtyUsersMaster SET UserPassword = :Pwd, AccStatus = 'Active' WHERE UserMailId = :MailID")
	Integer unlockUserAccount(@Param(value = "MailID") String userMailId, @Param(value = "Pwd") String userPassword);

	@Query("FROM EtyUsersMaster WHERE UserMailId =?1")
	EtyUsersMaster findUserByEmail(String eMail);

	@Query("FROM EtyUsersMaster WHERE UserMailId = ?1 AND UserPassword =?2")
	EtyUsersMaster loginCheck(String eMail, String pwd);

	@Query("FROM EtyUsersMaster WHERE UserID =?1")
	EtyUsersMaster findAllByUserId(Integer userId);

}
