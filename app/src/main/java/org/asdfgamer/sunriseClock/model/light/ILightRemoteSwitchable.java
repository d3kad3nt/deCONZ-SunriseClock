package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteSwitchable extends ICapability  {

    String FILTER = "Switchable = 1";

    boolean isOn();

    void requestSetOn(boolean on);
}
