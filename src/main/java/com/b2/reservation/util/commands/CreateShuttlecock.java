package com.b2.reservation.util.commands;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.util.TambahanUtils;
import lombok.Generated;

public class CreateShuttlecock implements TambahanCommand {
    private final TambahanCategory tambahanCategory;
    private final Reservasi reservasi;
    private final Integer amount;
    private final TambahanUtils tambahanUtils;

    @Generated
    public CreateShuttlecock(Reservasi reservasi, Integer amount, TambahanUtils tambahanUtils){
        this.reservasi = reservasi;
        this.amount = amount;
        this.tambahanCategory = TambahanCategory.SHUTTLECOCK;
        this.tambahanUtils = tambahanUtils;
    }
    @Override
    public void execute(){
        tambahanUtils.createTambahan(reservasi, tambahanCategory, amount);
    }
}
