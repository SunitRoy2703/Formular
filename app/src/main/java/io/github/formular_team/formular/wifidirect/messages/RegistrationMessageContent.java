package io.github.formular_team.formular.wifidirect.messages;

import io.github.formular_team.formular.wifidirect.WroupDevice;

public class RegistrationMessageContent {

    private WroupDevice wroupDevice;

    public WroupDevice getWroupDevice() {
        return wroupDevice;
    }

    public void setWroupDevice(WroupDevice wroupDevice) {
        this.wroupDevice = wroupDevice;
    }

}