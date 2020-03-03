package com.ble.blebzl.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by Admin
 * Date 2019/12/11
 */
public class Test8 {



    public static void main(String[] arg){

        String str = "汉";
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("--------周="+ Arrays.toString(bytes)+"\n"+bytes.length+"\n");

        String str2 = new String(bytes);

        try {
           String tmpStr2 =  URLEncoder.encode(str,"UTF-8");
            System.out.println("----change="+toHexString(bytes)+"--="+str2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray
     *            需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }


}
