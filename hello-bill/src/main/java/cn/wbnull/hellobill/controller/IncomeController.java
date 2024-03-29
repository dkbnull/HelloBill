package cn.wbnull.hellobill.controller;

import cn.wbnull.hellobill.common.model.RequestModel;
import cn.wbnull.hellobill.common.model.ResponseModel;
import cn.wbnull.hellobill.common.model.expend.DeleteRequestModel;
import cn.wbnull.hellobill.common.model.expend.QueryRequestModel;
import cn.wbnull.hellobill.common.model.income.AddRequestModel;
import cn.wbnull.hellobill.common.model.income.QueryListRequestModel;
import cn.wbnull.hellobill.common.model.income.UpdateRequestModel;
import cn.wbnull.hellobill.db.entity.IncomeInfo;
import cn.wbnull.hellobill.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收入信息接口
 *
 * @author dukunbiao(null)  2021-01-01
 * https://github.com/dkbnull/HelloBill
 */
@RestController
@Scope("prototype")
@RequestMapping("income")
public class IncomeController extends BaseController {

    @Autowired
    private IncomeService incomeService;

    /**
     * 收入信息明细查询接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "queryList")
    public ResponseModel<List<IncomeInfo>> queryList(@RequestBody @Validated RequestModel<QueryListRequestModel> request,
                                                     BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.queryList(request);
    }

    /**
     * 收入信息查询分类信息接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "classQuery")
    public ResponseModel<List<String>> classQuery(@RequestBody @Validated RequestModel<Object> request,
                                                  BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.classQuery(request);
    }

    /**
     * 新增收入信息接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "add")
    public ResponseModel<Object> add(@RequestBody @Validated RequestModel<AddRequestModel> request,
                                     BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.add(request);
    }

    /**
     * 收入信息查询接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "query")
    public ResponseModel<IncomeInfo> query(@RequestBody @Validated RequestModel<QueryRequestModel> request,
                                           BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.query(request);
    }

    /**
     * 修改收入信息接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "update")
    public ResponseModel<Object> update(@RequestBody @Validated RequestModel<UpdateRequestModel> request,
                                        BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.update(request);
    }

    /**
     * 删除收入信息接口
     *
     * @param request
     * @param result
     * @return
     * @throws Exception
     */
    @PostMapping(value = "delete")
    public ResponseModel<Object> delete(@RequestBody @Validated RequestModel<DeleteRequestModel> request,
                                        BindingResult result) throws Exception {
        super.validate(result);

        return incomeService.delete(request);
    }
}
