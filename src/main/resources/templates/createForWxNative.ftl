<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>支付</title>
</head>
<body>

<div id="myQrcode"></div>
<div id="orderId" hidden>${orderId}</div>
<div id="returnUrl" hidden>${returnUrl}</div>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery('#myQrcode').qrcode({
        text: "${codeUrl}"
    });
    $(function () {
        setInterval(function () {
            console.log('开始查询支付状态')
            $.ajax({
                url: '/pay/queryByOrderId',
                data:{
                    'orderId':$('#orderId').text()
                },
                success: function (res) {
                    console.log(res);
                    if(res.platformStatus!=null && res.platformStatus==='SUCCESS')
                        location.href=$('#returnUrl').text()
                },
                error: function (err) {
                    alert(err);
                }
            })
        },2000)
    })

</script>
</body>
</html>