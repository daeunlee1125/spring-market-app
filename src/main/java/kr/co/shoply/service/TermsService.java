package kr.co.shoply.service;


import kr.co.shoply.dto.TermsDTO;
import kr.co.shoply.entity.Qna;
import kr.co.shoply.entity.Terms;
import kr.co.shoply.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermsService {
    private final TermsRepository repository;
    private final ModelMapper modelMapper;


    public TermsDTO getTerms2(Integer t_no) {
        java.util.Optional<Terms> terms = repository.findById(t_no);
        if (terms.isPresent()) {
            TermsDTO dto = modelMapper.map(terms.get(), TermsDTO.class);
            return dto;
        }
        return null;
    }

    public void updateTerms2(TermsDTO dto) {
        if (repository.existsById(dto.getT_no())) {
            repository.save(modelMapper.map(dto, Terms.class));
        }
    }
}
