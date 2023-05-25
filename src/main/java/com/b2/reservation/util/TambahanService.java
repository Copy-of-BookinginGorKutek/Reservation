package com.b2.reservation.util;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.util.commands.CreateAirMineral;
import com.b2.reservation.util.commands.CreateRaket;
import com.b2.reservation.util.commands.CreateShuttlecock;
import com.b2.reservation.util.commands.TambahanCommand;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TambahanService {
    @Autowired
    TambahanUtils tambahanUtils;

    public void createTambahanForReservasi(Reservasi reservasi, Map<String, Integer> tambahanQuantity){
        for (Map.Entry<String, Integer> entry : tambahanQuantity.entrySet()) {
            TambahanCommand tambahanCommand = null;
            if(entry.getKey().equals("RAKET"))
                tambahanCommand = new CreateRaket(reservasi, entry.getValue(), tambahanUtils);
            else if(entry.getKey().equals("AIR_MINERAL"))
                tambahanCommand = new CreateAirMineral(reservasi, entry.getValue(), tambahanUtils);
            else
                tambahanCommand = new CreateShuttlecock(reservasi, entry.getValue(), tambahanUtils);
            TambahanCommandInvoker tambahanCommandInvoker = new TambahanCommandInvoker(tambahanCommand);
            tambahanCommandInvoker.executeCommand();
        }
    }

    @Generated
    public Integer getCost(Reservasi reservasi){
        return tambahanUtils.calculateTambahanCost(reservasi);
    }
}
