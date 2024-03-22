package com.medicare.neulpeum.service;

import com.medicare.neulpeum.Repository.DrugRepository;
import com.medicare.neulpeum.domain.entity.DrugInfo;
import com.medicare.neulpeum.dto.DrugNameResponseDto;
import com.medicare.neulpeum.dto.DrugRequestDto;
import com.medicare.neulpeum.dto.DrugResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DrugServiceImpl implements DrugService{

    @Autowired
    DrugRepository drugRepository;

    @Override
    public void save(DrugRequestDto drugReq) {
        try {
            drugRepository.save(drugReq.toEntity(drugReq));
        } catch (Exception e) {
            log.error("DrugInfo 저장 중 오류 발생: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> findAll() {
        List<DrugInfo> drugInfos = drugRepository.findAll();

        List<DrugResponseDto> drugResponseDtoList =
                drugInfos.stream().map(
                        drugInfo -> new DrugResponseDto(drugInfo)).collect(Collectors.toList()
                );

        return drugResponseDtoList;
    }

    @Override
    public List<DrugResponseDto> findByDrugName(String drugName) {
        List<DrugInfo> findDrug = drugRepository.findByDrugName(drugName);

        List<DrugResponseDto> drugResponseDtoList = findDrug.stream()
                .map(drugInfo -> new DrugResponseDto(drugInfo))
                .collect(Collectors.toList());

        return drugResponseDtoList;
    }

    @Override
    public boolean existsByDrugId(Long id) {
        return drugRepository.existsById(id);
    }

    @Override
    public void update(DrugRequestDto drugRequestDto) {
        Long drugId = drugRequestDto.getDrugId();
        Optional<DrugInfo> optionalDrug = drugRepository.findById(drugId);
        if (optionalDrug.isPresent()) {
            DrugInfo existingDrug = optionalDrug.get();
            existingDrug.setDrugName(drugRequestDto.getDrugName());
            existingDrug.setExpireDate(drugRequestDto.getExpireDate());
            existingDrug.setUsableAmount(drugRequestDto.getUsableAmount());
            drugRepository.save(existingDrug);
        } else {
            // 약물을 찾지 못한 경우에도 저장하도록 수정
            drugRepository.save(drugRequestDto.toEntity(drugRequestDto));
        }
    }

    @Override
    public List<DrugNameResponseDto> getDistinctDrugNames() {
        List<String> distinctDrugNames = drugRepository.findDistinctDrugNames();

        List<DrugNameResponseDto> drugNameResponseDtoList = distinctDrugNames.stream()
                .map(DrugNameResponseDto::new)
                .collect(Collectors.toList());
        return drugNameResponseDtoList;
    }


}
