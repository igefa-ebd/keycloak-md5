/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keenetic.account.keycloak.md5;

// import org.jboss.logging.Logger;
import java.math.BigInteger;
import java.security.MessageDigest;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.credential.PasswordCredentialModel;

/**
 * @author <a href="mailto:hokum@dived.me">Andrey Kotov</a>
 */
public class MD5PasswordHashProvider implements PasswordHashProvider {

    // https://github.com/keycloak/keycloak/blob/master/server-spi-private/src/main/java/org/keycloak/credential/hash/Pbkdf2PasswordHashProvider.java
    private final String providerId;

    public MD5PasswordHashProvider(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        // no need to check hash iterations, as MD5 doesn't use it
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        String hashedPassword = "";
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rawPassword.getBytes());
            BigInteger digestInt = new BigInteger(1, md.digest());
            StringBuilder sbZeroes = new StringBuilder(digestInt.toString(16));
            while (sbZeroes.length() < 32) { // add leading zeroes to 32 chars
                sbZeroes.insert(0, "0");
            }
            hashedPassword = sbZeroes.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        
        return PasswordCredentialModel.createFromValues(
                providerId, new byte[0], 0, hashedPassword
        );
    }

    @Override
    public void encode(String rawPassword, int iterations, CredentialModel credential) {
        String password = this.encodedCredential(rawPassword, iterations).getSecretData();
        credential.setAlgorithm(providerId);
        credential.setType(UserCredentialModel.PASSWORD);
        credential.setHashIterations(0);
        credential.setValue(password);
        credential.setSalt(new byte[0]);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        String given = this.encodedCredential(rawPassword, 0).getPasswordSecretData().getValue();
        String expected = credential.getPasswordSecretData().getValue();
        return given.equals(expected);
    }

}
