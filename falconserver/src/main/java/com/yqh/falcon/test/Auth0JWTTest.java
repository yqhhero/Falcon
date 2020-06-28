package com.yqh.falcon.test;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yqh.falcon.model.User;
import com.yqh.falcon.util.JWTUtils;

import java.util.Date;

public class Auth0JWTTest {

    public static void main(String[] args) throws Exception {

        User userInfo = User.builder().id(160520401023L).type(1).build();
        String token = JWTUtils.generTokenByRS256(userInfo);
        System.out.println(token);
        DecodedJWT jwt = JWTUtils.verifierToken(token);
        String algorithm = jwt.getAlgorithm(); //获取算法类型
        String type = jwt.getType();	//获取token类型
        String issuer = jwt.getIssuer();	//获取token发布者
        Date expiresAt = jwt.getExpiresAt(); //获取token过期时间
        Date issuedAt = jwt.getIssuedAt();	// 获取token生产日期
        Claim claim = jwt.getClaim("data");
        User user = JSON.parseObject(claim.asString(),User.class);
        System.out.println(user);



        System.out.println(algorithm); 	//=> 	HS256
        System.out.println(type);		//=>	JWT
        System.out.println(issuer);		//=> 	auth0
        System.out.println(expiresAt);	//=>	Sat Jan 11 22:25:13 CST 2020
        System.out.println(issuedAt);	//=>	Sat Jan 11 20:25:13 CST 2020

    }
}
