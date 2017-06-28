/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/test/java/utils/JsonUtils.p.vm.java
 */
package com.jaxio.demo.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    /**
     * Converts an object into its JSON representation as an array of bytes. 
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public final static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}