package pay.service;



import pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

public interface IPayService {
    /**
     * 创建/发起支付
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 支付完成异步通知
     */
    String asyncNotify(String notifyData);

    /**
     * 查询交易记录
     * @param orderId
     * @return PayInfo
     */
    public PayInfo queryByOrderId(String orderId);
}

