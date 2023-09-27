package com.keenetic.account.keycloak.md5;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MD5PasswordHashProviderTest {

  @Test
  public void encode() {

    MD5PasswordHashProvider mdd = new MD5PasswordHashProvider("md5");
    
    assertEquals(
            "df5ea29924d39c3be8785734f13169c6", 
            mdd.encodedCredential("blabla", 0).getPasswordSecretData().getValue()
    );
    assertEquals(
            "0dd7cb886ee8bbe82a9ea7ab2dc37efa", 
            mdd.encodedCredential("asadd", 0).getPasswordSecretData().getValue()
    );

  }
}
