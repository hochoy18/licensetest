package com.example.demo.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/17
 */
public class RSAEncrypt {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String ALGORITHM_METHOD = "RSA";
    private static final String priKeyFileName = "razorPrivate.keystore";
    private static final String pubKeyFileName = "razorPublic.keystore";

    /**
     * 生成公私秘钥对并保存到文件中
     *
     * @param filePath
     */
//    public static void genKeyPair(String filePath) {
//        //KeyPairGenerator 用于生成公私密钥对，基于RSA算法成像
//        KeyPairGenerator keyPairGen = null;
//        try {
//            keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_METHOD);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        // 初始化密钥对生成器，密钥大小为96-1024位
//        keyPairGen.initialize(1024, new SecureRandom());
//        //使用秘钥对生成器 生成一个秘钥对，保存在KeyPair中
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        //得到私钥
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        //得到公钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        FileWriter pubfw = null;
//        FileWriter prifw = null;
//        BufferedWriter pubbw = null;
//        BufferedWriter pribw = null;
//        try {
//            Base64 base64 = new Base64();
//            String pubKeyStr = new String(base64.encode(publicKey.getEncoded()));
//            String priKeyStr = new String(base64.encode(privateKey.getEncoded()));
//            pubfw = new FileWriter(filePath + File.pathSeparator + pubKeyFileName);
//            prifw = new FileWriter(filePath + File.pathSeparator + priKeyFileName);
//            pubbw = new BufferedWriter(pubfw);
//            pribw = new BufferedWriter(prifw);
//            pubbw.write(pubKeyStr);
//            pribw.write(priKeyStr);
//            pubbw.flush();
//            pribw.flush();
//        } catch (IOException e) {
//
//        } finally {
//            try {
//                pubfw.close();
//                prifw.close();
//                pubbw.close();
//                pribw.close();
//            } catch (IOException e) {
//
//            }
//        }
//    }

//    /**
//     * 从文件中输入流中加载公钥/私钥
//     *
//     * @param path
//     * @param file
//     * @return
//     */
//    public static String loadKeyByFile(String path, String file) {
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//        try {
//            br = new BufferedReader(new FileReader(path + File.pathSeparator + file));
//            String readLine = null;
//
//            while ((readLine = br.readLine()) != null) {
//                sb.append(readLine);
//            }
//
//        } catch (IOException e) {
//
//        } finally {
//            try {
//                br.close();
//            } catch (IOException e) {
//            }
//        }
//        return sb.toString();
//    }


    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
            throws Exception {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


//    public void encryptionByRSA(String fileName, String saveFileName, String keyFileName) {
//        long start = System.currentTimeMillis();
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            SecureRandom random = new SecureRandom();
//            keyGen.init(random);
//            SecretKey key = keyGen.generateKey();
//            String
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//    }


}
