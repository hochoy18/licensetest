package com.wbtech.client;

import com.wbtech.licensebase.MLicenseManager;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author wbmac
 * @Title: LicenseManagerHolder
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午2:32
 */
public class LicenseManagerHolder {
    private static LicenseManager licenseManager;
    private LicenseManagerHolder(){}
    public static synchronized LicenseManager getLicenseManager(LicenseParam param){
        if(licenseManager == null){
            licenseManager = new MLicenseManager(param);
        }
        return licenseManager;
    }

}
