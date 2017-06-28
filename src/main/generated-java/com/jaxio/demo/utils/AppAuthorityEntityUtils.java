/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/utils/EntityUtils.e.vm.java
 */
package com.jaxio.demo.utils;

import java.math.BigDecimal;

import com.jaxio.demo.jpa.model.AppAuthority;

public class AppAuthorityEntityUtils {

    public static AppAuthority createNewAppAuthority(String id) {
        AppAuthority appAuthority = new AppAuthority();
        // FIXME à terminer		
        /*		
        		appAuthority.setId("Toto");
        		appAuthority.setName("Toto");
        */
        return appAuthority;
    }

    public static com.jaxio.demo.elasticsearch.model.AppAuthority createNewElasticsearchAppAuthority(String id) {
        com.jaxio.demo.elasticsearch.model.AppAuthority appAuthority = new com.jaxio.demo.elasticsearch.model.AppAuthority();

        return appAuthority;
    }

    public static com.jaxio.demo.elasticsearch.model.AppAuthority convertToElasticsearchAppAuthority(AppAuthority appAuthority) {
        com.jaxio.demo.elasticsearch.model.AppAuthority elasticsearchAppAuthority = new com.jaxio.demo.elasticsearch.model.AppAuthority();

        elasticsearchAppAuthority.setId(appAuthority.getId());
        elasticsearchAppAuthority.setName(appAuthority.getName());

        return elasticsearchAppAuthority;
    }
}