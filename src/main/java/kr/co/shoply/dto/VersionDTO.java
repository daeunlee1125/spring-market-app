package kr.co.shoply.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionDTO {

    private int ver_no;
    private String ver_version;
    private String ver_description;
    private String ver_writer;
    public String getVer_rdate() {
        return ver_rdate.substring(0, 19).replace("T", " ");
    }

    private String ver_rdate;

}
