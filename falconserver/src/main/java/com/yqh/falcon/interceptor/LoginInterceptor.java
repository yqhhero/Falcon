package com.yqh.falcon.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yqh.falcon.exception.UnAuthorizedException;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.util.JWTUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String token = request.getHeader("Authorization");
       if (token == null){
           System.out.println("空口令");
           response.setCharacterEncoding("UTF-8");
           response.setContentType("application/json; charset=utf-8");

           PrintWriter out = response.getWriter();
           out.append(JSON.toJSONString(ResponseDto.fail(ResultCode.UN_AUTHORIZED)));
           out.flush();
           return false;
       }
       try {
           DecodedJWT jwt = JWTUtils.verifierToken(token);
           Claim claim = jwt.getClaim("data");
           System.out.println("用户ID"+claim.asString());
           return true;
       }catch (Exception e){
           System.out.println("口令验证失败");
           throw new UnAuthorizedException();
       }

    }
}
