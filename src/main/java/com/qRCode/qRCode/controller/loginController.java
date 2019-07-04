package com.qRCode.qRCode.controller;

import cn.hutool.Hutool;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



/**
 * 小程序登录
 * @author axf
 * @version 1.0
 * @date 2019/7/4 9:47
 */
public class loginController {

    @Value("appid")
    private String APPID;

    @Value("secret")
    private String SECRET;

    /**
     * 获取凭证校检接口
     * @param code 登录时通过js获取的 code
     * @return
     */
    public String login(String code)
    {
        //微信的接口
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+
                "&secret="+SECRET+"&js_code="+ code +"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //根据返回值进行后续操作
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            //解析从微信服务器获得的json数据
            JSONObject jsonObject = JSONUtil.parseObj(sessionData);
            String openid = jsonObject.getStr("openid");
            String session_key = jsonObject.getStr("session_key");
            String unionid = jsonObject.getStr("unionid");
            String errcode = jsonObject.getStr("errcode");
            String errmsg = jsonObject.getStr("errmsg");

            //处理登录逻辑

            //最后要返回一个自定义的登录态,用来做后续数据传输的验证
        }

        return null;

    }

}
