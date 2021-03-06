package com.a202.fishserver.dto.collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollectionPostRequestDto {
    public Integer length;
    public String location;
    public long fish_id;
    public String memo;
    public String bait;
    public String fishing_info;
    public long user_id;
    public String user_token;
    public String fish_image;
}
