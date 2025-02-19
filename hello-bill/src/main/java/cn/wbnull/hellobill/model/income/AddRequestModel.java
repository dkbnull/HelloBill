package cn.wbnull.hellobill.model.income;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 新增收入信息接口请求参数
 *
 * @author null  2021-01-01
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class AddRequestModel {

    @NotEmpty(message = "incomeDate 不能为空")
    private String incomeDate;

    @NotEmpty(message = "secondClass 不能为空")
    private String secondClass;

    @NotEmpty(message = "detail 不能为空")
    private String detail;

    @NotEmpty(message = "amount 不能为空")
    private String amount;

    private String remark;
}
