package com.learningcrew.linkup.place.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPlaceListRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Integer sportId;
    private String address;
    private Integer ownerId;
    private Boolean isActive;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
    public Boolean getActive() {
        return isActive;
    }
}

