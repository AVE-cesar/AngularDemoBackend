/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/elasticsearch/model/ElasticsearchEntity.e.vm.java
 */
package com.jaxio.demo.elasticsearch.model;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * entity AppTranslation for Elasticsearch.
 *
 */
@Document(indexName = "apptranslation_index", type = "appTranslations")
public class AppTranslation {

    private Integer id;
    private String language;
    private String key;
    private String value;

    /**
     * Default constructor.
     */
    public AppTranslation() {
        super();
    }

    public AppTranslation(Integer id, String language, String key, String value) {
        this.id = id;
        this.language = language;
        this.key = key;
        this.value = value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AppTranslation{" + "id='" + id + '\'' + ", id='" + id + '\'' + ", language='" + language + '\'' + ", key='" + key + '\'' + ", value='" + value
                + '\'' + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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

        AppTranslation other = (AppTranslation) obj;

        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }

        return true;
    }
}
