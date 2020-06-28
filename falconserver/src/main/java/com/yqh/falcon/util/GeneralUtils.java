package com.yqh.falcon.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yqh.falcon.exception.UnAuthorizedException;

import javax.servlet.http.HttpServletRequest;

public class GeneralUtils {

    public static Long getUserId(HttpServletRequest request){
        try {
            DecodedJWT jwt = JWTUtils.verifierToken(request.getHeader("Authorization"));
            Claim userIdClaim = jwt.getClaim("data");
            return Long.valueOf(userIdClaim.asString());
        } catch (Exception e) {
            throw new UnAuthorizedException();
        }
    }
}
