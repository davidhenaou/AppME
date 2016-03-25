package com.dhenao.miestadio.system.autenticacion;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.XmlRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import  com.dhenao.miestadio.R;

import java.io.IOException;


public class LogueoActivity extends Activity {


    EditText CuentaNombre ,CuentaUsuario, CuentaCelular;
    String CuentaNombre1 ,CuentaUsuario1, CuentaCelular1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_registrar);

        findViewById(R.id.btnloguear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CuentaNombre = (EditText) findViewById(R.id.usuariologueo);
                CuentaUsuario = (EditText) findViewById(R.id.correologueo);
                CuentaCelular = (EditText) findViewById(R.id.celularlogueo);
                CuentaNombre1 = ((TextView) findViewById(R.id.usuariologueo)).getText().toString().trim();
                CuentaUsuario1 = ((TextView) findViewById(R.id.correologueo)).getText().toString().trim();
                CuentaCelular1 = ((TextView) findViewById(R.id.celularlogueo)).getText().toString().trim();


                if (!isEmptyFields(CuentaNombre.getText().toString().trim(), CuentaUsuario.getText().toString().trim(), CuentaCelular.getText().toString().trim())
                        && hasSizeValid(CuentaNombre.getText().toString().trim(), CuentaUsuario.getText().toString().trim(), CuentaCelular.getText().toString().trim())) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("com.dhenao.miestadio_preferences", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("UsuarioPref", CuentaNombre1);
                    editor.putString("CorreoPref", CuentaUsuario1);
                    editor.putString("CelularPref", CuentaCelular1);
                    editor.commit();

                    Boolean resp = creaCuenta();

                    if (resp) {
                        Intent intent = new Intent();
                        //intent.putExtra("resultado", "true");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }


    private boolean isEmptyFields(String usuario, String email, String celular) {
        if (TextUtils.isEmpty(usuario)) {
            CuentaNombre.requestFocus();
            CuentaNombre.setError("campo requerido");
            return true;
        } else if (TextUtils.isEmpty(email)) {
            CuentaUsuario.requestFocus();
            CuentaUsuario.setError("campo requerido");
            return true;
        } else if (TextUtils.isEmpty(celular)) {
            CuentaCelular.requestFocus();
            CuentaCelular.setError("campo requerido");
            return true;
        }
        return false;
    }

    private boolean hasSizeValid(String usuario, String email, String celular) {

        if (!(usuario.length() > 3)) {
            CuentaNombre.requestFocus();
            CuentaNombre.setError("nombre muy pequeño");
            return false;
        } else if (!(email.length() > 5)) {
            CuentaUsuario.requestFocus();
            CuentaUsuario.setError("correo invalido");
            return false;
        } else if (celular.length() != 10) {
            CuentaCelular.requestFocus();
            CuentaCelular.setError("Numero celular Invalido");
            return false;
        }
        return true;
    }

    /**
     * Limpa os ícones e as mensagens de erro dos campos desejados
     *
     * @param editTexts lista de campos do tipo EditText
     */
    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    private boolean creaCuenta() {
        try {
            Account account = new Account(CuentaUsuario1, "com.dhenao.miestadio.account");
            AccountManager am = AccountManager.get(this);
            boolean accountCreated = am.addAccountExplicitly(account, CuentaCelular1, null);


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (accountCreated) {  //Pass the new account back to the account manager
                    AccountAuthenticatorResponse response = extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
                    Bundle result = new Bundle();
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, CuentaUsuario1);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE, "com.dhenao.miestadio.account");
                    result.putString(AccountManager.KEY_PASSWORD, CuentaCelular1);
                    response.onResult(result);
                }
            }
            return true;
        } catch (NullPointerException  ex) {
            Log.e("ERROR ", "Error:" + ex);
            return false;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent();
            // intent.putExtra("resultado","false");
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}