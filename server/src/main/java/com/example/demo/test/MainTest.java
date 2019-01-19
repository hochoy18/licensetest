package com.example.demo.test;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/16
 */
public class MainTest {
    public static void main(String[] args) {

//        String path =  System.getProperty("user.dir") + "\\server\\src\\test\\java\\com\\example\\demo\\test\\param.properties";
        String path = "/licenseMakeConf.properties";
//        String path = "E:\\work\\wbkit\\project\\Cobub-Java\\licensetest\\server\\src\\test\\resources\\param.properties";
        CreateLicense license = new CreateLicense(path);
        license.create();
    }
}
