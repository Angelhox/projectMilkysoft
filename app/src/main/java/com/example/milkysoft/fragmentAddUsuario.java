package com.example.milkysoft;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.milkysoft.Modelo.Clientes;
import com.example.milkysoft.Modelo.Usuario;
import com.example.milkysoft.databinding.FragmentAddProductBinding;
import com.example.milkysoft.databinding.FragmentAddUsuarioBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class fragmentAddUsuario extends Fragment {

    private FragmentAddUsuarioBinding binding;

    private View root;
    private ImageView imgUsuario;
    private EditText txtNombreUsuario,txtCodigoUsuario,
            txtApellidoUsuario,txtCedulaUsuario,txtTelefonoUsuario,
            txtCorreoUsuario,txtProvinciaUsuario,txtCantonUsuario,
            txtCallesUsuario,txtFechanacimiento,txtFechaRegistro,txtContraseniaUsaurio,txtCargoUsuario;

    private Button btnGuardarUsuario,btnUpdateimgUsuario,btnDeleteimgUsuario,btnCrearCuenta;

    private Spinner spCategoria,spPresentacion,spAzucar,spSal,spGrasa,
            spPeso;
    FragmentManager fragmentManager;
    String id_usuario=null,idObtenido,contraseniaObtenida,correoObtenido;
    int positionSpinner=0;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore mfirestore;
    FirebaseUser user;
    private Usuario usuarioToPost=null;
    private static final int COD_SEL_STORAGE=200;
    private static final int COD_SEL_IMAGE=300;
    StorageReference storageReference;
    String storagePath="imgUsuarios/*";
    private Uri img_url;
    String photo="photo";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private int dia,mes,anio;
    boolean create=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUsuarioBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        firebaseAuth=FirebaseAuth.getInstance();
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","AddUsuario");
        editor.commit();
        iniciarControles();
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(txtCorreoUsuario, Patterns.EMAIL_ADDRESS, "Ingresa un mail valido");
        awesomeValidation.addValidation(txtContraseniaUsaurio,".{6,}", "Ingresa una contraseña de 6 digitos como mínimo");
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        if(getArguments()!=null){
            id_usuario=getArguments().getString("id_usuario");
            if(id_usuario!=null||id_usuario!="") {
                getUsuario();
            }
        }

        imgUsuario.setImageResource(R.drawable.img);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCrearCuenta fm=new fragmentCrearCuenta();
                fm.show(getActivity().getSupportFragmentManager(),"Navegar a Fragment");
            }
        });
        btnUpdateimgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btnGuardarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_usuario==null||id_usuario.equals("")) {
                        obtenerValores();
                       crearCuenta();
                }else{
                    obtenerValores();
                    updateUsuarios(usuarioToPost);
                }
            }
        });
        txtFechanacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFecha(txtFechanacimiento);
            }
        });
        txtFechaRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFecha(txtFechaRegistro);
            }
        });
        return root;

    }
    private void uploadPhoto() {
        Intent i =new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,COD_SEL_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==COD_SEL_IMAGE){
                img_url=data.getData();
                subirPhoto(img_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void subirPhoto(Uri img_url) {
        //progressDialog.setMessage("Actualizando foto");
        // progressDialog.show();
        if(id_usuario==null|| id_usuario=="") {
            Toast.makeText(getContext(),"Crea primero un usuario",
                    Toast.LENGTH_LONG).show();
        }else {
            String rute_storage_photo = storagePath + "+photo" + mAuth.getUid() + "" + id_usuario;
            StorageReference reference = storageReference.child(rute_storage_photo);
            reference.putFile(img_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    if (uriTask.isSuccessful()) {
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String download_uri = uri.toString();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("photoUsuario", download_uri);
                                mfirestore.collection("Usuarios").document(id_usuario).update(map);
                                Toast.makeText(getContext(), "FotoActualizada", Toast.LENGTH_LONG).show();
                                //progressDialog.dismiss();
                                try {
                                    if (!download_uri.equals("")) {
                                        Picasso.get()
                                                .load(download_uri)
                                                .resize(190, 150)
                                                .into(imgUsuario);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Error al cargar la imagen Error: " + e, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    private void crearCuenta(){
        String correo=txtCorreoUsuario.getText().toString();
        String contraseña=txtContraseniaUsaurio.getText().toString();
        if(awesomeValidation.validate()) {

            firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();
                        user=FirebaseAuth.getInstance().getCurrentUser();
                        postUsuarios(usuarioToPost);

                    } else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        dameToastdeerror(errorCode);

                    }
                }
            });


        }else{
            Toast.makeText(getContext(), "Completa todos los datos", Toast.LENGTH_LONG).show();
            create =false;
        }
    }
    private void obtenerValores(){

        String nombreUsuario,codigoUsuario,apellidoUsuario,cedulaUsuario,
                telefonoUsuario,correoUsuario,provinciaUsuario
                ,cantonUsuario,callesUsuario,fechaNacimiento,
                fechaRegistro,photoUsuario,cargoUsuario,contraseniaUsuario;
        nombreUsuario=txtNombreUsuario.getText().toString();
        codigoUsuario=txtCodigoUsuario.getText().toString();
        apellidoUsuario=txtApellidoUsuario.getText().toString();
        cedulaUsuario=txtCedulaUsuario.getText().toString();
        telefonoUsuario=txtTelefonoUsuario.getText().toString();
        //cantidad=txtCantidad.getText().toString();
        correoUsuario=txtCorreoUsuario.getText().toString();
        provinciaUsuario=txtProvinciaUsuario.getText().toString();
        cantonUsuario=txtCantonUsuario.getText().toString();
        callesUsuario=txtCallesUsuario.getText().toString();
        fechaNacimiento=txtFechanacimiento.getText().toString();
        fechaRegistro=txtFechaRegistro.getText().toString();
        cargoUsuario=txtCargoUsuario.getText().toString();
        contraseniaUsuario=txtContraseniaUsaurio.getText().toString();
        if(correoUsuario==""||correoUsuario==null){
            Toast.makeText(getContext(),"Debes crear una cuenta con un Correo",Toast.LENGTH_LONG).show();
            create=false;
        }else {
            create=true;
            usuarioToPost = new Usuario("",codigoUsuario,nombreUsuario,
                    apellidoUsuario,cedulaUsuario,telefonoUsuario,correoUsuario,
                    provinciaUsuario,cantonUsuario,callesUsuario,fechaNacimiento,
                    fechaRegistro,contraseniaUsuario,cargoUsuario);

        }
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
                back();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateUsuarios(Usuario usuario){
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
        //uploadPhoto();
        mfirestore.collection("Usuarios").document(id_usuario).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Has actualizado a "+
                        usuario.getCorreoUsuario(),Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void getUsuario(){
        mfirestore=FirebaseFirestore.getInstance();
        mfirestore.collection("Usuarios").document(id_usuario).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreUsuario=documentSnapshot.getString("nombreUsuario");
                String codigoUsuario=documentSnapshot.getString("codigoUsuario");
                String apellidoUsuario=documentSnapshot.getString("apellidoUsuario");
                String cedulaUsuario=documentSnapshot.getString("cedulaUsuario");
                String telefonoUsuario=documentSnapshot.getString("telefonoUsuario");
                String correoUsuario=documentSnapshot.getString("correoUsuario");
                String provinciaUsuario=documentSnapshot.getString("provinciaUsuario");
                //checkSpinner(spCategoria,categoria);
                String cantonUsuario=documentSnapshot.getString("cantonUsuario");
                String callesUsuario=documentSnapshot.getString("callesUsuario");
                String fechaNacimiento=documentSnapshot.getString("fechaNacimiento");
                String fechaRegistro=documentSnapshot.getString("fechaRegistro");
                String cargoUsuario=documentSnapshot.getString("cargoUsuario");
                String contraseniaUsuario=documentSnapshot.getString("contraseniaUsuario");
                String imgUsuarioResource=documentSnapshot.getString("photoUsuario");

                txtNombreUsuario.setText(nombreUsuario);
                txtCodigoUsuario.setText(codigoUsuario);
                txtApellidoUsuario.setText(apellidoUsuario);
                txtCedulaUsuario.setText(cedulaUsuario);
                txtTelefonoUsuario.setText(telefonoUsuario);
                txtCorreoUsuario.setText(correoUsuario);
                txtProvinciaUsuario.setText(provinciaUsuario);
                txtCantonUsuario.setText(cantonUsuario);
                txtCallesUsuario.setText(callesUsuario);
                txtFechanacimiento.setText(fechaNacimiento);
                txtFechaRegistro.setText(fechaRegistro);
                txtCargoUsuario.setText(cargoUsuario);
                txtContraseniaUsaurio.setText(contraseniaUsuario);
                try {
                    if(!imgUsuarioResource.equals("")){
                        Toast toast=Toast.makeText(getContext(),"Cargando imagen...",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.get()
                                .load(imgUsuarioResource)
                                .resize(150,10)
                                .into(imgUsuario);
                    }else{
                        Toast.makeText(getContext(),"No encontramos una imagen",Toast.LENGTH_LONG).show();
                        imgUsuario.setImageResource(R.drawable.img);
                    }
                }catch(Exception e){
                    // Toast.makeText(getContext(),"Error al cargar la imagen Error: " +e,Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"No encontramos una imagen: ",Toast.LENGTH_LONG).show();
                    imgUsuario.setImageResource(R.drawable.img);
                }
                Toast.makeText(getContext(),"Mis datos",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al Cargar los datos",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getFecha(EditText txtSalida){
        final Calendar calendario = java.util.Calendar.getInstance();
        dia = calendario.get(java.util.Calendar.DAY_OF_MONTH);
        mes = calendario.get(java.util.Calendar.MONTH);
        anio = calendario.get(java.util.Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String diaFormateado, mesFormateado;
                //Obtener el dia
                if (dayOfMonth < 10) {
                    diaFormateado = "0" + String.valueOf(dayOfMonth);
                } else {
                    diaFormateado = String.valueOf(dayOfMonth);
                }
                //Obtener el mes
                int mes = month + 1;
                if (mes < 10) {
                    mesFormateado = "0" + String.valueOf(mes);
                } else {
                    mesFormateado = String.valueOf(mes);
                }
                //Setear Fecha en Textview
                txtSalida.setText(year + "/" + diaFormateado + "/" + mesFormateado);
            }
        }, anio, mes, dia);
        datePickerDialog.show();

    }



    private void checkSpinner(Spinner spnCheck,String item){
        for(int i=0;i<spnCheck.getAdapter().getCount();i++){
            try{ if(spnCheck.getAdapter().getItem(i).equals(item)){
                positionSpinner=i;
                spnCheck.setSelection(i);
            }}
            catch(Exception e){
                Toast.makeText(getContext(),"Error: "+e,Toast.LENGTH_LONG).show();
            }

        }

    }

    private void iniciarControles(){
        txtNombreUsuario=root.findViewById(R.id.txtNombreUsuario);
        txtCodigoUsuario=root.findViewById(R.id.txtCodigoUsuario);
        txtApellidoUsuario=root.findViewById(R.id.txtApellidoUsuario);
        //spPeso=root.findViewById(R.id.spPesoProducto);
        txtCedulaUsuario=root.findViewById(R.id.txtCedulaUsuario);
        txtTelefonoUsuario=root.findViewById(R.id.txtTelefonoUsuario);
        txtCorreoUsuario=root.findViewById(R.id.txtCorreoUsuarioadd);
        txtProvinciaUsuario=root.findViewById(R.id.txtProvinciaUsuario);
        txtCantonUsuario=root.findViewById(R.id.txtCantonUsuario);
        txtCallesUsuario=root.findViewById(R.id.txtCallesUsuario);
        txtFechanacimiento=root.findViewById(R.id.txtFechaNacimiento);
        txtFechaRegistro=root.findViewById(R.id.txtFechaRegistro);
        txtCargoUsuario=root.findViewById(R.id.txtCargoUsuario);
        txtContraseniaUsaurio=root.findViewById(R.id.txtContraseniaUsuario);
        btnDeleteimgUsuario=root.findViewById(R.id.btnDeleteimgUsuario);
        btnUpdateimgUsuario=root.findViewById(R.id.btnUpdateimgUsuario);
        btnGuardarUsuario=root.findViewById(R.id.btnGuardarUsuario);
        btnCrearCuenta=root.findViewById(R.id.btnCrearCuentafrus);
        imgUsuario=root.findViewById(R.id.imgAddUsuario);
    }
    private void setAdapterSpinner(){

        String []opcionesPeso={"Gramos","Kilogramos","Litros","Galones"};
        ArrayAdapter<String> adapterPeso = new
                ArrayAdapter<String>(getContext(),R.layout.layout_spinner,
                opcionesPeso);
        spPeso.setAdapter(adapterPeso);
        String []opcionesCategoria={"Queso","Yogurth","Manjar","Leche"};
        ArrayAdapter<String> adapterCategoria = new
                ArrayAdapter<String>(getContext(),R.layout.layout_spinner,
                opcionesCategoria);
        spCategoria.setAdapter(adapterCategoria);
        String []opcionesPresentacion={"Bottela","Funda","Tetrapack","Tarrina"};
        ArrayAdapter<String> adapterPresentacion = new
                ArrayAdapter<String>(getContext(),R.layout.layout_spinner,
                opcionesPresentacion);
        spPresentacion.setAdapter(adapterPresentacion);
        String []opcionesSemaforo={"Alto","Medio","Bajo"};
        ArrayAdapter<String> adapterSemaforo = new
                ArrayAdapter<String>(getContext(),R.layout.layout_spinner,
                opcionesSemaforo);
        spAzucar.setAdapter(adapterSemaforo);
        spSal.setAdapter(adapterSemaforo);
        spGrasa.setAdapter(adapterSemaforo);
    }


    private void back(){

        Intent i = new Intent(getContext(),listaUsuariosact.class);
        startActivity(i);

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
                txtContraseniaUsaurio.setError("La dirección de correo electrónico está mal formateada.");
                txtContraseniaUsaurio.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getContext(), "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                txtContraseniaUsaurio.setError("la contraseña es incorrecta ");
                txtContraseniaUsaurio.requestFocus();
                txtContraseniaUsaurio.setText("");
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
                txtCorreoUsuario.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                txtCorreoUsuario.requestFocus();
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
                txtContraseniaUsaurio.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                txtContraseniaUsaurio.requestFocus();
                break;

        }

    }
}