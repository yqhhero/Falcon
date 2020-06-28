package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.ReportMapper;
import com.yqh.falcon.model.Report;
import com.yqh.falcon.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportMapper reportMapper;

    /**
     * 查询未处理举报
     * @return
     */
    @Override
    public List<Report> getReportHandleTimeIsNull() {
        return reportMapper.findReportByHandleTimeIsNull();
    }

    /**
     * 添加举报信息
     * @param report
     * @return
     */
    @Override
    public int addReport(Report report) {
        report.setCreateTime(new Date());
        return reportMapper.insertReport(report);
    }

    /**
     * 更新处理举报时间
     * @param report
     * @return
     */
    @Override
    public int updateHandleTime(Report report) {
        return reportMapper.updateHandleTime(report);
    }
}
