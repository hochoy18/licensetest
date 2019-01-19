package com.example.demo.test;

import de.schlichtherle.license.*;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Describe: https://yq.aliyun.com/articles/629115
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/16
 */
public class CreateLicense {
    private static String PRIVATEALIAS = "";
    private static String KEYPWD = "";
    private static String STOREPWD = "";
    private static String SUBJECT = "";
    private static String licpath = "";
    private static String priPath = "";

    private static String issuedTime = "";
    private static String notBefore = "";
    private static String notAfter = "";
    private static String consumerType = "";
    private static int consumerAmount = 0;
    private static String info = "";

    public CreateLicense(String path) {
        setParam(path);
    }

    private final static X500Principal DEFAULTHOLDERANDISSUER =
            new X500Principal("CN=wq,OU=iss,O=iss,L=bj,ST=bj,C=china");
//            new X500Principal("CN=cn、OU=cn、O=cn、C=cn、ST=cn、L=cn");

    public void setParam(String path) {
        Properties prop = new Properties();
        InputStream in = null;
        //getClass().getResourceAsStream(path);

        try {
            in =  getClass().getResourceAsStream(path);
//            in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);
        } catch (IOException e) {
            System.out.println(e);
        }
        PRIVATEALIAS = prop.getProperty("PRIVATEALIAS");
        KEYPWD = prop.getProperty("KEYPWD");
        STOREPWD = prop.getProperty("STOREPWD");
        SUBJECT = prop.getProperty("SUBJECT");
        licpath = prop.getProperty("licpath");
        priPath = prop.getProperty("priPath");

        issuedTime = prop.getProperty("issuedTime");
        notBefore = prop.getProperty("notBefore");
        notAfter = prop.getProperty("notAfter");
        consumerAmount = Integer.valueOf(prop.getProperty("consumerAmount"));
        consumerType = prop.getProperty("consumerType");
        info = prop.getProperty("info");
    }

    private static LicenseParam init() {
        Preferences preferences = Preferences.userNodeForPackage(CreateLicense.class);
        CipherParam cipherParam = new DefaultCipherParam(STOREPWD);
        KeyStoreParam keyStoreParam = new DefaultKeyStoreParam(
                CreateLicense.class, priPath, PRIVATEALIAS, STOREPWD, KEYPWD);
        LicenseParam licenseParam = new DefaultLicenseParam(
                SUBJECT, preferences, keyStoreParam, cipherParam);
        return licenseParam;
    }

    public final static LicenseContent createContent() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LicenseContent content = null;
        content = new LicenseContent();
        content.setSubject(SUBJECT);
        content.setHolder(DEFAULTHOLDERANDISSUER);
        content.setIssuer(DEFAULTHOLDERANDISSUER);
        try {
            content.setIssued(format.parse(issuedTime));
            content.setNotBefore(format.parse(notBefore));
            content.setNotAfter(format.parse(notAfter));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        content.setConsumerType(consumerType);
        content.setConsumerAmount(consumerAmount);
        content.setInfo(info);
        content.setExtra(new Object());
        return content;

    }

    public boolean create() {
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getLicenseManager(init());
            File file = new File(licpath);
            if (!file.exists()) {
                file.createNewFile();
            }
            licenseManager.store(createContent(), new File(licpath));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端证书生成失败");
            return false;
        }

        System.out.println("客户端证书生成成功");
        return true;
    }


}
