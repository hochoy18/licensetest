//package com.example.demo.test;
//
//import de.schlichtherle.license.*;
//
//import java.io.*;
//import java.util.Properties;
//import java.util.prefs.Preferences;
//
///**
// * Describe:
// *
// * @author hochoy <hochoy18@sina.com>
// * @version V1.0.0
// * @date 2019/1/16
// */
//public class VerifyLicense {
//    private static String PUBLICALIAS = "";
//    private static String STOREPWD = "";
//    private static String SUBJECT = "";
//    private static String licPath = "";
//    private static String pubPath = "";
//
//    public void setParam(String path) {
//        Properties prop = new Properties();
//        InputStream in = null;
//        try {
//            in = new BufferedInputStream(new FileInputStream(path));
//            prop.load(in);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//        PUBLICALIAS = prop.getProperty("PUBLICALIAS");
//        STOREPWD = prop.getProperty("STOREPWD");
//        SUBJECT = prop.getProperty("SUBJECT");
//        licPath = prop.getProperty("licPath");
//        pubPath = prop.getProperty("pubPath");
//    }
//
//    private static LicenseParam init() {
//        Preferences preferences = Preferences.userNodeForPackage(VerifyLicense.class);
//        CipherParam cipher = new DefaultCipherParam(STOREPWD);
//
//        KeyStoreParam storeParam = new DefaultKeyStoreParam(
//                VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
//        LicenseParam param = new DefaultLicenseParam(SUBJECT,
//                preferences, storeParam, cipher);
//        return param;
//    }
//
//    public boolean veriry() {
//        LicenseManager manager = LicenseManagerHolder.getLicenseManager(init());
//        try {
//            manager.install(new File(licPath));
//            System.out.println("客户端安装证书成功!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("客户端证书安装失败!");
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("客户端证书安装失败!");
//            return false;
//        }
//        try {
//            manager.verify();
//            System.out.println("客户端验证证书成功!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("客户端证书验证失效!");
//            return false;
//        }
//        return true;
//    }
//
//    public static void main(String[] args) {
//        VerifyLicense v = new VerifyLicense();
//        v.setParam("E:\\work\\wbkit\\project\\Cobub-Java\\licensetest\\server\\src\\test\\resources\\verify.properties");
//        v.veriry();
//    }
//}
