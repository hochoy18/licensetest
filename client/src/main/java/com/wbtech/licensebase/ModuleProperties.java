package com.wbtech.licensebase;

import java.io.Serializable;

/**
 * Describe: Subsequently, if you have attributes that require authentication,
 * you can add them to this class.
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/17
 */
public class ModuleProperties<T1, T2> implements Serializable {

    public  T1 startTime;
    public  T2 endTime;

    public ModuleProperties() {
    }

    public ModuleProperties(T1 startTime, T2 endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
