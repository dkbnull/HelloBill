package cn.wbnull.hellobill.common.model.expend;

import cn.wbnull.hellobill.common.model.RequestModel;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 支出信息明细查询接口请求参数
 *
 * @author dukunbiao(null)  2020-12-31
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class QueryListRequestModel extends RequestModel {

    private String beginTime;
    private String endTime;
    private String topClass;
    private String secondClass;
    private String detail;
    private String order;

    public boolean orderByDesc() {
        return "2".equals(this.order);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
