package com.medicare.neulpeum.controller;

import com.medicare.neulpeum.domain.entity.PatientInfo;
import com.medicare.neulpeum.dto.*;
import com.medicare.neulpeum.service.ConsultService;
import com.medicare.neulpeum.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsultController {
    private final ConsultService consultService;
    private final DrugService drugService;

    //주민 상담 내용 추가
    @PostMapping("/patient/consult")
    public ResponseEntity<Long> postConsultInfo(@RequestBody ConsultRequestDto consultRequestDto) {
        try {
            Long consultId = consultService.save(consultRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(consultId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    //주민 상담 내역 조회(상담 리스트 조회)
    @GetMapping("/patient/consult")
    public ResponseEntity<List<ConsultResponseDto>> getConsult(@RequestParam PatientInfo patientId) {
        List<ConsultResponseDto> consultResponseDto = consultService.findAllByPatientId(patientId);
        if (consultResponseDto != null) {
            return ResponseEntity.ok(consultResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //주민 상담 내용 상세 조회
    @GetMapping("/patient/consultInfo")
    public ResponseEntity<ConsultDetailResponseDto> getConsultDetail(@RequestParam Long consultId) {
        ConsultDetailResponseDto consultDetailResponseDto = consultService.findByConsultId(consultId);
        if (consultDetailResponseDto != null) {
            return ResponseEntity.ok(consultDetailResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //주민 상담 내용 상세 조회 - 약 이름 & 제공한 약 개수
    @GetMapping("/patient/consultInfo/providedDrug")
    public ResponseEntity<List<ConsultDrugResponseDto>> getConsultDetailDrug(@RequestParam Long consultId) {
        List<ConsultDrugResponseDto> consultDrugResponseDtoList = consultService.findDrugInfoByConsultId(consultId);
        if (!consultDrugResponseDtoList.isEmpty()) {
            return ResponseEntity.ok(consultDrugResponseDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //주민 상담 상세 내용 수정
    @PutMapping("/patient/consultInfo")
    public ResponseEntity<?> updateConsultInfo(@RequestBody ConsultUpdateRequestDto consultUpdateRequestDto) {
        try {
            consultService.update(consultUpdateRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("주민 상담 상세 내용 수정 완료");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상담 정보를 찾을 수 없습니다.");
            } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("주민 상담 내용 수정 중 오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("/patient/consultDelete")
    public ResponseEntity<?> deleteConsultInfo(@RequestParam Long consultId) {
        try {
            consultService.delete(consultId);
            return ResponseEntity.status(HttpStatus.OK).body("주민 상담 상세 내용 삭제 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상담 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
