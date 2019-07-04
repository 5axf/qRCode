package com.qRCode.qRCode.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author axf
 * @version 1.0
 * @date 2019/7/2 15:03
 */
public class appletQRCode {

    /**
     * 获取小程序码--小程序二维码
     *
     * @param path 保存二维码路径
     * @param sceneStr 自定义内容
     * @param AppId 小程序appid
     * @param secret 小程序secret
     */
    public void getUnlimited(String path,String sceneStr,String AppId,String secret){
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //post请求
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+getAccessToken(AppId,secret);
            Map<String,Object> param = new HashMap<>();
            param.put("scene", sceneStr);
//            param.put("page", "pages/index/index");
//            param.put("width", 430);
//            param.put("auto_color", false);
//            Map<String,Object> line_color = new HashMap<>();
//            line_color.put("r", 0);
//            line_color.put("g", 0);
//            line_color.put("b", 0);
//            param.put("line_color", line_color);
//            param.put("is_hyaline",false);
            //LOG.info("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            //LOG.info("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
            byte[] result = entity.getBody();
            //LOG.info(Base64.encodeBase64String(result));
            inputStream = new ByteArrayInputStream(result);
            //保存二维码路径
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            //LOG.error("调用小程序生成微信永久小程序码URL接口异常",e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取AccessToken
     * @param AppId
     * @param secret
     * @return
     */
    public String getAccessToken(String AppId,String secret){
        String access_token = "";
        //获取access_token填写client_credential
        String grant_type = "client_credential";
        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            // 必须是get方式请求
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes);
            JSONObject demoJson = JSONObject.parseObject(message);
            access_token = demoJson.getString("access_token");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }
}
