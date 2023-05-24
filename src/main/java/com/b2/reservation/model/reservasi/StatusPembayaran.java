package com.b2.reservation.model.reservasi;

import lombok.Generated;

@Generated
public enum StatusPembayaran {
    MENUNGGU_PEMBAYARAN(1),
    MENUNGGU_KONFIRMASI(2),
    TERKONFIRMASI(3),
    BATAL(4);

    private final Integer id;

    private StatusPembayaran(Integer id){
        this.id = id;
    }

    public Integer getId(){ return this.id; }

}
