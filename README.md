# 微信Native支付+支付宝PC支付

## 使用best-pay-sdk实现

  ### 支付过程
    从RabbitMQ消息队列读取订单相关信息
    创建微信/支付宝的支付请求
    获取微信/支付宝的异步通知结果
    将结果发送给RabbitMQ消息队列
   
   
    
