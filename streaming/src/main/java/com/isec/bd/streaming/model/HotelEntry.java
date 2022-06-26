package com.isec.bd.streaming.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
// Order: id;price;level;levels;rooms;area;kitchen_area;geo_lat;geo_lon;building_type;object_type;postal_code;street_id;id_region;house_id
@JsonPropertyOrder({"id", "price", "level", "levels", "rooms", "area", "kitchen_area", "geo_lat", "geo_lon", "building_type",
        "object_type", "postal_code", "street_id", "id_region", "house_id"})
public class HotelEntry {
    private int id;
    private float price;
    private int level;
    private int levels;
    private int rooms;
    private float area;
    private float kitchen_area;
    private float geo_lat;
    private float geo_lon;
    private int building_type;
    private int object_type;
    private int postal_code;
    private int street_id;
    private int id_region;
    private int house_id;
}


