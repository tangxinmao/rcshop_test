package com.socool.soft.service;

import com.socool.soft.vo.newvo.ReportVO;

public interface IReportService {
	
	ReportVO findOverview(Integer merchantId, Integer vendorId);
	
	ReportVO findReport(int reportType, Integer merchantId, Integer vendorId);
}
