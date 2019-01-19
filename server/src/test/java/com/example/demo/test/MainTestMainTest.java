package com.example.demo.test;

import com.example.demo.DemoApplicationTests;

import java.util.prefs.Preferences;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/16
 */
public class MainTestMainTest {
    public static void main(String[] args) {
        Preferences preferences =Preferences.userRoot();
        Preferences preferences1 =Preferences.systemRoot();
        System.out.println(preferences );
        System.out.println(preferences.node("path"));
        System.out.println(preferences1 );
        System.out.println(preferences1.node("path"));
        Preferences node = Preferences.userNodeForPackage(MainTestMainTest.class);
        System.out.println(node);

        Preferences node1 = Preferences.systemNodeForPackage(DemoApplicationTests.class);
        System.out.println(node1);





//        String path =  System.getProperty("user.dir") + "\\server\\src\\test\\java\\com\\example\\demo\\test\\param.properties";
        //String path = "/param.properties";
/*        String path = "E:\\work\\wbkit\\project\\Cobub-Java\\licensetest\\server\\src\\test\\resources\\param.properties";
        CreateLicense license = new CreateLicense(path);
        license.setParam(path);
        license.create();*/
    }
}
