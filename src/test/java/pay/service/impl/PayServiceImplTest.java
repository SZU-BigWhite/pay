package pay.service.impl;

import pay.PayApplicationTests;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PayServiceImplTest extends PayApplicationTests {

    @Autowired
    private PayServiceImpl payServiceImpl;
    @Test
    public void create() {
        payServiceImpl.create("415626351", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
}