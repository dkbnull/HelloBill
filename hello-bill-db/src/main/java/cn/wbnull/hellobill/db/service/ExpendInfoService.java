package cn.wbnull.hellobill.db.service;

import cn.wbnull.hellobill.common.constant.ClassTypeEnum;
import cn.wbnull.hellobill.common.model.common.QueryListRequestModel;
import cn.wbnull.hellobill.common.util.StringUtils;
import cn.wbnull.hellobill.db.entity.ClassInfo;
import cn.wbnull.hellobill.db.entity.ExpendInfo;
import cn.wbnull.hellobill.db.mapper.ClassInfoMapper;
import cn.wbnull.hellobill.db.mapper.ExpendInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支出信息表 服务类
 *
 * @author dukunbiao(null)  2020-12-31
 * https://github.com/dkbnull/HelloBill
 */
@Service
public class ExpendInfoService {

    @Autowired
    private ExpendInfoMapper expendInfoMapper;

    @Autowired
    private ClassInfoMapper classInfoMapper;

    public List<ExpendInfo> getExpendInfos(String username, QueryListRequestModel request) {
        LambdaQueryWrapper<ExpendInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExpendInfo::getUsername, username);
        queryWrapper.like(!StringUtils.isEmpty(request.getTopClass()), ExpendInfo::getTopClass, request.getTopClass());
        queryWrapper.like(!StringUtils.isEmpty(request.getSecondClass()), ExpendInfo::getSecondClass, request.getSecondClass());
        queryWrapper.like(!StringUtils.isEmpty(request.getDetail()), ExpendInfo::getDetail, request.getDetail());
        queryWrapper.ge(!StringUtils.isEmpty(request.getBeginDate()), ExpendInfo::getExpendTime, request.getBeginDate() + " 00:00:00");
        queryWrapper.le(!StringUtils.isEmpty(request.getEndDate()), ExpendInfo::getExpendTime, request.getEndDate() + " 23:59:59");
        queryWrapper.orderBy(true, request.orderByAsc(), ExpendInfo::getExpendTime);

        return expendInfoMapper.selectList(queryWrapper);
    }

    public void addExpendInfo(String username, ExpendInfo expendInfo) {
        LambdaQueryWrapper<ClassInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassInfo::getSecondClass, expendInfo.getSecondClass());
        queryWrapper.eq(ClassInfo::getType, ClassTypeEnum.EXPEND.getTypeCode());
        ClassInfo classInfo = classInfoMapper.selectOne(queryWrapper);

        expendInfo.build(username, classInfo.getTopClass());
        expendInfoMapper.insert(expendInfo);
    }

    public ExpendInfo getExpendInfo(String id) {
        return expendInfoMapper.selectById(id);
    }

    public void updateExpendInfo(ExpendInfo expendInfo) {
        LambdaQueryWrapper<ClassInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassInfo::getSecondClass, expendInfo.getSecondClass());
        queryWrapper.eq(ClassInfo::getType, ClassTypeEnum.EXPEND.getTypeCode());
        ClassInfo classInfo = classInfoMapper.selectOne(queryWrapper);

        expendInfo.setTopClass(classInfo.getTopClass());

        expendInfoMapper.updateById(expendInfo);
    }

    public void deleteExpendInfo(String id) {
        expendInfoMapper.deleteById(id);
    }

    public List<ExpendInfo> getExpendReportByClass(String username, String reportDate) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("topClass, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')", reportDate);
        queryWrapper.groupBy("topClass");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportBySecondClass(String username, String reportDate) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("topClass, secondClass, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')", reportDate);
        queryWrapper.groupBy("topClass, secondClass");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportByDate(String username, String reportDate) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("topClass, DATE_FORMAT(expendTime, '%Y-%m') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')",
                reportDate.substring(0, 4));
        queryWrapper.groupBy("topClass", "DATE_FORMAT(expendTime, '%Y-%m')");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportByDateSum(String username, String reportDate) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y-%m') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.like("DATE_FORMAT(expendTime, '%Y-%m-%d %H:%i:%s')",
                reportDate.substring(0, 4));
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y-%m')");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y-%m')");

        return expendInfoMapper.selectList(queryWrapper);
    }

    public List<ExpendInfo> getExpendReportSum(String username) {
        QueryWrapper<ExpendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DATE_FORMAT(expendTime, '%Y') as remark, sum(amount) as amount");
        queryWrapper.eq("username", username);
        queryWrapper.groupBy("DATE_FORMAT(expendTime, '%Y')");
        queryWrapper.orderByAsc("DATE_FORMAT(expendTime, '%Y')");

        return expendInfoMapper.selectList(queryWrapper);
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
