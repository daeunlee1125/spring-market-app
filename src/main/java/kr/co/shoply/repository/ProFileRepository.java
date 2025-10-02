package kr.co.shoply.repository;

import kr.co.shoply.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProFileRepository extends JpaRepository<Product,Integer> {
}
