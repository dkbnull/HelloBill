/**
 * js
 *
 * @author null  2025-01-27
 * https://github.com/dkbnull/HelloBill
 */
let $, form, type, secondClass;

layui.use(['layer', 'form', 'laydate'], function () {
    $ = layui.jquery;
    form = layui.form;

    initData();
    initMethod();
})

function initData() {
    const search = window.location.search;
    const data = {
        id: search.substring(4, search.length)
    };

    doPost("import/query", data, callbackQuery)
}

function callbackQuery(result) {
    const data = result.data;
    type = data.billType;
    secondClass = data.secondClass;

    $("#bill-time-input").val(data.billTime);
    $("#detail-input").val(data.detail);
    $("#amount-input").val(data.amount);
    $("#remark-input").val(data.remark);

    form.render();

    initClass();
}

function initClass() {
    const data = {
        type: type
    };

    doPost("class/secondClassQuery", data, callbackClass)
}

function callbackClass(result) {
    for (let i = 0; i < result.data.length; i++) {
        $("#second-class-select").append('<option>' + result.data[i] + '</option>');
    }

    $("#second-class-select").val(secondClass);

    form.render();
}

function initMethod() {
    $('#second-class-select').on('keydown', function (event) {
        if (event.keyCode === 13) {
            $('#detail-input').focus();
            return false
        }
    });
    $('#detail-input').on('keydown', function (event) {
        if (event.keyCode === 13) {
            $('#amount-input').focus();
            return false
        }
    });
    $('#amount-input').on('keydown', function (event) {
        if (event.keyCode === 13) {
            $('#remark-input').focus();
            return false
        }
    });
    $('#remark-input').on('keydown', function (event) {
        if (event.keyCode === 13) {
            updateInfo();
            return false
        }
    });
}

function updateInfo() {
    const data = form.val('billInfo');
    if (!checkData(data)) {
        return
    }

    const search = window.location.search;
    data.id = search.substring(4, search.length);
    data.billType = type;
    doPost("import/update", data, callbackUpdate);
}

function checkData(data) {
    const error = $(".error");
    if (data.secondClass.length === 0) {
        error.text("分类不能为空");
        return false;
    }
    if (data.detail.length === 0) {
        error.text("明细不能为空");
        return false;
    }
    if (data.amount.length === 0) {
        error.text("金额不能为空");
        return false;
    }

    error.text("");
    return true;
}

function callbackUpdate(result) {
    const error = $(".error");

    if (!isSuccess(result.code)) {
        error.text(result.message);
        return;
    }

    parent.closeAll(result.message);
}
