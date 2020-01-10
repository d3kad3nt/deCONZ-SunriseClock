package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteSwitchable {

    boolean isOn();

    void requestSetOn(boolean on);
}
