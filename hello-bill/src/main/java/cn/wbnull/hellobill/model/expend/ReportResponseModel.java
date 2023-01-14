package cn.wbnull.hellobill.model.expend;

import cn.wbnull.hellobill.db.entity.ExpendInfo;
import cn.wbnull.hellobill.db.entity.IncomeInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 支出、收入信息报表接口响应参数
 *
 * @author dukunbiao(null)  2021-01-26
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class ReportResponseModel<T> {

    private List<T> reportClass;
    private List<ReportSecondClassBean<T>> reportSecondClass;
    private JSONArray reportDate;
    private JSONArray date;

    public static ReportResponseModel<ExpendInfo> buildExpend(List<ExpendInfo> expendReports,
                                                              List<ExpendInfo> expendReportsSecond) {
        ReportResponseModel<ExpendInfo> responseModel = new ReportResponseModel<>();
        responseModel.reportClass = expendReports;
        responseModel.reportSecondClass = new ArrayList<>();

        for (ExpendInfo expendInfo : expendReportsSecond) {
            ReportSecondClassBean<ExpendInfo> reportSecondClassTemp = null;
            for (ReportSecondClassBean<ExpendInfo> reportSecondClass : responseModel.reportSecondClass) {
                if (reportSecondClass.topClass.equals(expendInfo.getTopClass())) {
                    reportSecondClassTemp = reportSecondClass;
                    break;
                }
            }

            if (reportSecondClassTemp == null) {
                responseModel.reportSecondClass.add(ReportSecondClassBean.build(expendInfo));
            } else {
                reportSecondClassTemp.getReportClass().add(expendInfo);
            }
        }

        return responseModel;
    }

    public static ReportResponseModel<IncomeInfo> buildIncome(List<IncomeInfo> incomeReports,
                                                              List<IncomeInfo> incomeReportsSecond) {
        ReportResponseModel<IncomeInfo> responseModel = new ReportResponseModel<>();
        responseModel.reportClass = incomeReports;
        responseModel.reportSecondClass = new ArrayList<>();

        for (IncomeInfo expendInfo : incomeReportsSecond) {
            ReportSecondClassBean<IncomeInfo> reportSecondClassTemp = null;
            for (ReportSecondClassBean<IncomeInfo> reportSecondClass : responseModel.reportSecondClass) {
                if (reportSecondClass.topClass.equals(expendInfo.getTopClass())) {
                    reportSecondClassTemp = reportSecondClass;
                    break;
                }
            }

            if (reportSecondClassTemp == null) {
                responseModel.reportSecondClass.add(ReportSecondClassBean.build(expendInfo));
            } else {
                reportSecondClassTemp.getReportClass().add(expendInfo);
            }
        }

        return responseModel;
    }

    @Data
    static class ReportSecondClassBean<T> {

        private String topClass;
        private List<T> reportClass;

        public static ReportSecondClassBean<ExpendInfo> build(ExpendInfo expendInfo) {
            ReportSecondClassBean<ExpendInfo> reportSecondClass = new ReportSecondClassBean<>();
            reportSecondClass.topClass = expendInfo.getTopClass();
            reportSecondClass.reportClass = new ArrayList<>();
            reportSecondClass.reportClass.add(expendInfo);

            return reportSecondClass;
        }

        public static ReportSecondClassBean<IncomeInfo> build(IncomeInfo expendInfo) {
            ReportSecondClassBean<IncomeInfo> reportSecondClass = new ReportSecondClassBean<>();
            reportSecondClass.topClass = expendInfo.getTopClass();
            reportSecondClass.reportClass = new ArrayList<>();
            reportSecondClass.reportClass.add(expendInfo);

            return reportSecondClass;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}