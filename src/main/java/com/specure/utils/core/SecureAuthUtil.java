package com.specure.utils.core;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;

@UtilityClass
public class SecureAuthUtil {
    public String calculateHMAC(final String secret, final String data) {
        try {
            final SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
            final Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            final byte[] rawHmac = mac.doFinal(data.getBytes());
            final String result = new String(Base64.getEncoder().encode(rawHmac));
            return result;
        } catch (final GeneralSecurityException e) {

            System.out.println("Unexpected error while creating hash: " + e.getMessage());
            return "";
        }
    }
}
