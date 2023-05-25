package com.b2.reservation.util;

import com.b2.reservation.util.commands.TambahanCommand;
import lombok.Generated;

@Generated
public class TambahanCommandInvoker {
    private final TambahanCommand tambahanCommand;
    public TambahanCommandInvoker(TambahanCommand tambahanCommand){
        this.tambahanCommand = tambahanCommand;
    }
    public void executeCommand(){
        tambahanCommand.execute();
    }
}
