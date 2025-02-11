package com.storelink.projection;


public interface ShopProjection {
    Long getShopId();
    String getShopName();
    Double getLatitude();
    Double getLongitude();
    Double getDistance();
    
    // Address fields (instead of Address object)
    Long getAddressId();
    String getStreet();
    String getCity();
    String getState();
    String getPostalCode();
}

