package com.wbtech.licensebase;

/**
 * @author wbmac
 * @Title: LicenseCheckModel
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午5:52
 */
public class LicenseCheckModel {
    private String ipAddress;
    private String macAddress;
    private String CPUSerial;
    private String motherboardSN;
    private String hardDiskSN;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getCPUSerial() {
        return CPUSerial;
    }

    public void setCPUSerial(String CPUSerial) {
        this.CPUSerial = CPUSerial;
    }

    public String getMotherboardSN() {
        return motherboardSN;
    }

    public void setMotherboardSN(String motherboardSN) {
        this.motherboardSN = motherboardSN;
    }

    public String getHardDiskSN() {
        return hardDiskSN;
    }

    public void setHardDiskSN(String hardDiskSN) {
        this.hardDiskSN = hardDiskSN;
    }
}
