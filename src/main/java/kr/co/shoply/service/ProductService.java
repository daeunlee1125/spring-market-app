package kr.co.shoply.service;

import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Product;
import kr.co.shoply.mapper.CartMapper;
import kr.co.shoply.mapper.ProductMapper;
import kr.co.shoply.mapper.ReviewMapper;
import kr.co.shoply.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final CartMapper cartMapper;
    private final ReviewMapper reviewMapper;

    public List<ProductDTO> getProductAll3(int cate2_no, String sort){
        return productMapper.selectAll3(cate2_no, sort);
    }

    public ProductDTO getProduct3(String prod_no){
        return productMapper.select3(prod_no);
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
}
