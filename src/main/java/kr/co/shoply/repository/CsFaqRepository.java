package kr.co.practice.repository;


import kr.co.practice.entity.CsFaq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsFaqRepository extends JpaRepository<CsFaq, Integer> {
}
