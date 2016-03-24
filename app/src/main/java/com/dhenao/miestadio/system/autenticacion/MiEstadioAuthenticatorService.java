package com.dhenao.miestadio.system.autenticacion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dhenao.miestadio.system.autenticacion.MiestadioAuthenticator;

public class MiEstadioAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        MiestadioAuthenticator authenticator = new MiestadioAuthenticator(this);
        return authenticator.getIBinder();
    }
}