/**
 * expend-info.html js
 *
 * @author dukunbiao(null)  2020-12-31
 * https://github.com/dkbnull/HelloBill
 */
let $;

layui.use(['layer', 'table', 'laydate'], function () {
    if (!validate()) {
        return;
    }

    $ = layui.jquery;

    initDatetime();
    initMethod();
    doPostQuery(dateBeginTheMonth(), dateCalc(0));
});

function initDatetime() {
    const laydate = layui.laydate;

    laydate.render({
        elem: '#begin-time-input',
        theme: 'grid',
        value: dateBeginTheMonth(),
        max: 0,
        done: function (value, date) {
            doPostQuery(value, $('#end-time-input').val());
        }
    });
    laydate.render({
        elem: '#end-time-input',
        theme: 'grid',
        value: dateCalc(0),
        max: 0,
        done: function (value, date) {
            doPostQuery($('#begin-time-input').val(), value);
        }
    });
}

function initMethod() {
    const layer = layui.layer;

    $('.reload-info .layui-btn').on('click', function () {
        const method = $(this).data('method');
        active[method] ? active[method].call(this) : '';
    });

    const active = {
        reloadInfo: function () {
            doPostQuery($('#begin-time-input').val(), $('#end-time-input').val());
        },

        addInfo: function () {
            layer.open({
                type: 2
                , title: '记账'
                , content: 'expend-info-add.html'
                , area: ['350px', '500px']
                , maxmin: true
                , shade: 0
                , btn: ['确认', '取消']
                , yes: function (index, layero) {
                    const childWindow = layero.find('iframe')[0].contentWindow;
                    childWindow.addInfo();
                }
                , btn2: function () {
                    layer.closeAll();
                }
            });
        }
    };
}

function doPostQuery(beginTime, endTime) {
    const request = {
        username: localStorage.getItem("username"),
        beginTime: beginTime,
        endTime: endTime,
        topClass: $('#top-class-input').val(),
        secondClass: $('#second-class-input').val(),
        detail: $('#detail-input').val(),
        order: $('input[name="order"]:checked').val()
    };

    doPost("expend/queryList", request, callback)
}

function callback(result) {
    if (!isSuccess(result.code)) {
        layer.alert(result.message);
        return;
    }

    const table = layui.table;
    table.render({
        elem: '#info-table',
        data: result.data,
        cellMinWidth: 100,
        totalRow: true,
        cols: [[
            {field: 'expendTime', title: '时间', width: 180, sort: true},
            {field: 'topClass', title: '顶级分类', width: 110, sort: true},
            {field: 'secondClass', title: '二级分类', width: 110, sort: true},
            {field: 'detail', title: '明细'},
            {field: 'amount', title: '金额', width: 120, totalRow: true},
            {field: 'remark', title: '备注'},
            {fixed: 'right', title: '操作', toolbar: '#info-table-bar', width: 120}
        ]],
        page: true,
        limit: 10
    });

    table.on('tool(infoTable)', function (obj) {
        if (obj.event === 'del') {
            layer.confirm('是否删除当前支出明细？', function (index) {
                const request = {
                    username: localStorage.getItem("username"),
                    uuid: obj.data.uuid
                };

                doPost("expend/delete", request, callbackDelete)
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            layer.confirm('是否修改当前支出明细？', function (index) {
                layer.open({
                    type: 2
                    , title: '修改'
                    , content: 'expend-info-add.html?uuid=' + obj.data.uuid
                    , area: ['350px', '500px']
                    , maxmin: true
                    , shade: 0
                    , btn: ['确认', '取消']
                    , yes: function (index, layero) {
                        const childWindow = layero.find('iframe')[0].contentWindow;
                        childWindow.updateInfo();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });

                layer.close(index);
            });
        }
    });
}

function callbackDelete(result) {
    if (!isSuccess(result.code)) {
        layer.alert(result.message);
        return;
    }

    doPostQuery($('#begin-time-input').val(), $('#end-time-input').val());
}

function closeAll(message) {
    layer.closeAll();
    layer.msg(message, {time: 1000});

    doPostQuery($('#begin-time-input').val(), $('#end-time-input').val());
}