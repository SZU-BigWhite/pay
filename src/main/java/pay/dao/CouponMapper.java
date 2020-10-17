package pay.dao;

import pay.pojo.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    Coupon selectByProductId(Integer productId);
}