/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/domain/EntityMeta_.e.vm.java
 */
package com.jaxio.demo.domain;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AppToken.class)
public abstract class AppToken_ {

    // Raw attributes
    public static volatile SingularAttribute<AppToken, String> id;
    public static volatile SingularAttribute<AppToken, String> tokenValue;
    public static volatile SingularAttribute<AppToken, Date> tokenCreationDate;
    public static volatile SingularAttribute<AppToken, String> ipAddress;
    public static volatile SingularAttribute<AppToken, String> userAgent;
    public static volatile SingularAttribute<AppToken, String> userLogin;
}