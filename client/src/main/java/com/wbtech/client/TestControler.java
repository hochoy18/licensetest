package com.wbtech.client;

import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api")
public class TestControler {

    private String licPath = System.getProperty("user.dir") + File.separator + "license.lic";
    @RequestMapping(value="checkLicense",method = RequestMethod.GET)
    @ResponseBody
    public String checkLicense(
            @RequestParam(name = "modelName",required = true,defaultValue = "web") String modelName ) {
        modelName = "app";
        System.out.println("modelName.............."+modelName);

//        try {
//            downloadNet();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        LicenseVertify vlicense=new LicenseVertify("happy"); // 项目唯一识别码，对应生成配置文件的subject
        vlicense.install(licPath,modelName);  //D:\eclipse_mars_workspace\LicenseTest
        boolean success = vlicense.vertify("app");
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

            URL url = new URL("http://localhost:8080/license.lic");

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
