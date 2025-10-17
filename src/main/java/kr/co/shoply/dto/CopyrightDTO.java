package kr.co.shoply.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CopyrightDTO {
    private String sCopy;
    private String sTitle;
    private String verVersion;
}
