package kr.co.shoply.service;

import kr.co.shoply.dto.CsFaqDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.entity.CsFaq;
import kr.co.shoply.entity.CsNotice;
import kr.co.shoply.repository.CsFaqRepository;
import kr.co.shoply.repository.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsService {

    private final CsNoticeRepository csNoticeRepository;
    private final ModelMapper modelMapper;
    private final CsFaqRepository csFaqRepository;

    public CsNoticeDTO getCsNotice(int cs_no) {

        Optional<CsNotice> optCsNotice = csNoticeRepository.findById(cs_no);

        if (optCsNotice.isPresent()) {
            CsNotice csNotice = optCsNotice.get();
            return modelMapper.map(csNotice, CsNoticeDTO.class);
        }
        return null;
    }

    public List<CsNoticeDTO> getCsNoticeAll(){
        List<CsNotice> list = csNoticeRepository.findAll();
        return list.stream()
                .map(csNotice -> modelMapper.map(csNotice, CsNoticeDTO.class))
                .toList();
    }

    public CsFaqDTO getCsFaq(int cs_faq_no) {

        Optional<CsFaq> optCsFaq = csFaqRepository.findById(cs_faq_no);
        if (optCsFaq.isPresent()) {
            CsFaq csFaq = optCsFaq.get();
            return modelMapper.map(csFaq, CsFaqDTO.class);
        }
        return null;
    }

    public List<CsFaqDTO> getCsFaqAll(){
        List<CsFaq> list = csFaqRepository.findAll();
        return list.stream()
                .map(csFap -> modelMapper.map(csFap, CsFaqDTO.class))
                .toList();
    }
}