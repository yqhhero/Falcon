package com.yqh.falcon.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yqh.falcon.model.pojo.RSA256Key;
import com.yqh.falcon.model.User;

import java.util.Date;
import java.util.UUID;

public class JWTUtils {
    private static final String ISSUER = "FALCON_API";

    /*获取签发的token，返回给前端*/
    public static String generTokenByRS256(User user) throws Exception{

        RSA256Key rsa256Key = SecretKeyUtils.getRSA256Key(); // 获取公钥/私钥
        Algorithm algorithm = Algorithm.RSA256(
                rsa256Key.getPublicKey(),rsa256Key.getPrivateKey());

        return createToken(algorithm, user.getId());

    }

    /*签发token*/
    public static String createToken(Algorithm algorithm,Object data) throws Exception {

        String[] audience  = {"web"};
        return JWT.create()
                .withIssuer(ISSUER)   		//发布者
                .withAudience(audience)     //观众，相当于接受者
                .withIssuedAt(new Date())   // 生成签名的时间
                .withExpiresAt(DateUtils.addHour(new Date(),2))    // 生成签名的有效期
                .withClaim("data", JSON.toJSONString(data)) //存数据
                .withNotBefore(new Date())  //生效时间
                .withJWTId(UUID.randomUUID().toString())    //编号
                .sign(algorithm);							//签入
    }

    /*验证token*/
    public static DecodedJWT verifierToken(String token)throws Exception{

        RSA256Key rsa256Key = SecretKeyUtils.getRSA256Key(); // 获取公钥/私钥

        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);


        return jwt;

    }
}
