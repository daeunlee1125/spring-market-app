package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitDTO {

    private int rec_no;
    private String rec_title;
    private String rec_department;
    private String rec_experience;
    private String rec_job_type;
    private String rec_start_time;
    private String rec_end_date;
    private String rec_status;
    private String rec_content;
    private String rec_qualification;
    private String rec_welfare;
    private String rec_rdate;

    public String getRec_start_date() {
        return rec_start_time != null ? rec_start_time.substring(0, 10) : "";
    }

    public String getRec_end_date() {
        return rec_end_date != null ? rec_end_date.substring(0, 10) : "";
    }

    public String getRec_rdate() {
        return rec_rdate != null ? rec_rdate.substring(0, 10) : "";
    }
}
