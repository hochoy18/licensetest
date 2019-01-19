//package com.wbtech.licensebase;
//
//import de.schlichtherle.license.*;
//import de.schlichtherle.xml.GenericCertificate;
//
//import java.net.SocketException;
//
///**
// * @author wbmac
// * @Title: MLicenseManager
// * @ProjectName licensetest
// * @Description: TODO
// * @date 2019/1/2 下午5:50
// */
//public class MLicenseManager extends LicenseManager{
//
//    public MLicenseManager(LicenseParam var1){
//        super(var1);
//    }
//    @Override
//    protected synchronized void validate(LicenseContent licenseContent) throws LicenseContentException {
//        super.validate(licenseContent);
//        if(!(licenseContent.getExtra() instanceof LicenseCheckModel)) {
//            throw new LicenseContentException("extra is not validate");
//        }
//
//        //验证mac地址
//        LicenseCheckModel checkModel = (LicenseCheckModel) licenseContent.getExtra();
//        try {
//            if(!HardWareCheck.validateMacAddress(checkModel.getMacAddress())) {
//                throw new LicenseContentException("server mac is not validate");
//            }
//        } catch (SocketException e) {
//            throw new LicenseContentException("can not find server mac");
//        }
//    }
//
//    @Override
//    protected synchronized byte[] create(LicenseContent paramLicenseContent,
//                                         LicenseNotary paramLicenseNotary) throws Exception {
//        initialize(paramLicenseContent);
////        super.validate(paramLicenseContent);
//        GenericCertificate localGenericCertificate = paramLicenseNotary
//                .sign(paramLicenseContent);
//        byte[] arrayOfByte = getPrivacyGuard()
//                .cert2key(localGenericCertificate);
//        return arrayOfByte;
//    }
//
//    @Override
//    protected synchronized LicenseContent install(byte[] var1, LicenseNotary var2) throws Exception {
//        GenericCertificate var3 = this.getPrivacyGuard().key2cert(var1);
//        var2.verify(var3);
//        LicenseContent var4 = (LicenseContent)var3.getContent();
////        this.validate(var4);
//        this.setLicenseKey(var1);
//        this.setCertificate(var3);
//        return var4;
//    }
//
//    @Override
//    protected synchronized LicenseContent verify(LicenseNotary var1) throws Exception {
//        GenericCertificate var2 = this.getCertificate();
//            byte[] var3 = this.getLicenseKey();
//            if(null == var3) {
//                throw new NoLicenseInstalledException(this.getLicenseParam().getSubject());
//            } else {
//                var2 = this.getPrivacyGuard().key2cert(var3);
//                var1.verify(var2);
//                LicenseContent var4 = (LicenseContent)var2.getContent();
//                this.validate(var4);
//                this.setCertificate(var2);
//                return var4;
//            }
//        }
//}
