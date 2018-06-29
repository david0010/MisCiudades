package com.example.david.misciudades;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.david.misciudades.constants.Constants;
import com.example.david.misciudades.helpers.InitializerHelper;
import com.example.david.misciudades.helpers.SharedPreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    private LoginTask loginTask = null;
    private EditText usuario;
    private EditText password;
    private View vistaProgreso;
    private View vistaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializerHelper.inicializarAppSiNecesario(this);
        boolean usuarioLogueado = SharedPreferencesHelper.getSharedPreference(getApplicationContext(), Constants.USUARIO_LOGUEADO);
        // Si el usuario ya está logueado, vamos a MainActivity
        if (usuarioLogueado) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        usuario = findViewById(R.id.usuario);
        password = findViewById(R.id.password);
        // Si pulsamos enter se intentará el Login, sin necesidad de pulsar en el botón "Entrar"
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    entrar();
                    return true;
                }
                return false;
            }
        });

        Button botonEntrar = findViewById(R.id.email_sign_in_button);
        botonEntrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                entrar();
            }
        });

        vistaLogin = findViewById(R.id.login_form);
        vistaProgreso = findViewById(R.id.iconoProgreso);
    }

    private void entrar() {
        if (loginTask != null) {
            return;
        }

        usuario.setError(null);
        password.setError(null);
        String usuarioValue = usuario.getText().toString();
        String passwordValue = this.password.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Se comprueba si la contraseña es válida
        if (TextUtils.isEmpty(passwordValue)) {
            this.password.setError(getString(R.string.campo_requerido));
            focusView = this.password;
            cancel = true;
        }

        // Se comprueba si el usuario es válido
        if (TextUtils.isEmpty(usuarioValue)) {
            usuario.setError(getString(R.string.campo_requerido));
            focusView = usuario;
            cancel = true;
        } else if (!isEmailValid(usuarioValue)) {
            usuario.setError(getString(R.string.error_email));
            focusView = usuario;
            cancel = true;
        }

        if (cancel) {
            // Se ha producido algún error. Se pone el foco en el primer campo que lo haya producido.
            focusView.requestFocus();
        } else {
            // Muestra pantalla de carga e intenta hacer Login
            mostrarCarga(true);
            loginTask = new LoginTask(usuarioValue, passwordValue);
            loginTask.execute((Void) null);
        }
    }

    // Se comprueba el formato del correo
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Se muestra el icono de carga y se ocultan el resto de elementos
    // Dependiendo de la versión, se mostrará de una u otra forma
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void mostrarCarga(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            vistaLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            vistaLogin.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vistaLogin.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            vistaProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
            vistaProgreso.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vistaProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            vistaProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
            vistaLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String usuario;
        private final String password;

        LoginTask(String email, String password) {
            this.usuario = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url = null;
            try {
                // URL donde se encontraría nuestra API REST
                url = new URL("https://api.misciudades.com/login");
                HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();
                myConnection.setRequestMethod("POST");
                // Incorporamos a la petición el usuario y contraseña introducidos
                String credenciales = usuario + ":" + password;
                String basicAuth = "Basic " + new String(Base64.encode(credenciales.getBytes(), Base64.DEFAULT));
                myConnection.setRequestProperty ("Authorization", basicAuth);
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setRequestProperty("Content-Type", "text/plain");
                // Se ntentará la conexión durante 4 segundos
                myConnection.setConnectTimeout(4000);
                if (myConnection.getResponseCode() == 200) {
                    String resultado = getStringFromInputStream(myConnection.getInputStream());
                    JSONObject json = new JSONObject(resultado);
                    String hasLoggedIn = json.getString("hasLoggedIn");
                    // El valor devuelto es OK, luego el usuario puede entrar a la aplicación
                    if (hasLoggedIn.equalsIgnoreCase("ok")) {
                        return true;
                    }
                }
                return false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                // Como nuestra REST API no está operativa, se generará una ConnectException
                // Devolvemos TRUE para que el Login se complete
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            loginTask = null;
            mostrarCarga(false);

            if (success) {
                SharedPreferencesHelper.updateSharedPreference(getApplicationContext(), Constants.USUARIO_LOGUEADO, true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            } else {
                LoginActivity.this.password.setError(getString(R.string.error_login));
                LoginActivity.this.password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            loginTask = null;
            mostrarCarga(false);
        }

        /* Convierte un InputStream (de la Response recibida por la API REST) a String, de modo que
           obtener el tratamiento JSON es mucho más sencillo */
        private String getStringFromInputStream(InputStream is) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
    }
}
