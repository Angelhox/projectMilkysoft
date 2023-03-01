package com.example.milkysoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.milkysoft.Modelo.Clientes;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btnLoginCorreo,btnCrearCuenta,btnLoginGoogle,btnInvitado;
    private EditText etCorreo,etContraseña;
    private TextView mtvRespuesta;
    String perfil=null;
    String estadoRegistro=null;
    String id_cliente;
    String id_clientelogeado;
    boolean logeados;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore mfirestore;
    FirebaseUser userlogeado;
    String mode;
    AwesomeValidation awesomeValidation;
    Clientes clienteToPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mfirestore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // onStart();



        firebaseAuth= FirebaseAuth.getInstance();
        userlogeado=firebaseAuth.getCurrentUser();

        //Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_LONG).show();
        iniciarControles();
       /* SharedPreferences preferences =getSharedPreferences("preferenciasLogin",0);
        mode=preferences.getString("mode","invitado");
        logeados=preferences.getBoolean("logeado",false);
        id_clientelogeado=preferences.getString("id_cliente", "");*/

        SharedPreferences preferencias = getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","MainActivity");
        editor.commit();
        perfil=preferencias.getString("Here","MainActivity");
        Toast.makeText(getApplicationContext(),perfil,Toast.LENGTH_LONG).show();

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.etCorreo, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.etpassword,".{6,}",R.string.invalid_password);
        firebaseAuth= FirebaseAuth.getInstance();
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnLoginCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    String mail=etCorreo.getText().toString();
                    String contraseña=etContraseña.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(mail,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                id_cliente=user.getUid();
                                //getUsuario();
                                irHome(user);
                            }else{
                                String errorCode=((FirebaseAuthException)task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                }

            }
        });
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("preferenciasCrearCuenta", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("creando", "creandoCliente");
                editor.commit();
                fragmentCrearCuenta fm=new fragmentCrearCuenta();
                fm.show(getSupportFragmentManager(),"Navegar a Fragment");
            }
        });
        btnInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Content.class));
            }
        });


       /* if (logeados==true) {
            id_cliente = id_clientelogeado;

            startActivity(new Intent(getApplicationContext(), Content.class));
        } else if (!logeados == false || !logeados) {
            Toast.makeText(getApplicationContext(),"Inicia sesion o Crea una cuenta",Toast.LENGTH_LONG).show();
        }*/

    }
    //---->Inicio con gmail

    public void onStart() {

        /* Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseUser user=firebaseAuth.getCurrentUser();*/
        firebaseAuth= FirebaseAuth.getInstance();
        userlogeado=firebaseAuth.getCurrentUser();
        SharedPreferences preferences =getSharedPreferences("preferenciasLogin",0);
        mode=preferences.getString("mode","invitado");
        logeados=preferences.getBoolean("logeado",false);
        //id_clientelogeado=preferences.getString("id_cliente", "");
        if(logeados==true){
            irHome(userlogeado);
        }else{
            Toast.makeText(getApplicationContext(),"Heres",Toast.LENGTH_LONG).show();
        }


        super.onStart();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                mtvRespuesta.setText(e.getMessage());
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                           if(user!=null) {
                               id_cliente = user.getUid();
                               if(id_cliente!=""){
                                   clienteToPost = new Clientes(user.getEmail());
                                   postClientes(clienteToPost,user);
                                   irHome(user);
                               }
                           }

                            clienteToPost = new Clientes(user.getEmail());
                            postClientes(clienteToPost,user);
                            irHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mtvRespuesta.setText(task.getException().toString());
                            irHome(null);
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*private void updateUI(FirebaseUser user) {
    if(user!=null){

       irHome();
    }
    }*/





    //<----Inicio con gmail

    @Override
    public void onBackPressed() {

        if(perfil.equals("MainActivity")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Desea Volver al menu de Inicio").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),Content.class));
                }
            });
            builder.show();
        }
        super.onBackPressed();
    }
    private void iniciarControles(){
    btnLoginCorreo=findViewById(R.id.btnIniciarCorreo);
    btnCrearCuenta=findViewById(R.id.btnGooglemain);
    btnCrearCuenta=findViewById(R.id.btnCrearCuenta);
    etCorreo=findViewById(R.id.etCorreo);
    etContraseña=findViewById(R.id.etpassword);
    btnLoginGoogle=findViewById(R.id.btnGooglemain);
    mtvRespuesta=findViewById(R.id.tvRespuestamain);
    btnInvitado=findViewById(R.id.btnInvitado);

}

   // @Override
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Desea Volver al menu de Inicio").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    //@Override
    private void irHome(FirebaseUser user) {
        String estadoRegistrov = null;
        id_cliente=user.getUid();
        if (user != null) {
            //user1.setUserName(user.getEmail());
            try {
                mfirestore.collection("Clientes").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                       // String correo = documentSnapshot.getString("correoCliente");
                        //if (correo != null || correo!= ""||correo!="null") {
                            if (documentSnapshot.exists()) {
                                String estadoRegistrov = documentSnapshot.getString("estadoRegistro");

                                String nombreUsuario=documentSnapshot.getString("nombreCliente")+" "
                                        +documentSnapshot.getString("apellidoCliente");
                                String cedulaUsuario=documentSnapshot.getString("cedulaFacturacion");
                                String correoUsuario=documentSnapshot.getString("correoCliente");
                                String telefonoUsuario=documentSnapshot.getString("telefonoCliente");
                                String direccionUsuario=documentSnapshot.getString("direccionFacturacion");
                                if(telefonoUsuario==""||telefonoUsuario==null){
                                    telefonoUsuario=documentSnapshot.getString("celularCliente");
                                }

                                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("mode", "cliente");
                            editor.putString("id_cliente", id_cliente);
                            editor.putBoolean("logeado", true);
                            editor.putString("nombreUsuario",nombreUsuario);
                            editor.putString("correoUsuario",correoUsuario);
                            editor.putString("cedulaUsuario",cedulaUsuario);
                            editor.putString("telefonoUsuario",telefonoUsuario);
                            editor.putString("direccionUsuario",direccionUsuario);
                            editor.commit();

                            if (estadoRegistrov.equalsIgnoreCase("sinInfo")) {
                                Intent i = new Intent(getApplicationContext(), Content.class);
                                i.putExtra("id_usuario",id_cliente);
                                i.putExtra("toShow", "modificarCliente");
                                i.putExtra("acceso", "cliente");

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), Content.class);
                                i.putExtra("id_usuario",id_cliente);
                                i.putExtra("toShow", "menuInicio");
                                i.putExtra("acceso", "cliente");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        } else {
                            mfirestore.collection("Usuarios").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {


                                    if (documentSnapshot.exists()) {
                                        String estadoRegistrov = documentSnapshot.getString("estadoRegistro");
                                        String cargoUsuario = documentSnapshot.getString("cargoUsuario");
                                        String nombreUsuario=documentSnapshot.getString("nombreUsuario")+" "
                                                +documentSnapshot.getString("apellidoUsuario");
                                        String cedulaUsuario=documentSnapshot.getString("cedulaUsuario");
                                        String correoUsuario=documentSnapshot.getString("correoUsuario");
                                        String telefonoUsuario=documentSnapshot.getString("telefonoUsuario");
                                        String provinciaUsuario=documentSnapshot.getString("provinciaUsuario");
                                        String cantonUsuario=documentSnapshot.getString("cantonUsuario");
                                        String callesUsuario=documentSnapshot.getString("callesUsuario");
                                        String direccionUsuario=cantonUsuario+", "+callesUsuario;
                                        if(telefonoUsuario.isEmpty()||telefonoUsuario==""||telefonoUsuario==null){
                                            telefonoUsuario="0999999999";
                                        }
                                        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("mode", "admin");
                                        editor.putString("id_cliente", id_cliente);
                                        editor.putBoolean("logeado", true);
                                        editor.putString("nombreUsuario",nombreUsuario);
                                        editor.putString("correoUsuario",correoUsuario);
                                        editor.putString("cedulaUsuario",cedulaUsuario);
                                        editor.putString("telefonoUsuario",telefonoUsuario);
                                        editor.putString("direccionUsuario",direccionUsuario);
                                        editor.commit();
                                        Intent i = new Intent(getApplicationContext(),Content.class);
                                        i.putExtra("toShow", "menuInicio");
                                        i.putExtra("acceso", cargoUsuario);

                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);


                                        Toast.makeText(getApplicationContext(), "" + estadoRegistrov, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "Mis datos", Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(getApplicationContext(),"No tienes una cuenta",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Toast.makeText(getApplicationContext(), "" + estadoRegistrov, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Mis datos", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
                    }
                });

            }catch (Exception e){

            }
        }
    }
    private void getUsuario() {

        mfirestore.collection("Clientes").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               String estadoRegistro = documentSnapshot.getString("estadoRegistro");
                Toast.makeText(getApplicationContext(),""+estadoRegistro,Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(), "Mis datos", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void postClientes(Clientes cliente,FirebaseUser user){
        String id_clientetopost=user.getUid();
        mfirestore.collection("Usuarios").document(id_clientetopost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (!documentSnapshot.exists()) {
        mfirestore.collection("Clientes").document(id_clientetopost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                // String correo = documentSnapshot.getString("correoCliente");
                //if (correo != null || correo!= ""||correo!="null") {
                if (!documentSnapshot.exists()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigoCliente", cliente.getCodigoCliente());
                    map.put("nombreCliente", cliente.getNombreCliente());
                    map.put("apellidoCliente", cliente.getApellidoCliente());
                    map.put("telefonoCliente", cliente.getTelefonoCliente());
                    map.put("celularCliente", cliente.getCelularCliente());
                    map.put("correoCliente", cliente.getCorreoCliente());
                    map.put("photoCliente", cliente.getPhotoCliente());
                    map.put("cedulaFacturacion", cliente.getCedulaFacturacion());
                    map.put("rucFacturacion", cliente.getRucFacturacion());
                    map.put("direccionFacturacion", cliente.getDireccionFacturacion());
                    map.put("telefonoFacturacion", cliente.getTelefonoFacturacion());
                    map.put("ubicacionEntrega", cliente.getUbicacionEntrega());
                    map.put("latitud", cliente.getLatitud());
                    map.put("longitud", cliente.getLongitud());
                    map.put("estadoRegistro", "sinInfo");
                    String idDocument = user.getUid();
                    //uploadPhoto();
                    mfirestore.collection("Clientes").document(idDocument).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "New here" +
                                    cliente.getCorreoCliente(), Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }} });

                }}});}





    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(MainActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(MainActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(MainActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(MainActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                etCorreo.setError("La dirección de correo electrónico está mal formateada.");
                etCorreo.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(MainActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                etContraseña.setError("la contraseña es incorrecta ");
                etContraseña.requestFocus();
                etContraseña.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(MainActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(MainActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(MainActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(MainActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                etCorreo.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                etCorreo.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(MainActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(MainActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(MainActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(MainActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(MainActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente:).", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(MainActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(MainActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                etContraseña.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                etContraseña.requestFocus();
                break;

        }

    }


}