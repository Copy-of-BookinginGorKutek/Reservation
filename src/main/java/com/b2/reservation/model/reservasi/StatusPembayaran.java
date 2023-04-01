package com.b2.reservation.model.reservasi;

public enum StatusPembayaran {
    MENUNGGU_PEMBAYARAN(1),
    SUDAH_DIBAYAR(2),
    DITOLAK(3);

    private final Integer id;
    StatusPembayaran(Integer id){
        this.id = id;
    }

    public Integer getId(){ return this.id; }
}
