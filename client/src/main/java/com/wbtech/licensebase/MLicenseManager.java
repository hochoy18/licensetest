package com.wbtech.licensebase;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;

import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wbmac
 * @Title: MLicenseManager
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午5:50
 */
public class MLicenseManager extends LicenseManager{

    String cModelName;
    public MLicenseManager(LicenseParam var1){
        super(var1);
    }

    public MLicenseManager(MDefaultLicenseParam param) {
        super(param);
    }

    public MLicenseManager(LicenseParam licenseParam, String cModelName) {
        super(licenseParam);
        this.cModelName = cModelName;
    }

    @Override
    protected synchronized void validate(LicenseContent licenseContent) throws LicenseContentException {
        super.validate(licenseContent);
        if (!(licenseContent.getExtra() instanceof LicenseCheckModel)) {
            throw new LicenseContentException("extra is not validate");
        }
        //验证mac地址
        LicenseCheckModel checkModel = (LicenseCheckModel) licenseContent.getExtra();
        try {
            if(!HardWareCheck.validateMacAddress(checkModel.getMacAddress())) {
                throw new LicenseContentException("server mac is not validate");
            }
            Map<String, ModuleProperties<String,String>> modelAndTimes = checkModel.getModels();
            if (modelAndTimes.size() <= 0) {
                throw new LicenseContentException("model and time are not validate");
            }
            //TODO 模块属性验证，目前只验证结束时间，后续如果需要可添加其他验证
            if (!modelPropertiesValidate(modelAndTimes,cModelName)){
                throw new LicenseContentException("model and time are not validate");
            }

        } catch (SocketException e) {
            throw new LicenseContentException("can not find server mac");
        }
    }

    @Override
    protected synchronized byte[] create(LicenseContent paramLicenseContent,
                                         LicenseNotary paramLicenseNotary) throws Exception {
        initialize(paramLicenseContent);
//        super.validate(paramLicenseContent);
        GenericCertificate localGenericCertificate = paramLicenseNotary
                .sign(paramLicenseContent);
        byte[] arrayOfByte = getPrivacyGuard()
                .cert2key(localGenericCertificate);
        return arrayOfByte;
    }

    @Override
    protected synchronized LicenseContent install(byte[] var1, LicenseNotary var2) throws Exception {
        GenericCertificate var3 = this.getPrivacyGuard().key2cert(var1);
        var2.verify(var3);
        LicenseContent var4 = (LicenseContent)var3.getContent();
//        this.validate(var4);
        this.setLicenseKey(var1);
        this.setCertificate(var3);
        return var4;
    }

    @Override
    protected synchronized LicenseContent verify(LicenseNotary var1) throws Exception {
        GenericCertificate var2 = this.getCertificate();
        byte[] var3 = this.getLicenseKey();
        if (null == var3) {
            throw new NoLicenseInstalledException(this.getLicenseParam().getSubject());
        } else {
            var2 = this.getPrivacyGuard().key2cert(var3);
            var1.verify(var2);
            LicenseContent var4 = (LicenseContent) var2.getContent();

            this.validate(var4);
            this.setCertificate(var2);
            return var4;
        }
    }
    boolean modelPropertiesValidate(Map<String, ModuleProperties<String,String>> modelAndTimes, String cModelName) {
        boolean flag = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (modelAndTimes.containsKey(cModelName)){
            try {
                Date client_end_date = format.parse(format.format(new Date()));
                Date model_end_date = format.parse(modelAndTimes.get(cModelName).endTime);
                if (model_end_date !=null && client_end_date != null && client_end_date.before(model_end_date)) {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}
