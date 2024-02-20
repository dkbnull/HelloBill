package cn.wbnull.hellobill.model.clazz;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 报表分类查询接口请求参数
 *
 * @author dukunbiao(null)  2024-02-11
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class ClassRequestModel {

    @NotEmpty(message = "type 不能为空")
    private String type;

    private String topClass;
}
