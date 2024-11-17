package cn.wbnull.hellobill.common.model.expend;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 修改支出信息接口请求参数
 *
 * @author dukunbiao(null)  2021-01-17
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class UpdateRequestModel {

    @NotEmpty(message = "id 不能为空")
    private String id;

    @NotEmpty(message = "expendTime 不能为空")
    private String expendTime;

    @NotEmpty(message = "secondClass 不能为空")
    private String secondClass;

    @NotEmpty(message = "detail 不能为空")
    private String detail;

    @NotEmpty(message = "amount 不能为空")
    private String amount;

    private String remark;
}
