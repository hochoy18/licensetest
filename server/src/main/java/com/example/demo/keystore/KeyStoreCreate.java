package com.example.demo.keystore;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/17
 */
public class KeyStoreCreate {
    //System.getProperty("user.dir") +
    public static String filePath =  "server/src/main/resources/new_KeyStore.keystore";
    private static final int keysize = 1024;
    private static final String commonName = "www.ctbri.com";
    private static final String organizationUnit = "IT";
    private static final String organization = "test";
    private static final String city = "beijing";
    private static final String state = "beijing";
    private static final String country = "beijing";
    private static final long validity = 1L;
    private static final String alias = "Hochoy_Key";
    private static final char[] keyPwd = "123456".toCharArray();

    public static void main(String[] args) {
        genKeyStore();
    }

    static void genKeyStore() {
        try {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            ks.load(null);
            CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
            X500Name x500Name = new X500Name(commonName, organizationUnit, organization, city, state, country);
            keypair.generate(keysize);
            PrivateKey privateKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[1];

            chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long) validity * 24 * 60 * 60);
            FileOutputStream fos = new FileOutputStream(filePath);
            ks.setKeyEntry(alias, privateKey, keyPwd, chain);

            ks.store(fos, keyPwd);
            fos.close();
            System.out.println("create Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
