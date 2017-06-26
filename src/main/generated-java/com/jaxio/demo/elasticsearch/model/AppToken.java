/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/elasticsearch/model/ElasticsearchEntity.e.vm.java
 */
package com.jaxio.demo.elasticsearch.model;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * entity AppToken for Elasticsearch.
 *
 */
@Document(indexName = "apptoken_index", type = "appTokens")
public class AppToken {

    private String id;
    private String tokenValue;
    private Date tokenCreationDate;
    private String ipAddress;
    private String userAgent;
    private String userLogin;

    /**
     * Default constructor.
     */
    public AppToken() {
        super();
    }

    public AppToken(String id, String tokenValue, Date tokenCreationDate, String ipAddress, String userAgent, String userLogin) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.tokenCreationDate = tokenCreationDate;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.userLogin = userLogin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public void setTokenCreationDate(Date tokenCreationDate) {
        this.tokenCreationDate = tokenCreationDate;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "AppToken{" + "id='" + id + '\'' + ", id='" + id + '\'' + ", tokenValue='" + tokenValue + '\'' + ", tokenCreationDate='" + tokenCreationDate
                + '\'' + ", ipAddress='" + ipAddress + '\'' + ", userAgent='" + userAgent + '\'' + ", userLogin='" + userLogin + '\'' + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((tokenValue == null) ? 0 : tokenValue.hashCode());
        result = prime * result + ((tokenCreationDate == null) ? 0 : tokenCreationDate.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
        result = prime * result + ((userLogin == null) ? 0 : userLogin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        AppToken other = (AppToken) obj;

        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (tokenValue == null) {
            if (other.tokenValue != null) {
                return false;
            }
        } else if (!tokenValue.equals(other.tokenValue)) {
            return false;
        }
        if (tokenCreationDate == null) {
            if (other.tokenCreationDate != null) {
                return false;
            }
        } else if (!tokenCreationDate.equals(other.tokenCreationDate)) {
            return false;
        }
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        if (userAgent == null) {
            if (other.userAgent != null) {
                return false;
            }
        } else if (!userAgent.equals(other.userAgent)) {
            return false;
        }
        if (userLogin == null) {
            if (other.userLogin != null) {
                return false;
            }
        } else if (!userLogin.equals(other.userLogin)) {
            return false;
        }

        return true;
    }
}
