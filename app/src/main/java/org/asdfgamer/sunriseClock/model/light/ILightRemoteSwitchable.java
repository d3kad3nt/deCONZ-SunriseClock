package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteSwitchable extends ICapability  {

    boolean isOn();

    void requestSetOn(boolean on);
}
