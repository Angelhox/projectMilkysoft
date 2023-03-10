package com.example.milkysoft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.milkysoft.Modelo.Clientes;
import com.example.milkysoft.Modelo.Usuario;
import com.example.milkysoft.databinding.FragmentCrearCuentaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class fragmentCrearCuentaUsuario extends DialogFragment {

    private EditText etCorreo,etContraseña;
    private Button btnRegistrar,btnRegresar;
    AwesomeValidation awesomeValidation;
    FragmentCrearCuentaBinding binding;
    View root;
    String creando;
    FirebaseAuth firebaseAuth;
    private Usuario clienteToPost=null;
    FirebaseFirestore mfirestore;

    FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCrearCuentaBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        firebaseAuth=FirebaseAuth.getInstance();
        mfirestore= FirebaseFirestore.getInstance();
        iniciarControles();
        SharedPreferences preferencias = getActivity().getSharedPreferences("preferenciasCrearCuenta",0);
        creando=preferencias.getString("creando","creandoUsuario");
        Toast.makeText(getContext(),creando,Toast.LENGTH_LONG).show();
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(etCorreo, Patterns.EMAIL_ADDRESS, "Ingresa un mail valido");
        awesomeValidation.addValidation(etContraseña,".{6,}", "Ingresa una contraseña de 6 digitos como mínimo");
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=etCorreo.getText().toString();
                String contraseña=etContraseña.getText().toString();
                if(awesomeValidation.validate()) {

                        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                   // clienteToPost = new Clientes(user.getEmail());
                                    postUsuarios(clienteToPost);
                                   /* SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasCuenta", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("correoUsuario", correo);
                                    editor.putString("contraseñaUsuario", contraseña);
                                    editor.putString("id_usuario", user.getUid());
                                    editor.commit();
                                Intent i= new Intent(getContext(),Content.class);
                                i.putExtra("estadoRegistro","sinInfo");
                                i.putExtra("toShow","modificarCliente");
                                i.putExtra("id_cliente",user.getUid());*/
                                    startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
                                    dismiss();
                                } else {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    dameToastdeerror(errorCode);
                                }
                            }
                        });

                }else{
                    Toast.makeText(getContext(), "Completa todos los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
/*        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
            }
        });*/
        return root;

    }
    private void postUsuarios(Usuario usuario){
        mfirestore= FirebaseFirestore.getInstance();

        Map<String,Object> map=new HashMap<>();
        map.put("codigoUsuario",usuario.getCodigoUsuario());
        map.put("nombreUsuario",usuario.getNombreUsuario());
        map.put("apellidoUsuario",usuario.getApellidoUsuario());
        map.put("cedulaUsuario",usuario.getCedulaUsuario());
        map.put("telefonoUsuario",usuario.getTelefonoUsuario());
        map.put("correoUsuario",usuario.getCorreoUsuario());
        map.put("provinciaUsuario",usuario.getProvinciaUsuario());
        map.put("cantonUsuario",usuario.getCantonUsuario());
        map.put("callesUsuario",usuario.getCallesUsuario());
        map.put("fechaNacimiento",usuario.getFechaNacimiento());
        map.put("fechaRegistro",usuario.getFechaRegistro());
        map.put("cargoUsuario",usuario.getCargoUsuario());
        map.put("contraseniaUsuario",usuario.getContraseniaUsuario());
        String idUsuario=user.getUid();
        //uploadPhoto();
        mfirestore.collection("Usuarios").document(idUsuario).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Has registrado a "+usuario.getCorreoUsuario(),Toast.LENGTH_LONG).show();
              //  dismis();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void iniciarControles(){
        btnRegistrar=root.findViewById(R.id.btnCrearCuentafr);
        //btnRegresar=root.findViewById(R.id.btnRegresar);
        etCorreo=root.findViewById(R.id.etCrearCorreo);
        etContraseña=root.findViewById(R.id.etCrearContraseña);
    }
    private void dameToastdeerror(String error){
        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(getContext(), "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(getContext(), "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(getContext(), "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getContext(), "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                etCorreo.setError("La dirección de correo electrónico está mal formateada.");
                etCorreo.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getContext(), "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                etContraseña.setError("la contraseña es incorrecta ");
                etContraseña.requestFocus();
                etContraseña.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(getContext(), "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(getContext(),"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(getContext(), "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(getContext(), "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                etCorreo.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                etCorreo.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(getContext(), "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(getContext(), "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(getContext(), "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getContext(), "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(getContext(), "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(getContext(), "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(getContext(), "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                etContraseña.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                etContraseña.requestFocus();
                break;

        }

    }
}