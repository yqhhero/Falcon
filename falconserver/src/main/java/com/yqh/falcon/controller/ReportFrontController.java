package com.yqh.falcon.controller;

import com.yqh.falcon.model.Report;
import com.yqh.falcon.model.User;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.ReportService;
import com.yqh.falcon.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ReportFrontController {
    @Autowired
    private ReportService reportService;

    /**
     * 添加举报信息
     * @param report
     * @return
     */
    @PostMapping("/user/report")
    public ResponseDto addReport(@Validated @RequestBody Report report, HttpServletRequest request){
        long userId = GeneralUtils.getUserId(request);
        report.setUser(User.builder().id(userId).build());
        return reportService.addReport(report) == 1 ? ResponseDto.success(ResultCode.CREATED) : ResponseDto.fail(ResultCode.FAIL);
    }
}
