package cn.wbnull.hellobill.db.service;

import cn.wbnull.hellobill.common.core.util.DateUtils;
import cn.wbnull.hellobill.common.core.util.StringUtils;
import cn.wbnull.hellobill.db.param.QueryListParam;
import cn.wbnull.hellobill.db.entity.ExpendInfo;
import cn.wbnull.hellobill.db.mapper.ExpendInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 支出信息表 服务类
 *
 * @author null  2020-12-31
 * https://github.com/dkbnull/HelloBill
 */
@Service
public class ExpendInfoService {

    @Autowired
    private ExpendInfoMapper expendInfoMapper;

    public List<ExpendInfo> getExpendInfos(String username, QueryListParam param) {
        LambdaQueryWrapper<ExpendInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExpendInfo::getUsername, username);
        queryWrapper.like(!StringUtils.isEmpty(param.getTopClass()), ExpendInfo::getTopClass, param.getTopClass());
        queryWrapper.like(!StringUtils.isEmpty(param.getSecondClass()), ExpendInfo::getSecondClass, param.getSecondClass());
        queryWrapper.like(!StringUtils.isEmpty(param.getDetail()), ExpendInfo::getDetail, param.getDetail());
        queryWrapper.ge(!StringUtils.isEmpty(param.getBeginDate()), ExpendInfo::getExpendTime, param.getBeginDate() + " 00:00:00");
        queryWrapper.le(!StringUtils.isEmpty(param.getEndDate()), ExpendInfo::getExpendTime, param.getEndDate() + " 23:59:59");
        queryWrapper.orderBy(true, param.orderByAsc(), ExpendInfo::getExpendTime, ExpendInfo::getId);

        return expendInfoMapper.selectList(queryWrapper);
    }

    public void addExpendInfo(ExpendInfo expendInfo) {
        expendInfoMapper.insert(expendInfo);
    }

    public ExpendInfo getExpendInfo(String id) {
        return expendInfoMapper.selectById(id);
    }

    public void updateExpendInfo(ExpendInfo expendInfo) {
        expendInfoMapper.updateById(expendInfo);
    }

    public void deleteExpendInfo(String id) {
        expendInfoMapper.deleteById(id);
    }

    public List<ExpendInfo> getExpendReport(String username) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y')");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y')");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportNet(String username) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        List<String> topClassList = new ArrayList<>();
        topClassList.add("人情往来");
        topClassList.add("五险一金");
        queryWrapper.notIn("topClass", topClassList);
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y')");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y')");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportByDate(String username, String reportDate, String reportClass) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y-%m') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        convertQueryWrapper(queryWrapper, reportDate, reportClass);
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y-%m')");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y-%m')");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportByClass(String username, String reportDate, String reportClass) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y-%m') as remark, topClass, secondClass, sum(amount) as amount");
        queryWrapper.eq("username", username);
        convertQueryWrapper(queryWrapper, reportDate, reportClass);
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y-%m')", "topClass", "secondClass");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y-%m')", "secondClass");

        return expendInfoMapper.selectList(queryWrapper);
    }

    private void convertQueryWrapper(QueryWrapper<ExpendInfo> queryWrapper, String reportDate, String reportClass) {
        if (StringUtils.isEmpty(reportDate)) {
            queryWrapper.ge("expendTime", DateUtils.atStartOfMonth(11).atStartOfDay());
        } else {
//            queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')",
//                    reportDate.substring(0, 4));
            queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')", reportDate);
        }

        if ("1".equals(reportClass)) {
            queryWrapper.eq("topClass", "日常支出");
        }
        if ("2".equals(reportClass)) {
            queryWrapper.eq("topClass", "生活支出");
        }
        if ("3".equals(reportClass)) {
            queryWrapper.eq("topClass", "子女支出");
        }
    }

    public List<ExpendInfo> getExpendInfoByClass(String username, String reportDate, String topClass) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("secondClass, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.eq("topClass", topClass);
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')", reportDate);
        queryWrapper.groupBy("secondClass");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendInfoByDetail(String username, String reportDate, String topClass,
                                                  String secondClass) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("detail, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.eq("topClass", topClass);
        if (!StringUtils.isEmpty(secondClass)) {
            queryWrapper.eq("secondClass", secondClass);
        }
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')", reportDate);
        queryWrapper.groupBy("detail");
        queryWrapper.orderByDesc("amount");

        return expendInfoMapper.selectList(queryWrapper);
    }
}
