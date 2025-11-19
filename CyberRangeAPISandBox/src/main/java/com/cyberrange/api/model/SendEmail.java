package com.cyberrange.api.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "sendemail")
public class SendEmail {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    // from or to
    @Column(name = "type")
	private String type;

    @Column(name = "email_address")
	private String emailAddress;

    @Column(name = "verification_code")
	private String verificationCode;

    @Column(name = "verified")
	private int verified;

    public Long getId(){
        return this.id;
    }

    public int isVerified(){
        return this.verified;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }
    public String getVerificationCode(){
        return this.verificationCode;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
    public void setVerificationCode(String code){
        this.verificationCode = code;
    }
    public void setVerified(int verified){
        this.verified = verified;
    }

}
