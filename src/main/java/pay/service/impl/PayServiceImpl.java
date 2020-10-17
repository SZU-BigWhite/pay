package pay.service.impl;

import com.google.gson.Gson;
import pay.dao.PayInfoMapper;
import pay.enums.PayPlatformEnum;
import pay.pojo.PayInfo;
import pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service
public class PayServiceImpl implements IPayService {
    String PAY_NOTIFY_QUEUE="payNotify";
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    BestPayService bestPayService;
    @Override
    public PayResponse create(String orderId, BigDecimal amount,BestPayTypeEnum bestPayTypeEnum) {
        //写入数据库
        PayInfo payInfo=new PayInfo(Long.parseLong(orderId), PayPlatformEnum.getBestPayTypeEnum(bestPayTypeEnum).getCode(), OrderStatusEnum.NOTPAY.name(), amount);
        payInfoMapper.insertSelective(payInfo);

        PayRequest request=new PayRequest();
        request.setOrderName("9184922-OrderName");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(bestPayTypeEnum);

        PayResponse response = bestPayService.pay(request);
        log.info("发起订单response={}",response);

        return response;
    }

    @Override
    public String asyncNotify(String notifyData) {
        //签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("支付完成Response={}",payResponse);

        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if(payInfo==null)
        {
            //比较严重，短信通知
            new RuntimeException("不存在该订单");
        }
        //未支付
        if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name()))
        {
            //检验金额
            if(payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))!=0)
                throw new RuntimeException("异步通知的金额与应付进而不一致+orderId"+payResponse.getOrderId());
            //修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }
        //TODO 发送MQ消息  mall接收MQ消息
        amqpTemplate.convertAndSend(PAY_NOTIFY_QUEUE,new Gson().toJson(payInfo));

        //返回微信支付成功
        if(payResponse.getPayPlatformEnum()== BestPayPlatformEnum.WX)
        {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        }
        else if(payResponse.getPayPlatformEnum()==BestPayPlatformEnum.ALIPAY)
            return "success";
        throw new RuntimeException("异步通知错误的支付平台");
    }

    public PayInfo queryByOrderId(String orderId)
    {
        return payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
    }
}
