package yq.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Armory {

    private static final Logger log = LoggerFactory.getLogger(Armory.class);

    /**
     * 将多个map合成一个
     * @param params
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> Map<String, T> mapOf(Map<String, T> ... params){
        Map<String, T> map = new TreeMap<>();
        for (Map<String, T> m : params) {
            map.putAll(m);
        }
        return map;
    }

    /**
     * 将key-value数据转成Map类型
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> kv(String key, T value) {
        Map<String, T> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    /**
     * 格式化时间
     * @param d Date or long
     * @param format like : "yyyy-MM-dd HH:mm:ss"
     * @param locale 时区
     * @return
     */
    public static String getDateFormat(Object d , String format, Locale locale){
        return new SimpleDateFormat(format, locale).format(d);
    }

    /**
     * 格式化时间
     * @param d Date or long
     * @param format
     * @return
     */
    public static String getDateFormat(Object d , String format){
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 默认使用中国传统格式
     * @param d
     * @return
     */
    public static String getDateFormat(Object d){
        return getDateFormat(d , "yy/MM/dd HH:mm:ss:SSS" ,Locale.CHINA);
    }

    /**
     * 提供默认格式
     * @param datatime
     * @return
     */
    public static long getTime(String datatime) {
        return getTime(datatime, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 将格式化的时间 转换成time (long类型)
     * @param format 如: yyyy-MM-dd HH:mm:ss
     * @param datetime
     * @return
     */
    public static long getTime(String datetime , String format) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        long msTime = -1;
        try {
            msTime=simpleDateFormat.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return msTime;
    }
    /**
     * 获取当前时间的毫秒数
     * @return
     */
    public static String now(){
        return String.valueOf(new Date().getTime());
    }

    /**
     * 捕获异常后的 sleep
     * @param ms
     */
    public static void sleep(long ms) {
        try {
            log.info("线程等待 >> 时长 {} ms" ,ms);
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取一个随机整数
     * @param max
     * @param min
     * @return
     */
    public static int getRandomInt(int max, int min) {
        if(max == 0){
            return 0;
        }
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public static List<String> getMatchedString(String s ,String regexText){
        // 编译正则
        Pattern pattern = Pattern.compile(regexText);
        // 创建matcher对象
        Matcher m = pattern.matcher(s);

        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }


    /**
     * 适应当前的16进制转字符串
     * 单个格式 : [0xe9][0x97][0xb4]
     * @param str16
     * @return
     */
    public static String myHex2string(String str16) {
        String s = str16.replaceAll("\\[0x", "");
        String s1 = s.replaceAll("]", "");
        return hex2string(s1);
    }

    /**
     * 16进制转字符串
     * 默认6个字符一节
     * @param s
     * @return
     */
    public static String hex2string(String s)  {
        s = s.toUpperCase();
        int cha = s.length() % 6;
        if (cha != 0) {
            StringBuilder sBuilder = new StringBuilder(s);
            for (int i = 0; i < 6 - cha; i++) {
                sBuilder.append("0");
            }
            s = sBuilder.toString();
        }
        String[] split = getMatchedString(s, ".{6}").toArray(new String[0]);
        StringBuilder res = new StringBuilder();
        for (String sp : split) {
            int i = Integer.parseInt(sp ,16);
            byte[] bytes = int2bytes(i);
            String ss = null;
            try {
                ss = new String(bytes ,"UTf-8");
                res.append(ss);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return res.toString();
    }

    /**
     * int 转 byte[]
     * 高位在前，低位在后
     * @param num
     * @return
     */
    public static byte[] int2bytes(int num){
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);//说明一
        result[1] = (byte)((num >>> 16)& 0xff );
        result[2] = (byte)((num >>> 8) & 0xff );
        result[3] = (byte)((num >>> 0) & 0xff );
        return result;
    }

    /**
     * 获取给的集合对象的类型集合
     * @param objects
     * @return
     */
    public static Class[] getTypesArray(Object... objects) {
        if (objects == null)    return null;
        int len = objects.length;
        if (len == 0)   return null;

        Class[] clzs = new Class[len];
        for (int i = 0; i < len; i++) {
            clzs[i] = objects[i].getClass();
        }
        return clzs;
    }
}
