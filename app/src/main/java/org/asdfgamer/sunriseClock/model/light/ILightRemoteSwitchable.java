package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteSwitchable {

    boolean SWITCHABLE = true;

    boolean isOn();

    void requestSetOn(boolean on);
}
