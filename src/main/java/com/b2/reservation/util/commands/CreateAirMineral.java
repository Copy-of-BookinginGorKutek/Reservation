package com.b2.reservation.util.commands;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.util.TambahanUtils;
import lombok.Generated;

@Generated
public class CreateAirMineral implements TambahanCommand {
    private final Integer amount;
    private final Reservasi reservasi;
    private final TambahanCategory tambahanCategory;
    private final TambahanUtils tambahanUtils;
    public CreateAirMineral(Reservasi reservasi, Integer amount, TambahanUtils tambahanUtils){
        this.reservasi = reservasi;
        this.amount = amount;
        this.tambahanCategory = TambahanCategory.AIR_MINERAL;
        this.tambahanUtils = tambahanUtils;
    }
    @Override
    public void execute(){
        tambahanUtils.createTambahan(reservasi, tambahanCategory, amount);
    }
}
