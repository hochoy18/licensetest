package com.wbtech.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wbmac
 * @Title: TestControler
 * @ProjectName licensetest
 * @Description: TODO
 * @date 2019/1/2 下午8:06
 */
@RestController
public class TestControler {

    private String licPath = System.getProperty("user.dir") + File.separator + "lic";
    @RequestMapping("checkLicense")
    public String checkLicense() {

        try {
            downloadNet();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        LicenseVertify vlicense=new LicenseVertify("happy"); // 项目唯一识别码，对应生成配置文件的subject
        vlicense.install(licPath);  //D:\eclipse_mars_workspace\LicenseTest
        boolean success = vlicense.vertify();
        if(success) {
            return "验证成功";
        } else {
            return "验证失败";
        }
    }

    public void downloadNet() throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL("http://localhost:9090/license.lic");

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(licPath + File.separator + "license.lic");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
