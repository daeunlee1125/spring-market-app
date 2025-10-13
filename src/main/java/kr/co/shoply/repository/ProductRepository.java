package kr.co.shoply.repository;

import kr.co.shoply.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // DB 컬럼 이름(prod_no)을 직접 사용하여 쿼리 작성
    @Query("SELECT p FROM Product p WHERE p.prod_no = :prod_no")
    Optional<Product> findByProd_no(@Param("prod_no") String prod_no);
}