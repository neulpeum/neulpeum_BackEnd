package com.medicare.neulpeum.dto;

import com.medicare.neulpeum.domain.entity.ConsultContentInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ConsultResponseDto {
    private Long consultId;
    private String providerName;
    private String takingDrug;
    private Date consultDate;

    public ConsultResponseDto(ConsultContentInfo consultContentInfo) {
        this.consultId = consultContentInfo.getConsultId();
        this.providerName = consultContentInfo.getProviderName();
        this.takingDrug = consultContentInfo.getTakingDrug();
        this.consultDate = consultContentInfo.getConsultDate();
    }
}
