package kr.co.shoply.service;

import kr.co.shoply.dto.AdInfoDTO;
import kr.co.shoply.dto.CsNoticeDTO;
import kr.co.shoply.dto.SiteInfoDTO;
import kr.co.shoply.dto.VersionDTO;
import kr.co.shoply.entity.CsNotice;
import kr.co.shoply.mapper.*;
import kr.co.shoply.repository.CsNoticeRepository;
import kr.co.shoply.repository.SiteInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteInfoService {


    private final SiteInfoMapper siteInfoMapper;
    private final VisitsMapper visitsMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final MemberMapper memberMapper;
    private final QnaMapper qnaMapper;
    private final CsNoticeMapper csNoticeMapper;

    public List<CsNoticeDTO> getCsNotList(){

        return csNoticeMapper.noticeList2();
    }

    public AdInfoDTO getAdminInfos(){

        AdInfoDTO dto1 = orderItemMapper.itemStatCnt2();
        AdInfoDTO dto2 = orderMapper.ordStatCnt2();
        AdInfoDTO dto3 = memberMapper.regCnt2();
        AdInfoDTO dto4 = qnaMapper.qnaCnt2();



        return AdInfoDTO.builder()
                .st1Total(dto1.getSt1Total())
                .st5Total(dto1.getSt5Total())
                .st6Total(dto1.getSt6Total())
                .st1(dto2.getSt1())
                .st3(dto2.getSt3())
                .ordNum(dto2.getOrdNum())
                .todOrdNum(dto2.getTodOrdNum())
                .yesOrdNum(dto2.getYesOrdNum())
                .ordTotal(dto2.getOrdTotal())
                .todOrdTotal(dto2.getTodOrdTotal())
                .yesOrdTotal(dto2.getYesOrdTotal())
                .regTotal(dto3.getRegTotal())
                .todRegTotal(dto3.getTodRegTotal())
                .yesRegTotal(dto3.getYesRegTotal())
                .qnaTotal(dto4.getQnaTotal())
                .todQnaTotal(dto4.getTodQnaTotal())
                .yesQnaTotal(dto4.getYesQnaTotal())
                .build();
    }


    public SiteInfoDTO getSiteInfo(int s_no) {
        return siteInfoMapper.selectRecentOne(s_no);
    }

    public void modifySiteInfo(SiteInfoDTO siteInfoDTO, int type, MultipartFile img1, MultipartFile img2, MultipartFile img3) throws IOException {
        siteInfoDTO.setS_no(1);

        if (type==1){
            siteInfoMapper.modifyTitles(siteInfoDTO);
        }
        else if (type==2){
            String baseUploadPath1 = "/home/ec2-user/shoply/uploads/logo/header/";
            String baseUploadPath2 = "/home/ec2-user/shoply/uploads/logo/footer/";
            String baseUploadPath3 = "/home/ec2-user/shoply/uploads/logo/favicon/";
            Files.createDirectories(Paths.get(baseUploadPath1));
            Files.createDirectories(Paths.get(baseUploadPath2));
            Files.createDirectories(Paths.get(baseUploadPath3));

            if (!img1.isEmpty()) {
                // 파일명 생성
                String uuid = UUID.randomUUID().toString();
                String oname = img1.getOriginalFilename();

                String sfname = StringUtils.cleanPath(oname);
                sfname = sfname.replaceAll("[^a-zA-Z0-9._-]", "_");

                String nname = uuid + "_" + sfname;

                // 서버 저장
                Path filePath = Paths.get(baseUploadPath1 + nname);
                img1.transferTo(filePath);

                // DB 경로 지정
                String dbPath1 = "/uploads/logo/header/" + nname;
                siteInfoDTO.setS_logo1(dbPath1);

                siteInfoMapper.modifyLogo1(siteInfoDTO);
            }
            if (!img2.isEmpty()) {
                // 파일명 생성
                String uuid = UUID.randomUUID().toString();
                String oname = img2.getOriginalFilename();

                String sfname = StringUtils.cleanPath(oname);
                sfname = sfname.replaceAll("[^a-zA-Z0-9._-]", "_");

                String nname = uuid + "_" + sfname;

                // 서버 저장
                Path filePath = Paths.get(baseUploadPath2 + nname);
                img2.transferTo(filePath);

                // DB 경로 지정
                String dbPath2 = "/uploads/logo/footer/" + nname;
                siteInfoDTO.setS_logo2(dbPath2);

                siteInfoMapper.modifyLogo2(siteInfoDTO);
            }
            if (!img3.isEmpty()) {
                String uuid = UUID.randomUUID().toString();
                String oname = img3.getOriginalFilename();

                String sfname = StringUtils.cleanPath(oname);
                sfname = sfname.replaceAll("[^a-zA-Z0-9._-]", "_");

                String nname = uuid + "_" + sfname;

                Path filePath = Paths.get(baseUploadPath3 + nname);
                img3.transferTo(filePath);

                String dbPath3 = "/uploads/logo/favicon/" + nname;
                siteInfoDTO.setS_logo3(dbPath3);

                siteInfoMapper.modifyLogo3(siteInfoDTO);
            }


        }
        else if (type==3){
            siteInfoMapper.modifyFooters(siteInfoDTO);
        }
        else if (type==4){
            siteInfoMapper.modifyCsInfos(siteInfoDTO);
        }
        else if (type==5){
            siteInfoMapper.modifyCopy(siteInfoDTO);
        }
    }

    public void logVisit() {
        visitsMapper.insertVisit();
    }

    public int getTodayCnt() {
        return visitsMapper.visitCount(LocalDate.now());
    }

    public int getYesterdayCnt() {
        return visitsMapper.visitCount(LocalDate.now().minusDays(1));
    }

    public int getVisitCnt(){
        return visitsMapper.visitCount2();
    }


}
