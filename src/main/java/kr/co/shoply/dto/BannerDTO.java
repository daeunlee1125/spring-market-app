package kr.co.shoply.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerDTO {
    private int ban_no;
    private String ban_name;
    private String ban_img;
    private String ban_size;
    private String ban_bg_color;
    private int ban_location;
    private String ban_banner_link;
    private String ban_start_date;
    private String ban_end_date;
    private int ban_status;

    // 추가 필드
    public String getBan_start_ndate() {
        if (ban_start_date == null || ban_start_date.isEmpty()) {
            return ban_start_ndate;
        } else{
            return ban_start_date.substring(0, 10);
        }
    }
    private String ban_start_ndate;

    public String getBan_end_ndate() {
        if (ban_end_date == null || ban_end_date.isEmpty()) {
            return ban_end_ndate;
        } else{
            return ban_end_date.substring(0, 10);
        }
    }
    private String ban_end_ndate;

    public String getBan_start_time() {
        if (ban_start_date == null || ban_start_date.isEmpty()) {
            return ban_start_time;
        } else{
            return ban_start_date.substring(10, 19);
        }
    }
    private String ban_start_time;

    public String getBan_end_time() {
        if (ban_end_date == null || ban_end_date.isEmpty()) {
            return ban_end_time;
        } else{
            return ban_end_date.substring(10, 19);
        }
    }
    private String ban_end_time;
}
