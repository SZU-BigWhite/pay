package pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

@Getter
public enum PayPlatformEnum {
    //1阿里，2微信
    ALIPAY(1),

    WX(2),
    ;
    Integer code;
    PayPlatformEnum(Integer code)
    {
        this.code=code;
    }
    public static PayPlatformEnum getBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum)
    {
        bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name());
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if(bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name()))
                return payPlatformEnum;
        }
        throw new RuntimeException("错误的支付平台"+bestPayTypeEnum.name());

    }
}
