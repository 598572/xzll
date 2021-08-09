//package com.xzll.auth.security.password;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//public class CustomPassword implements PasswordEncoder {
//
//    private final Log logger;
//
//    public CustomPassword() {
//        this.logger = LogFactory.getLog(this.getClass());
//    }
//
//    @Override
//    public String encode(CharSequence charSequence) {
//        return charSequence.toString();
//    }
//
//    @Override
//    public boolean matches(CharSequence charSequence, String encodedPassword) {
//        if (encodedPassword != null && encodedPassword.length() != 0) {
//            return encodedPassword.equals(charSequence.toString());
//        } else {
//            this.logger.warn("Empty encoded password");
//            return false;
//        }
//    }
//}
