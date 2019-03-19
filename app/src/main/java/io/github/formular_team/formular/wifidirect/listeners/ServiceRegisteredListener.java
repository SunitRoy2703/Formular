package io.github.formular_team.formular.wifidirect.listeners;

import com.skozubenko.wifip2p_3.WifiDirect.P2PError;
import com.skozubenko.wifip2p_3.WifiDirect.WroupServiceDevice;

import java.util.List;

public interface ServiceRegisteredListener {
    void onSuccessServiceRegistered();

    void onErrorServiceRegistered(P2PError wiFiP2PError);
}
