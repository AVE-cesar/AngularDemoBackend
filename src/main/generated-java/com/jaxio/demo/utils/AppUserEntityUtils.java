/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/utils/EntityUtils.e.vm.java
 */
package com.jaxio.demo.utils;

import java.math.BigDecimal;

import com.jaxio.demo.jpa.model.AppUser;

public class AppUserEntityUtils {

    public static AppUser createNewAppUser(String id) {
        AppUser appUser = new AppUser();
        // FIXME à terminer		
        /*		
        		appUser.setId("Toto");
        		appUser.setFirstName("Toto");
        		appUser.setLastName("Toto");
        		appUser.setEmail("Toto");
        		appUser.setLanguage("Toto");
        		appUser.setLogin("Toto");
        		appUser.setPassword("Toto");
        		appUser.setEnabled("Toto");
        */
        return appUser;
    }

    public static com.jaxio.demo.elasticsearch.model.AppUser createNewElasticsearchAppUser(String id) {
        com.jaxio.demo.elasticsearch.model.AppUser appUser = new com.jaxio.demo.elasticsearch.model.AppUser();

        return appUser;
    }

    public static com.jaxio.demo.elasticsearch.model.AppUser convertToElasticsearchAppUser(AppUser appUser) {
        com.jaxio.demo.elasticsearch.model.AppUser elasticsearchAppUser = new com.jaxio.demo.elasticsearch.model.AppUser();

        elasticsearchAppUser.setId(appUser.getId());
        elasticsearchAppUser.setFirstName(appUser.getFirstName());
        elasticsearchAppUser.setLastName(appUser.getLastName());
        elasticsearchAppUser.setEmail(appUser.getEmail());
        elasticsearchAppUser.setLanguage(appUser.getLanguage());
        elasticsearchAppUser.setLogin(appUser.getLogin());
        elasticsearchAppUser.setPassword(appUser.getPassword());
        elasticsearchAppUser.setEnabled(appUser.getEnabled());

        return elasticsearchAppUser;
    }
}
