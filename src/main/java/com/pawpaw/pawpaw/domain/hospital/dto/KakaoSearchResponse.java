package com.pawpaw.pawpaw.domain.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoSearchResponse {

    private List<Document> documents;

    @Getter
    @Setter
    public static class Document {

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("address_name")
        private String addressName;

        private String phone;
        private String x; // 경도(longitude)
        private String y; // 위도(latitude)
    }
}
