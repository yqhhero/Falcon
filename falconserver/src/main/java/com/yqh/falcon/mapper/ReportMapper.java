package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Report;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMapper {

    int insertReport(Report report);
    int updateHandleTime(Report report);
    List<Report> findReportByHandleTimeIsNull();
}
