package com.wldk.tick.tuils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * HTTP工具
 * @author robinzhang
 *
 */
public class HttpUtil {
    /**
     * 请求类型： GET
     */
    public final static String GET = "GET";
    /**
     * 请求类型： POST
     */
    public final static String POST = "POST";

    /**
     * 模拟Http Get请求
     * @param urlStr 请求路径
     * @param paramMap 请求参数
     * @return
     * @throws Exception
     */
    public static String get(String urlStr, String paramMap) throws Exception{
        urlStr = urlStr + "?key=" + paramMap;
        HttpURLConnection conn = null;
        try{
            //创建URL对象
            URL url = new URL(urlStr);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();

            //设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            //建立实际的连接
            conn.connect();
            //获取响应的内容
            return readResponseContent(conn.getInputStream());
        }finally{
            if(null!=conn) conn.disconnect();
        }
    }
    public static String get(String urlStr) throws Exception{

        HttpURLConnection conn = null;
        try{
            //创建URL对象
            URL url = new URL(urlStr);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();

            //设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            //建立实际的连接
            conn.connect();
            //获取响应的内容
            return readResponseContent(conn.getInputStream());
        }finally{
            if(null!=conn) conn.disconnect();
        }
    }
    /**
     * 模拟Http Post请求
     * @param urlStr 请求路径
     * @param paramMap 请求参数
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, Map<String, String> paramMap) throws Exception{
        HttpURLConnection conn = null;
        PrintWriter writer = null;
        try{
            //创建URL对象
            URL url = new URL(urlStr);
            //获取请求参数
            String param = getParamString(paramMap);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            //设置通用请求属性
            setHttpUrlConnection(conn, POST);
            //建立实际的连接
            conn.connect();
            //将请求参数写入请求字符流中
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            //读取响应的内容
            return readResponseContent(conn.getInputStream());
        }finally{
            if(null!=conn) conn.disconnect();
            if(null!=writer) writer.close();
        }
    }

    /**
     * 读取响应字节流并将之转为字符串
     * @param in 要读取的字节流
     * @return
     * @throws IOException
     */
    private static String readResponseContent(InputStream in) throws IOException{
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            reader = new InputStreamReader(in);
            char[] buffer = new char[1024];
            int head = 0;
            while( (head=reader.read(buffer))>0 ){
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        }finally{
            if(null!=in) in.close();
            if(null!=reader) reader.close();
        }
    }

    /**
     * 设置Http连接属性 
     * @param conn http连接
     * @return
     * @throws ProtocolException
     * @throws Exception
     */
    private static void setHttpUrlConnection(HttpURLConnection conn, String requestMethod) throws ProtocolException{
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
        conn.setRequestProperty("Cookie", "acw_tc=2f6a1f9e15886411949654121e3538bfde1c7089c3f014ae2256d0bb062917; _uab_collina=158864119633229639305169; MEIQIA_TRACK_ID=1bT1JelpNkiyt3cIlfiojpatieF; UM_distinctid=171e264f7737f8-0346f373be7263-30677c00-384000-171e264f7747e3; XpShop_CartID=0fe781ad-0212-430e-9bca-7099a2dd49ce; aliyungf_tc=AQAAAJvyUkv/lAsAyjHxcjtIDm+oL1K3; MEIQIA_VISIT_ID=1bX7YDw5uP1KVFcupFt9m56G6rW; ASP.NET_SessionId=3i2owzse0kyjrzvhh0wudqrz; CNZZDATA1278193973=1504480042-1588638655-https%253A%252F%252Fgzgsv.xpshop.cn%252F%7C1588763586; acw_sc__v2=5eb2a7b3e5d79f25c4ae34a06a3cfea8ee4cf905; u_asec=099%23KAFEeGEKE3SEhGTLEEEEEpEQz0yFD60cDrwoS6fhDcB7W6fTZXaED6fESP7TEEiStEE7jYFET%2FidlllP%2FqMTEhqE8OId%2FqYWcR4f9BZaCycVcA97ae93PaYBb5Dtz62nqOeAaQss%2Ba8nqOepalvIhTYMhBVuuzIWPLMSadu4DAZBNs67%2BW9jHaGQroC6zIeA6VXA1buADOoV%2FRvtlunmHa7WPbDoE7EIt37EG7j7tjwrE7Eht%2FMF16k6sEFEp3iSlllP%2F3iSt374luZdtvJSt1ilsyaA59iSK3lP%2F3Hjt374luZddmJUE7TxEwwhD5G%2BxYP0DLrI3kikwmXRXyTn0Kaok7U%2Bukj2wmf3kmwNB1W2ZDeyIlo%2Bp7P0DYAkWhf2kfM6mDyV0HU6mStIyUQGBEroiDN7QmGlw7V6a3QTEEx5Lq5jBYFETEEEbOI%3D; SERVERID=ff6b7f752c6b204a1bdc17a466735c8e|1588767517|1588766627");
        if(null!=requestMethod && POST.equals(requestMethod)){
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }

    /**
     * 将参数转为路径字符串
     * @param paramMap
     *             参数
     * @return
     */
    private static String getParamString(Map<String, String> paramMap){
        if(null==paramMap || paramMap.isEmpty()){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(String key : paramMap.keySet() ){
            builder.append("&")
                    .append(key).append("=").append(paramMap.get(key));
        }
        return builder.deleteCharAt(0).toString();
    }

    public static void main(String[] args){
        try {
            System.out.println( get("http://127.0.0.1/crazy_java.pdf", null) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}