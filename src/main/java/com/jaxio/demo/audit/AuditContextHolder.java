package com.jaxio.demo.audit;

import static org.apache.commons.lang.StringUtils.trimToNull;

import com.jaxio.demo.security.SecurityUtils;

// see https://github.com/moacybarros/app-demo
public class AuditContextHolder {
	
	private static final ThreadLocal<Boolean> audit = new ThreadLocal<Boolean>();
    private static final ThreadLocal<String> username = new ThreadLocal<String>();

    public static void setAudit(boolean auditing) {
        audit.set(auditing);
    }

    public static void setUsername(String user) {
        username.set(trimToNull(user));
    }

    public static boolean audit() {
        return audit.get() == null || audit.get();
    }

    public static String username() {
        return username.get() == null ? SecurityUtils.getCurrentLogin() : username.get();
    }
}
