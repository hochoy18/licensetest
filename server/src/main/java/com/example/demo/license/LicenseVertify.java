package com.example.demo.license;


import de.schlichtherle.license.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * @author wbmac
 * @Title: LicenseVertify
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午4:02
 */
public class LicenseVertify {
    /**
     * 公钥别名
     */
    private String pubAlias;
    /**
     * 该密码是在使用keytool生成密钥对时设置的密钥库的访问密码
     */
    private String keyStorePwd;
    /**
     * 系统的统一识别码
     */
    private String onlykey;
    /**
     * 证书路径
     */
    private String licName;
    /**
     * 公钥库路径
     */
    private String pubPath;

    private String confPath = System.getProperty("user.dir") + "/server/src/main/resources/licenseVertifyConf.properties";

    public LicenseVertify(String onlykey){
        setConf(confPath,onlykey);
    }

    public LicenseVertify(String confPath,String onlykey){
        setConf(confPath,onlykey);
    }

    public void setConf(String confPath,String onlykey){
        Properties prop = new Properties();
        InputStream in = null;//getClass().getResourceAsStream(confPath);
        try {
            in = new BufferedInputStream(new FileInputStream(confPath));
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.onlykey = onlykey;
        pubAlias = prop.getProperty("public.alias");
        keyStorePwd = prop.getProperty("key.store.pwd");
        licName = prop.getProperty("license.name");
        pubPath = prop.getProperty("public.store.path");
//        pubPath = System.getProperty("user.dir")  + "/server/src/main/resources"+prop.getProperty("public.store.path");
    }

    private LicenseParam initLicenseParams(){
        Class<LicenseVertify> clazz = LicenseVertify.class;
        Preferences pre = Preferences.userNodeForPackage(clazz);
        CipherParam cipherParam = new DefaultCipherParam(keyStorePwd);
        KeyStoreParam pubStoreParam = new DefaultKeyStoreParam(
                clazz,pubPath,pubAlias,keyStorePwd,null);

        LicenseParam licenseParam = new DefaultLicenseParam(
                onlykey,pre,pubStoreParam,cipherParam);
        return licenseParam;
    }

    private LicenseManager getLicenseManager(){
        return LicenseManagerHolder.getLicenseManager(initLicenseParams());
    }

    public void install(String licdir) {
        try {
            LicenseManager licenseManager = getLicenseManager();
            //path=D:\eclipse_mars_workspace\LicenseTest\1license.lic
            System.out.println("path="+(licdir+File.separator+licName));
            File file = new File(licdir+File.separator+licName);
            licenseManager.install(file);
            System.out.println("安装证书成功！");
        } catch (Exception e) {
            System.out.println("安装证书失败！");
            e.printStackTrace();
//            System.exit(0);
        }
    }
    public boolean vertify(){
        try {
            LicenseManager licenseManager=getLicenseManager();
            licenseManager.verify();
            System.out.println("验证证书成功！");
            return true;
        }catch (Exception e) {
            System.out.println("验证证书失败！");
//            System.out.println(e.getLocalizedMessage());
//            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception
    {
        LicenseVertify vlicense=new LicenseVertify("happy"); // 项目唯一识别码，对应生成配置文件的subject
        vlicense.install(System.getProperty("user.dir"));  //D:\eclipse_mars_workspace\LicenseTest
        vlicense.vertify();
    }

}
