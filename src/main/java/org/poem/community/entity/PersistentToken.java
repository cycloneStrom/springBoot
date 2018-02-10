package org.poem.community.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.poem.core.entity.IdEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by poem on 2016/6/18.
 */
@Entity
@Table(name = "JHI_PERSISTENT_TOKEN")
public class PersistentToken extends IdEntity {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("d MMMM yyyy");

    private static final int MAX_USER_AGENT_LEN = 255;


    @JsonIgnore
    @NotNull
    @Column(name = "TOKEN_VALUE", nullable = false)
    private String tokenValue;

    @JsonIgnore
    @Column(name = "TOKEN_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate tokenDate;

    //an IPV6 address max length is 39 characters
    @Size(min = 0, max = 39)
    @Column(name = "IP_ADDRESS", length = 39)
    private String ipAddress;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SECURITY_ACCOUNT_ID")
    private PlatformSecurityAccount platformSecurityAccount;


    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public LocalDate getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(LocalDate tokenDate) {
        this.tokenDate = tokenDate;
    }

    @JsonGetter
    public String getFormattedTokenDate() {
        return DATE_TIME_FORMATTER.print(this.tokenDate);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        if (userAgent.length() >= MAX_USER_AGENT_LEN) {
            this.userAgent = userAgent.substring(0, MAX_USER_AGENT_LEN - 1);
        } else {
            this.userAgent = userAgent;
        }
    }

    public PlatformSecurityAccount getPlatformSecurityAccount() {
        return platformSecurityAccount;
    }

    public void setPlatformSecurityAccount(PlatformSecurityAccount platformSecurityAccount) {
        this.platformSecurityAccount = platformSecurityAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentToken that = (PersistentToken) o;

        if (!getId().equals(that.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "PersistentToken{" +
                "series='" + getId() + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", tokenDate=" + tokenDate +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                "}";
    }
}

