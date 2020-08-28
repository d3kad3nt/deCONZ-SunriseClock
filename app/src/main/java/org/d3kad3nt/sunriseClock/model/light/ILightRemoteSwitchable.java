package org.d3kad3nt.sunriseClock.model.light;

public interface ILightRemoteSwitchable extends ICapability  {

    boolean isOn();

    void requestSetOn(boolean on);
}
