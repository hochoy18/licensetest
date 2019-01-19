package com.wbtech.licensebase;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;

import java.util.prefs.Preferences;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/18
 */
public class MDefaultLicenseParam extends DefaultLicenseParam {
    String modelName;
    public MDefaultLicenseParam(String s, Preferences preferences, KeyStoreParam keyStoreParam, CipherParam cipherParam, String modelName) {
        super(s, preferences, keyStoreParam, cipherParam);
        this.modelName = modelName;
    }
}
