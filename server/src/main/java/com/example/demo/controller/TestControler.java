package com.example.demo.controller;

import com.example.demo.license.LicenseMake;
import com.wbtech.licensebase.ModuleProperties;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

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
        LicenseMake clicense = new LicenseMake("/licenseMakeConf.properties");
        Map<String, ModuleProperties<String, String>> modelProperties = LicenseMake.getModelProperties();
        clicense.create(modelProperties);
        return "hello";

    }
}
