/**
 * web js
 *
 * @author dukunbiao(null)  2020-12-29
 * https://github.com/dkbnull/HelloBill
 */
doPost = function (url, data, callback) {
    const $ = layui.jquery;

    let loading = layer.load(1, {
        shade: false,
    });

    if (data == null) {
        data = {};
    }

    const timestamp = Math.round(new Date().getTime());
    const nonce = Math.round(Math.random() * 10000000000);

    const request = {
        username: isEmpty(getItem("username")) ? data.username : getItem("username"),
        timestamp: timestamp,
        nonce: nonce,
        sign: "sign",
        data: data
    };

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=UTF-8",
        url: url,
        data: JSON.stringify(request),
        headers: {"token": getItem("token")},
        dataType: "json",
        success: function (result) {
            if (isEmpty(result)) {
                layer.alert("返回参数为空");
                layer.close(loading);
                return;
            }

            if (isTokenExpired(result.code)) {
                layer.confirm(result.message + '，是否重新登录？', function (index) {
                    localStorage.clear();
                    parent.window.location.href = 'index.html';
                    layer.close(index);
                });

                layer.close(loading);

                return;
            }

            if (!isSuccess(result.code)) {
                layer.msg(result.message);
                layer.close(loading);
                return;
            }

            callback(result);
            layer.close(loading);
        },
        error: function (XMLHttpRequest) {
            callback(XMLHttpRequest.responseJSON);
            layer.close(loading);
        }
    });
}

isSuccess = function (code) {
    return code === "1000";
}

isTokenExpired = function (code) {
    return code.length === 4 && code.startsWith("30");
}