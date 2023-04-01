package com.b2.reservation.model.reservasi;

public enum TambahanCategory {
    RAKET(15000),
    AIR_MINERAL(5000),
    SHUTTLECOCK(5000);
    private final Integer price;
    private TambahanCategory(Integer price){
        this.price = price;
    }

    public Integer getPrice(){
        return this.price;
    }
}
