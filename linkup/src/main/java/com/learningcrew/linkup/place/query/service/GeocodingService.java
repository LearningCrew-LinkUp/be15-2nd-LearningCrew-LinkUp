package com.learningcrew.linkup.place.query.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

@Service
public class GeocodingService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public Optional<double[]> getCoordinates(String address) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                    .queryParam("query", address)
                    .build()
                    .toString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = new RestTemplate().exchange(
                    url, HttpMethod.GET, entity, String.class);

            JSONObject json = new JSONObject(response.getBody());
            JSONArray documents = json.getJSONArray("documents");

            if (documents.length() > 0) {
                JSONObject first = documents.getJSONObject(0);
                double lat = first.getDouble("y");
                double lng = first.getDouble("x");
                return Optional.of(new double[]{lat, lng});
            }

        } catch (org.json.JSONException e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
        }

        return Optional.empty();
    }


}

