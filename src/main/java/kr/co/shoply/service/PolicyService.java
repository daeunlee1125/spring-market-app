package kr.co.shoply.service;

import kr.co.shoply.entity.Terms;
import kr.co.shoply.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final TermsRepository termsRepository;

    public Terms getTerms() {
        return termsRepository.findById(4)
                .orElseThrow(() -> new IllegalArgumentException("약관을 찾을 수 없습니다."));
    }
}