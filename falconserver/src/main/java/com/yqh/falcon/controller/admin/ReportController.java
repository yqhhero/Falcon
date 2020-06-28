package com.yqh.falcon.controller.admin;

import com.yqh.falcon.model.Report;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 获取未处理举报信息
     * @return
     */
    @GetMapping("/reports/unhandle")
    public ResponseDto getReportByHandleTimeIsNull( ){
        List<Report> reportList = reportService.getReportHandleTimeIsNull();
        return ResponseDto.success(ResultCode.OK,reportList);
    }

    /**
     * 更新处理时间
     * @param report
     * @return
     */
    @PutMapping("/report")
    public ResponseDto updateReportHandleTime(@RequestBody Report report){
        return reportService.updateHandleTime(report) == 1 ? ResponseDto.success(ResultCode.OK) : ResponseDto.fail(ResultCode.FAIL);
    }
}
