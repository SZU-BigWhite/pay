package pay.listener;

import pay.dao.CouponMapper;
import com.google.gson.Gson;
import pay.dao.UserCouponMapper;
import pay.pojo.Coupon;
import pay.pojo.UserCoupon;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "couponNotify")
public class CouponListener {

    @Autowired
    UserCouponMapper userCouponMapper;
    @Autowired
    CouponMapper couponMapper;

    @RabbitHandler
    public void getCoupon(String msg){
        UserCoupon userCoupon=new Gson().fromJson(msg,UserCoupon.class);
        //优惠券库存-1
        Coupon coupon = couponMapper.selectByProductId(userCoupon.getProductid());
        System.out.println("couponNum:"+coupon.getNum());
        coupon.setNum(coupon.getNum()-1);
        couponMapper.updateByPrimaryKeySelective(coupon);
        //优惠券存入用户数据库
        userCouponMapper.insertSelective(userCoupon);
    }
}
