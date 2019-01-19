package com.wbtech.client;

import com.wbtech.licensebase.MDefaultLicenseParam;
import com.wbtech.licensebase.MLicenseManager;
import de.schlichtherle.license.*;

import java.io.*;
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
    private String confPath=System.getProperty("user.dir") + "/client/src/main/resources/licenseVertifyConf.properties";//"/licenseVertifyConf.properties";

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.onlykey = onlykey;
        pubAlias = prop.getProperty("public.alias");
        keyStorePwd = prop.getProperty("key.store.pwd");
        licName = prop.getProperty("license.name");
        pubPath = prop.getProperty("public.store.path");
//        pubPath = (System.getProperty("user.dir")  + "/client/src/main/resources"+prop.getProperty("public.store.path")).replaceAll("\\\\","/");
//        pubPath = "E:/work/wbkit/project/Cobub-Java/licensetest/client/src/main/resources/myPublicCerts01.store";
//        pubPath = (LicenseVertify.class.getResource("/").getPath() + "myPublicCerts01.store").replaceFirst("/","");
        System.out.println(pubPath);
    }

    private MDefaultLicenseParam initLicenseParams(String modelName){
        Class<LicenseVertify> clazz = LicenseVertify.class;
        Preferences pre = Preferences.userNodeForPackage(clazz);
        CipherParam cipherParam = new DefaultCipherParam(keyStorePwd);
        KeyStoreParam pubStoreParam = new DefaultKeyStoreParam(
                clazz,pubPath,pubAlias,keyStorePwd,null);

        MDefaultLicenseParam licenseParam = new MDefaultLicenseParam(
                onlykey,pre,pubStoreParam,cipherParam,modelName);
        return licenseParam;
    }

    private LicenseManager getLicenseManager(String modelName){
        return  new MLicenseManager(initLicenseParams(modelName),modelName);
        //return LicenseManagerHolder.getLicenseManager(initLicenseParams());
    }

    public void install(String licdir,String modelName) {
        try {
            LicenseManager licenseManager = getLicenseManager(modelName);
            //path=D:\eclipse_mars_workspace\LicenseTest\wlicense.lic
            System.out.println("path="+(licdir+File.separator+licName));
            String path = (licdir+File.separator+licName).replaceAll("\\\\","/");
            System.out.println("license path ..... "+path);
            File file = new File(path);
            if (!file.exists()){
                System.out.println("xxxxxxxxxxx file "+file+" not exit");
            }
            licenseManager.install(file);
            System.out.println("安装证书成功！");
        } catch (Exception e) {
            System.out.println("安装证书失败！");
            e.printStackTrace();
//            System.exit(0);
        }
    }
    public boolean vertify(String modelName){
        try {
            LicenseManager licenseManager=getLicenseManager(modelName);
            licenseManager.verify();
            System.out.println("验证证书成功！");
            return true;
        }catch (Exception e) {
            System.out.println("验证证书失败！");
//            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception
    {
        LicenseVertify vlicense=new LicenseVertify("happy"); // 项目唯一识别码，对应生成配置文件的subject
        vlicense.install(System.getProperty("user.dir")+File.separator + "client/src/main/resources","app");  //D:\eclipse_mars_workspace\LicenseTest
        vlicense.vertify("mini");
    }

}
