package com.example.demo.license;

import com.wbtech.licensebase.LicenseCheckModel;
import com.wbtech.licensebase.ModuleProperties;
import de.schlichtherle.license.*;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * @author wbmac
 * @Title: LicenseMake
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午2:33
 */
public class LicenseMake {
    private String licPath;
    private String issued;
    private String notBefore;
    private String notAfter;
    private String consumerType;
    private int consumerAmount;
    private String info;
    /**
     * 私钥的别名
     */
    private String priAlias;
    /**
     * 该密码生成密钥对的密码
     */
    private String privateKeyPwd;
    /**
     * 使用keytool生成密钥对时设置的密钥库的访问密码
     */
    private String keyStorePwd;
    private String subject;
    private String priPath;

    private String mac;
    /**
     * X500Principal是一个证书文件的固有格式
     */
    static String DName = "CN=RIC, OU=Client, O=CBC, L=SH, ST=SH, C=zh";
    //    static String DName = "CN=wq,OU=iss,O=iss,L=bj,ST=bj,C=china";
    private final static X500Principal DEFAULTHOLDERANDISSUER =
            new X500Principal(DName);

    public LicenseMake(){}
    public LicenseMake(String confPath){
        initParam(confPath);
    }
    /**
     * 读取属性文件
     * @param confPath
     */
    public void initParam(String confPath) {
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream(confPath);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        priAlias = prop.getProperty("private.key.alias");
        privateKeyPwd = prop.getProperty("private.key.pwd");
        keyStorePwd = prop.getProperty("key.store.pwd");
        subject = prop.getProperty("subject");
        priPath = prop.getProperty("priPath");
        licPath = prop.getProperty("licPath");
        //license content
        issued = prop.getProperty("issuedTime");
        notBefore = prop.getProperty("notBefore");
        notAfter = prop.getProperty("notAfter");
        consumerType = prop.getProperty("consumerType");
        consumerAmount = Integer.valueOf(prop.getProperty("consumerAmount"));
        info = prop.getProperty("info");
        mac = prop.getProperty("mac");

    }
    private LicenseParam initLicenseParam(){
        Class<LicenseMake> clazz = LicenseMake.class;
        Preferences pre = Preferences.userNodeForPackage(clazz);
        //设置对证书内容加密的对称密码
        CipherParam cipherParam = new DefaultCipherParam(keyStorePwd);
        /**
         * clazz 从哪个类Class.getResource()获得密钥库
         * priPath 从哪个类Class.getResource()获得密钥库
         * priAlias 密钥库的别名
         * keystorePwd 密钥库存储密码
         * privateKeyPwd 密钥库密码
         */
        KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
                clazz, priPath, priAlias, keyStorePwd, privateKeyPwd);
        //返回生成证书时需要的参数
        LicenseParam licenseParam = new DefaultLicenseParam(
                subject, pre, privateStoreParam, cipherParam);
        return licenseParam;
    }

    public LicenseContent buildLicenseContent(LicenseCheckModel model) throws ParseException {
        LicenseContent content = new LicenseContent();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        content.setSubject(subject);
        content.setConsumerAmount(consumerAmount);
        content.setConsumerType(consumerType);
        content.setHolder(DEFAULTHOLDERANDISSUER);
        content.setIssuer(DEFAULTHOLDERANDISSUER);
        content.setIssued(formate.parse(issued));
        //todo 对各个模块时间验证之后，是否需要对整个时间验证
        content.setNotBefore(formate.parse(notBefore));
        content.setNotAfter(formate.parse(notAfter));
        content.setInfo(info);

        content.setExtra(model);
        return content;
    }

    public void create(LicenseCheckModel model) {
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getLicenseManager(initLicenseParam());
            LicenseContent content = buildLicenseContent(model);
            licenseManager.store(content, new File(licPath));
            System.out.println("证书发布成功");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        LicenseMake clicense = new LicenseMake("/licenseMakeConf.properties");
        LicenseCheckModel modelAndTimes = getCheckModel();
        clicense.create(modelAndTimes);
    }

    public static LicenseCheckModel getCheckModel() {
        LicenseCheckModel model = new LicenseCheckModel();
        String mac = "4C-BB-58-5B-6C-C8";
        model.setMacAddress(mac);
        Map<String, ModuleProperties<String, String>> modelProperties
                = new HashMap<String, ModuleProperties<String, String>>();
        modelProperties.put("app", new ModuleProperties<String,String>("2019-01-01", "2019-01-17"));
        modelProperties.put("web", new ModuleProperties<String,String>("2019-01-01", "2019-01-18"));
        modelProperties.put("mini", new ModuleProperties<String,String>("2019-01-01", "2019-01-20"));

        model.setModels(modelProperties);
        return model;
    }
}
