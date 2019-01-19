package com.example.demo.test;


import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.*;

import org.apache.commons.codec.binary.Base64;

/**
 * Describe:
 *
 * @author hochoy <hochoy18@sina.com>
 * @version V1.0.0
 * @date 2019/1/17
 */
public class Test {
    //RSA算法对文件加密
    private static final String priKeyFileName = "razorPrivate.keystore";
    private static final String pubKeyFileName = "razorPublic.keystore";
    private static final String ALGORITHM_METHOD = "RSA";

    //RSA算法对文件加密
    public void encryptByRSA(String fileName, String saveFileName, String keyFileName) throws Exception {
        long start = System.currentTimeMillis();
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keygen.init(random);
            SecretKey key = keygen.generateKey();
            String[] result = null;//readKeyUtil(new File(keyFileName));
            RSAPublicKey key2 = getPublicKey(result[0], result[1]);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, key2);
            byte[] wrappedKey = cipher.wrap(key);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFileName));
            out.writeInt(wrappedKey.length);
            out.write(wrappedKey);
            InputStream in = new FileInputStream(fileName);
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            crypt(in, out, cipher);
            in.close();
            out.close();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("总共耗时：" + (end - start));
    }

    //RSA算法对文件解密
//    public void decryptByRSA(String fileName, String saveFileName, String keyFileName) throws Exception {
//        try {
//            DataInputStream in = new DataInputStream(new FileInputStream(fileName));
//            int length = in.readInt();
//            byte[] wrappedKey = new byte[length];
//            in.read(wrappedKey, 0, length);
//            String[] result = readKeyUtil(new File(keyFileName));
//            RSAPrivateKey key2 = getPrivateKey(result[0], result[1]);
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.UNWRAP_MODE, key2);
//            Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
//
//            OutputStream out = new FileOutputStream(saveFileName);
//            cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, key);
//
//            crypt(in, out, cipher);
//            in.close();
//            out.close();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //对数据块加密
    public void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean next = true;
        while (next) {
            inLength = in.read(inBytes);
            System.out.println(inLength);
            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                next = false;
            }
        }
        if (inLength > 0) {
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        } else {
            outBytes = cipher.doFinal();
        }
        out.write(outBytes);
    }

    //生成RSA密钥对
    public void genKeyPair(String filePath) {
        //KeyPairGenerator 用于生成公私密钥对，基于RSA算法成像
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_METHOD);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        //使用秘钥对生成器 生成一个秘钥对，保存在KeyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        FileWriter pubfw = null;
        FileWriter prifw = null;
        BufferedWriter pubbw = null;
        BufferedWriter pribw = null;
        try {
            Base64 base64 = new Base64();
            String pubKeyStr = new String(base64.encode(publicKey.getEncoded()));
            String priKeyStr = new String(base64.encode(privateKey.getEncoded()));
            pubfw = new FileWriter(filePath + File.separator + pubKeyFileName);
            prifw = new FileWriter(filePath + File.separator + priKeyFileName);
            pubbw = new BufferedWriter(pubfw);
            pribw = new BufferedWriter(prifw);
            pubbw.write(pubKeyStr);
            pribw.write(priKeyStr);
            pubbw.flush();
            pribw.flush();
        } catch (IOException e) {

        } finally {
            try {
                pubfw.close();
                prifw.close();
                pubbw.close();
                pribw.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * 从保存的公钥/私钥文件中加载公钥/私钥
     *
     * @param path
     * @param fileName
     * @return
     */
    public String loadSecretKeyFromFile(String path, String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(path + File.separator + fileName));
            String readLine = null;

            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }

        } catch (IOException e) {

        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成xml字符串
     * @return
     * @throws Exception
     */
//    public static void createXmlFile(String Modulus,String Exponent,String filepath) throws Exception{
//        Document document = DocumentHelper.createDocument();
//        Element root = document.addElement( "root" );
//        Element supercarElement= root.addElement("Modulus");
//
//        supercarElement.addText(Modulus);
//        Element supercarElement2= root.addElement("Exponent");
//
//        supercarElement2.addText(Exponent);
//        // 写入到一个新的文件中
//        writer(document,filepath);
//    }
    /**
     * 把document对象写入新的文件
     *
     * @param document
     * @throws Exception
     */
//    public static void writer(Document document,String path) throws Exception {
//        // 紧凑的格式
//        // 排版缩进的格式
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        // 设置编码
//        format.setEncoding("UTF-8");
//        // 创建XMLWriter对象,指定了写出文件及编码格式
//        XMLWriter writer = new XMLWriter(new OutputStreamWriter(
//                new FileOutputStream(new File(path)), "UTF-8"), format);
//        // 写入
//        writer.write(document);
//        // 立即写入
//        writer.flush();
//        // 关闭操作
//        writer.close();
//    }

//    public String[] readKeyUtil(File file) throws DocumentException{
//        // 创建saxReader对象
//        SAXReader reader = new SAXReader();
//        // 通过read方法读取一个文件 转换成Document对象
//        Document document = reader.read(file);
//        //获取根节点元素对象
//        Element node = document.getRootElement();
//        Element ele = node.element("Modulus");
//        String Modulus = ele.getTextTrim();
//        Element ele1 = node.element("Exponent");
//        String Exponent = ele1.getTextTrim();
//        return new String[]{Modulus,Exponent};
//    }

    private static final int RSA_KEYSIZE = 1024;


    public RSAPublicKey loadPublicKeyByStr(String publicKeyStr){
        try {
            Base64 base64 = new Base64();
            byte [] buffer = base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_METHOD);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws Exception {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 公钥加密过程
     * @param publicKey
     * @param plainTextData
     * @return
     */
    public static byte[] encrypt(RSAPublicKey publicKey,byte[] plainTextData){
        if (publicKey == null){
            return null;
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte [] out = cipher.doFinal(plainTextData);
            return out;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 私钥加密过程
     * @param privateKey
     * @param plainTextData
     * @return
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData){
        if (privateKey == null){
            return null;
        }
        Cipher cipher = null;
        try {
            cipher= Cipher.getInstance(ALGORITHM_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            byte [] out = cipher.doFinal(plainTextData);
            return out;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥解密
     * @param publicKey
     * @param cipherData
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }
    public static void main(String[] args) throws Exception {
        //生成xml格式的公钥和私钥
        String path = "E:/work/wbkit/project/Cobub-Java/licensetest/server/src/main/java/com/example/demo/test";
        Base64 base64 = new Base64();
        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String plainText = "ihep_公钥加密私钥解密";
//        byte [] data = Test.decrypt()


//        new Test().genKeyPair(path);
//        //加载秘钥
//        String secretKey = new Test().loadSecretKeyFromFile(path, priKeyFileName);
//        System.out.println("priKeyFileName：  " + secretKey);
//        System.out.println("pubKeyFileName:   " + new Test().loadSecretKeyFromFile(path, pubKeyFileName));
//
//        System.out.println("=======================");
//        System.out.println("............."+new Test().loadPublicKeyByStr(new Test().loadSecretKeyFromFile(path,pubKeyFileName)));
//
//        System.out.println("------------------------"+Test.loadPrivateKeyByStr(new Test().loadSecretKeyFromFile(path,priKeyFileName)));
        //对文件进行加密
//        new Test().encryptByRSA("C://ZJY//12345.txt", "C://ZJY//123456.txt", "C:/ZJY/1/public.xml");
        //对文件进行解密
//        new Test().decryptByRSA("C://ZJY//123456.txt", "C://ZJY//1234567.txt", "C:/ZJY/1/private.xml");

    }
}