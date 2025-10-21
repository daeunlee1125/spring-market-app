package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Order;
import kr.co.shoply.entity.Product;
import kr.co.shoply.mapper.*;
import kr.co.shoply.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final CartMapper cartMapper;
    private final PointMapper pointMapper;
    private final CouponMapper couponMapper;
    private final OrderMapper orderMapper;
    private final Cate1Mapper cate1Mapper;
    private final Cate2Mapper cate2Mapper;
    private final ReviewMapper reviewMapper;

    public List<ProductDTO> getProductAll3(int cate2_no, String sort){
        return productMapper.selectAll3(cate2_no, sort);
    }

    public ProductDTO getProduct3(String prod_no){
        return productMapper.getProductDetail(prod_no);
    }

    public List<ProFileDTO> getFiles3(String prod_no){
        return productMapper.selectFiles3(prod_no);
    }

    public List<ProdOptionDTO> getProductOption3(String prod_no){
        return productMapper.selectOption3(prod_no);
    }

    public List<CartDTO> getCartAll3(String mem_id){
        return cartMapper.selectCartList3(mem_id);
    }

    public Boolean checkCart3(String mem_id, String prod_no, String cart_option){
        if (cartMapper.selectCartCount3(mem_id, prod_no, cart_option) > 0) {
            return true;
        }else {
            return false;
        }
    }

    public List<CartDTO> getSelectedCartList3(List<Integer> cart_no_list){
        return cartMapper.selectCartsByNos(cart_no_list);
    }

    public OrderDTO getOrderNo(String mem_id){
        return orderMapper.selectOrderNo3(mem_id);
    }

    public OrderDTO getOrderById(String orderNo){
        return orderMapper.selectOrderById(orderNo);
    }

    public int getPoint3(String mem_id){
        return pointMapper.selectPoint3(mem_id);
    }

    public List<SysCouponDTO> getUserCoupon3(String mem_id){
        return couponMapper.selectUserCoupon3(mem_id);
    }

    public SysCouponDTO getSysCoupon3(String cpCode){
        return couponMapper.selectSysCoupon3(cpCode);
    }

    public void saveOrderItem3(List<OrderItemDTO> list){
        orderMapper.insertOrderItemList3(list);
    }

    public List<ProductDTO> getSearchProduct3(String keyword, String sort){
        return productMapper.selectSearchProduct3(keyword, sort);
    }

    public List<ProductDTO> getSearch2Product3(String keyword,
                                               String sort,
                                               String type,
                                               String keyword2,
                                               Integer start_price,
                                               Integer end_price){

        return  productMapper.selectSearch2Product3(keyword, sort, type, keyword2, start_price, end_price);
    }

    public List<Cate1DTO> getCate1List(){
        return cate1Mapper.selectCate1_3();
    }

    public List<Cate2DTO> getCate2List(int cate1No){
        return cate2Mapper.selectAll3(cate1No);
    }

    public void modifyUsedCoupon3(String cpCode, String memId){
        couponMapper.updateUsedCoupon3(cpCode, memId);
    }

    public void saveUsedCoupon3(String memId, int pType, int pPoint, String pInfo, String ordNo){
        pointMapper.insertUsedPoint3(memId, pType, pPoint, pInfo, ordNo);
    }

    public void saveOrder3(String memId, String ordName, String ordHp, String ordZip, String ordAddr1, String ordAddr2, String payment, int ordTotal){
        orderMapper.insertOrder3(memId, ordName, ordHp, ordZip, ordAddr1, ordAddr2, payment, ordTotal);
    }

    public void insertCart3(String mem_id, String prod_no, int cart_item_cnt, String cart_option){
        cartMapper.insertCart3(mem_id, prod_no, cart_item_cnt, cart_option);
    }

    public void updateCartQuantity(CartUpdateDTO cartUpdateDTO) {
        cartMapper.updateCartQuantity(cartUpdateDTO);
    }

    public void deleteCart3(int cart_no){
        cartMapper.deleteCart3(cart_no);
    }
    public void deleteSelectedCarts3(List<Integer> cart_no_list){
        cartMapper.deleteSelectedCarts3(cart_no_list);
    }

    public List<CompleteDTO> getCompleteOrder3(String ord_no){
        return orderMapper.selectCompleteOrder3(ord_no);
    }


}
