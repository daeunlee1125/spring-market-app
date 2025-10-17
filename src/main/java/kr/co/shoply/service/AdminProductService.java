package kr.co.shoply.service;

import jakarta.transaction.Transactional;
import kr.co.shoply.dto.*;
import kr.co.shoply.entity.Cate1;
import kr.co.shoply.entity.Cate2;
import kr.co.shoply.mapper.AdminProductMapper;
import kr.co.shoply.repository.Cate1Repository;
import kr.co.shoply.repository.Cate2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;
    private final AdminProductMapper adminProductMapper;
    private final ModelMapper modelMapper;

    //1차 카테고리 조회
    public List<Cate1DTO> getAllCate1(){

        List<Cate1> cate1List = cate1Repository.findAll();
        log.info("cate1List={}",cate1List);

        return cate1List.stream()
                .map(cate1 -> modelMapper.map(cate1, Cate1DTO.class))
                .toList();
    }

    //2차 카테고리 조회
    public List<Cate2DTO> getCate2ByCate1(String cate1){
        int cate1_no = Integer.parseInt(cate1);

        List<Cate2> cate2List = cate2Repository.findAllByCate1No(cate1_no);

        return cate2List.stream()
                .map(cate2 -> modelMapper.map(cate2, Cate2DTO.class))
                .toList();
    }

    //상품목록
    public PageResponseDTO<ProductListDTO> getProductList(String memId, int memLevel , PageRequestDTO pageRequestDTO){

        // 전체 개수
        int total = adminProductMapper.countProductList(memId, memLevel, pageRequestDTO.getSearchType(), pageRequestDTO.getKeyword());


        // 목록 조회
        List<ProductListDTO> products = adminProductMapper.selectProductList(
                memId,
                memLevel,
                pageRequestDTO.getOffset(),
                pageRequestDTO.getSize(),
                pageRequestDTO.getSearchType(),
                pageRequestDTO.getKeyword()
        );

        return PageResponseDTO.<ProductListDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(products)
                .total(total)
                .build();
    }


    //상품등록
    @Transactional
    public void registerProduct(ProductRegisterDTO productRegisterDTO, String memId) {

        ProductDTO productDTO = ProductDTO.builder()
                .cate2_no(productRegisterDTO.getCate2_no())
                .prod_name(productRegisterDTO.getProd_name())
                .prod_info(productRegisterDTO.getProd_info())
                .prod_company(productRegisterDTO.getProd_company())
                .prod_price(productRegisterDTO.getProd_price())
                .prod_sale(productRegisterDTO.getProd_sale())
                .prod_point(productRegisterDTO.getProd_point())
                .prod_stock(productRegisterDTO.getProd_stock())
                .prod_deliv_price(productRegisterDTO.getProd_deliv_price())
                .mem_id(memId)
                .prod_sold(0)
                .prod_hit(0)
                .build();

        log.info("#### registerProduct ====> productDTO={}",productDTO);

        int result = adminProductMapper.insertProduct(productDTO);

        if(result == 0) {
            throw new RuntimeException("상품 INSERT 실패");
        }

        log.info("생성된 PROD_NO: {}", productDTO.getProd_no());

        String prodNo = productDTO.getProd_no();

        MultipartFile[] files = {
                productRegisterDTO.getFile1(),
                productRegisterDTO.getFile2(),
                productRegisterDTO.getFile3(),
                productRegisterDTO.getFile4()
        };

        for(int i = 0; i < files.length; i++) {
            if(files[i] != null && !files[i].isEmpty()) {
                log.info("#### registerProduct ====> files={}",files[i]);
                uploadProductFile(files[i], prodNo, i + 1);
            }
        }

        insertProductNotices(productRegisterDTO, prodNo);
        insertProductOptions(productRegisterDTO, prodNo);

    }

    // 상품등록 - 파일 업로드, INSERT 메서드 (private : 해당 service에서만 사용)
    private void uploadProductFile(MultipartFile file, String prodNo, int fileType) {
        if(file == null || file.isEmpty()) {
            throw new RuntimeException("파일이 없습니다.");
        }

        try {

            // 업로드 경로 설정(기본은 e2c)
            String baseUploadPath = "/home/ec2-user/shoply/uploads/product/";

            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("win")) {
                // Windows - 로컬
                baseUploadPath = "C:/uploads/product/";
            } else {
                // Linux - 서버
                baseUploadPath = "/home/ec2-user/shoply/uploads/product/";
            }

            // 디렉토리 생성 (없으면)
            File uploadDir = new File(baseUploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                log.info("디렉토리 생성 결과: {}", created);
            }

            // 파일명 생성
            String uuid = UUID.randomUUID().toString();
            String originalName = file.getOriginalFilename();

            if (originalName == null) {
                throw new IllegalArgumentException("파일명이 null입니다.");
            }

            String safeName = StringUtils.cleanPath(originalName);
            safeName = safeName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String newName = uuid + "_" + safeName;

            // File 객체로 변경
            File destFile = new File(baseUploadPath + newName);

            log.info("#### uploadProductFile ====> 저장 경로: {}", destFile.getAbsolutePath());
            log.info("#### uploadProductFile ====> 파일 크기: {}", file.getSize());

            // 서버에 파일 저장

            file.transferTo(destFile);  // Path 대신 File 사용!
            log.info("#### uploadProductFile ====> 파일 저장 성공!");



            String dbPath = "/uploads/product/" + newName;

            log.info("#### uploadProductFile ====> dbPath={}", dbPath);

            // DB에 저장할 정보
            ProFileDTO fileDTO = ProFileDTO.builder()
                    .prod_no(prodNo)
                    .f_name(dbPath)
                    .f_dist(fileType)
                    .build();

            log.info("#### uploadProductFile ====> fileDTO={}", fileDTO);

            // DB INSERT
            int result = adminProductMapper.insertProductFile(fileDTO);

            if(result == 0) {
                throw new RuntimeException("파일업로드 INSERT 실패");
            }

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 상품등록 - 고시정보 INSERT 메서드
    private void insertProductNotices(ProductRegisterDTO dto, String prodNo) {
        String[] noticeNames = {
                "상품상태",
                "부가세 면세여부",
                "영수증 발행",
                "사업자구분",
                "원산지"
        };

        String[] noticeVals = {
                dto.getNot_val1(),
                dto.getNot_val2(),
                dto.getNot_val3(),
                dto.getNot_val4(),
                dto.getNot_val5()
        };

        for(int i = 0; i < 5; i++) {
            if(noticeVals[i] != null && !noticeVals[i].isEmpty()) {
                ProdNoticeDTO noticeDTO = ProdNoticeDTO.builder()
                        .prod_no(prodNo)
                        .not_name(noticeNames[i])
                        .not_val(noticeVals[i])
                        .build();

                log.info("#### insertProductNotices ==> noticeDTO={}", noticeDTO);

                int result = adminProductMapper.insertProductNotice(noticeDTO);
                if(result == 0) {
                    throw new RuntimeException("고시정보 INSERT 실패");
                }
            }
        }
    }

    // 상품등록 - 옵션 INSERT 메서드
    private void insertProductOptions(ProductRegisterDTO dto, String prodNo) {
        List<String> optNames = dto.getOptNames();
        List<String> optVals = dto.getOptVals();

        if(optNames == null || optVals == null) {
            return; // 옵션 없으면 스킵
        }

        for(int i = 0; i < optNames.size(); i++) {
            String optName = optNames.get(i);
            String optVal = optVals.get(i);

            if(optName != null && !optName.isEmpty()) {
                ProdOptionDTO optionDTO = ProdOptionDTO.builder()
                        .prod_no(prodNo)
                        .opt_name(optName)
                        .opt_val(optVal)
                        .build();

                log.info("#### insertProductOptions ==> optionDTO={}", optionDTO);

                int result = adminProductMapper.insertProductOption(optionDTO);
                if(result == 0) {
                    throw new RuntimeException("옵션 INSERT 실패");
                }
            }
        }
    }

    //상품목록 선택삭제
    @Transactional
    public void deleteProducts(List<String> prodNos, String memId, int memLevel) {

        for(String prodNo : prodNos) {

            // 권한 체크 (level 2는 본인 것만 삭제 가능)
            if(memLevel == 2) {
                String owner = adminProductMapper.selectProductOwner(prodNo);
                if(!memId.equals(owner)) {
                    throw new RuntimeException("삭제 권한이 없습니다.");
                }
            }

            // 파일 정보 조회
            List<ProFileDTO> files = adminProductMapper.selectProductFiles(prodNo);

            // DB 삭제
            adminProductMapper.deleteProductOptions(prodNo);
            adminProductMapper.deleteProductNotices(prodNo);
            adminProductMapper.deleteProductFiles(prodNo);
            adminProductMapper.deleteProduct(prodNo);

            // 실제 파일 삭제
            deletePhysicalFiles(files);
        }
    }

    // 실제 파일 삭제
    private void deletePhysicalFiles(List<ProFileDTO> files) {
        for(ProFileDTO file : files) {
            try {
                Path filePath = Paths.get(file.getF_name());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("파일 삭제 실패: " + file.getF_name(), e);
            }
        }
    }
}
