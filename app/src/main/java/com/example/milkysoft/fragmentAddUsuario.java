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
import android.widget.Toast;


import com.example.milkysoft.Modelo.Usuario;
import com.example.milkysoft.databinding.FragmentAddProductBinding;
import com.example.milkysoft.databinding.FragmentAddUsuarioBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
            txtCallesUsuario,txtFechanacimiento,txtFechaRegistro;
    //private TextView txtContenidotext;
    private Button btnGuardarUsuario,btnUpdateimgUsuario,btnDeleteimgUsuario;

    private Spinner spCategoria,spPresentacion,spAzucar,spSal,spGrasa,
            spPeso;
    FragmentManager fragmentManager;
    String id_usuario=null;
    int positionSpinner=0;
    FirebaseFirestore mfirestore;
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
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","AddUsuario");
        editor.commit();
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        if(getArguments()!=null){
            id_usuario=getArguments().getString("id_usuario");
            if(id_usuario!=null||id_usuario!="") {
                getUsuario();
            }
        }

        iniciarControles();

        imgUsuario.setImageResource(R.drawable.img);
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
                    postUsuarios(usuarioToPost);
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
                                                .resize(150, 150)
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

    private void obtenerValores(){
        String nombreUsuario,codigoUsuario,apellidoUsuario,cedulaUsuario,
                telefonoUsuario,correoUsuario,provinciaUsuario
                ,cantonUsuario,callesUsuario,fechaNacimiento,
                fechaRegistro,photoUsuario;
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
       usuarioToPost=new Usuario(codigoUsuario,nombreUsuario,apellidoUsuario,cedulaUsuario
       ,telefonoUsuario,correoUsuario,provinciaUsuario,cantonUsuario,callesUsuario,
               fechaNacimiento,fechaRegistro);

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
        //uploadPhoto();
        mfirestore.collection("Usuarios").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
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
                try {
                    if(!imgUsuarioResource.equals("")){
                        Toast toast=Toast.makeText(getContext(),"Cargando imagen...",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.get()
                                .load(imgUsuarioResource)
                                .resize(150,150)
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
        txtCorreoUsuario=root.findViewById(R.id.txtCorreoUsuario);
        txtProvinciaUsuario=root.findViewById(R.id.txtProvinciaUsuario);
        txtCantonUsuario=root.findViewById(R.id.txtCantonUsuario);
        txtCallesUsuario=root.findViewById(R.id.txtCallesUsuario);
        txtFechanacimiento=root.findViewById(R.id.txtFechaNacimiento);
        txtFechaRegistro=root.findViewById(R.id.txtFechaRegistro);
        btnDeleteimgUsuario=root.findViewById(R.id.btnDeleteimgUsuario);
        btnUpdateimgUsuario=root.findViewById(R.id.btnUpdateimgUsuario);
        btnGuardarUsuario=root.findViewById(R.id.btnGuardarUsuario);
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
}