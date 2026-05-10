package com.pawpaw.pawpaw.domain.hospital.dto;

import com.pawpaw.pawpaw.domain.hospital.entity.Hospital;
import lombok.Getter;

@Getter
public class HospitalResponseDto {
    private Long id;
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String phone;
    private String hours;

    public HospitalResponseDto(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.lat = hospital.getLat();
        this.lng = hospital.getLng();
        this.phone = hospital.getPhone();
        this.hours = hospital.getHours();
    }
}
