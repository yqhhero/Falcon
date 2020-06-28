package com.yqh.falcon.service;

import com.yqh.falcon.model.Report;

import java.util.List;

public interface ReportService {
    
    List<Report> getReportHandleTimeIsNull();
    int addReport(Report report);
    int updateHandleTime(Report report);
}
