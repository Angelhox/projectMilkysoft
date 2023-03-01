package com.example.milkysoft;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.KeyEvent;
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

import com.example.milkysoft.Modelo.Clientes;
import com.example.milkysoft.Modelo.Usuario;
import com.example.milkysoft.databinding.FragmentAddClientesBinding;
import com.example.milkysoft.databinding.FragmentAddUsuarioBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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


public class fragmentAddClientes extends Fragment {
    private FragmentAddClientesBinding binding;

    private View root;
    private ImageView imgCliente;
    private TextView txtCorreoCliente;
    private EditText txtNombreCliente,txtCodigoCliente,
            txtApellidoCliente,txtTelefonoCliente,
            txtCelularCliente,txtCedulaFacturacion,
            txtRucFacturacion,txtDireccionFacturacion,txtTelefonoFacturacion,
            txtUbicacion,txtLatitud,txtLongitud;
    private Button btnGuardarCliente,btnUpdateimgCliente,btnDeleteimgCliente;

    private Spinner spCategoria,spPresentacion,spAzucar,spSal,spGrasa,
            spPeso;
    FragmentManager fragmentManager;
    String id_cliente=null;
    int positionSpinner=0;
    FirebaseFirestore mfirestore;
    private Clientes clienteToPost=null;
    private static final int COD_SEL_STORAGE=200;
    private static final int COD_SEL_IMAGE=300;
    int numerocliente= (int)(Math.random() * (10000+1000)-1) + 1000;
    StorageReference storageReference;
    String storagePath="imgClientes/*";
    private Uri img_url;
    String photo="photo";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser user;
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
        binding = FragmentAddClientesBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","AddCliente");
        editor.commit();
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        if(getArguments()!=null){
            id_cliente=getArguments().getString("id_cliente");
            if(id_cliente!=null||id_cliente!="") {
                        getCliente();
            }else{
                id_cliente=getArguments().getString("id_usuario");
                if(id_cliente!=null||id_cliente!="") {
                    getCliente();}
            }
        }

        iniciarControles();

        imgCliente.setImageResource(R.drawable.img);
        txtCodigoCliente.setText("cliente"+String.valueOf(numerocliente));
        txtTelefonoCliente.
                setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        txtTelefonoFacturacion.setText(txtTelefonoCliente.getText().toString());
                        return true;
                    }
                });
        btnUpdateimgCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_cliente==null||id_cliente.equals("")) {
                    obtenerValores();
                    postClientes(clienteToPost);
                }else{
                    obtenerValores();
                    updateClientes(clienteToPost);
                }
            }
        });
        /*Calendario se habre con un click
        txtFechanacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFecha(txtFechanacimiento);
            }
        });*/

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
        if(id_cliente==null|| id_cliente=="") {
            Toast.makeText(getContext(),"Crea primero un cliente",
                    Toast.LENGTH_LONG).show();
        }else {
            String rute_storage_photo = storagePath + "+photo" + mAuth.getUid() + "" + id_cliente;
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
                                map.put("photoCliente", download_uri);
                                mfirestore.collection("Clientes").document(id_cliente).update(map);
                                Toast.makeText(getContext(), "FotoActualizada", Toast.LENGTH_LONG).show();
                                //progressDialog.dismiss();
                                try {
                                    if (!download_uri.equals("")) {
                                        /*Picasso.get()
                                                .load(download_uri)
                                                .resize(150, 150)
                                                .into(imgCliente);*/

                                        Picasso.get()
                                                .load(download_uri).fit().centerCrop()
                                                /* .placeholder()
                                                 .resize()*/
                                                .into(imgCliente);

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
        String nombreCliente,codigoCliente,apellidoCliente,celularCliente,
                telefonoCliente,correoCliente
                //Facturacion
                ,cedulaFacturacion,rucFacturacion,direccionFacturacion,
                telefonoFacturacion
                //Entregas
                ,ubicacion,latitud,longitud;

        nombreCliente=txtNombreCliente.getText().toString();
        codigoCliente=txtCodigoCliente.getText().toString();
        apellidoCliente=txtApellidoCliente.getText().toString();
        celularCliente=txtCelularCliente.getText().toString();
        telefonoCliente=txtTelefonoCliente.getText().toString();
        correoCliente=txtCorreoCliente.getText().toString();
        //Facturacion
        cedulaFacturacion=txtCedulaFacturacion.getText().toString();
        rucFacturacion=txtRucFacturacion.getText().toString();
        direccionFacturacion=txtDireccionFacturacion.getText().toString();
        telefonoFacturacion=txtTelefonoFacturacion.getText().toString();
        //Ubicacion
        ubicacion=txtUbicacion.getText().toString();
        latitud=txtLatitud.getText().toString();
        longitud=txtLongitud.getText().toString();

        clienteToPost=new Clientes(codigoCliente,nombreCliente,
                apellidoCliente,telefonoCliente,celularCliente,
                correoCliente,null,cedulaFacturacion,rucFacturacion,
                direccionFacturacion,telefonoFacturacion,ubicacion,latitud,longitud);

    }
    private void postClientes(Clientes cliente){
        mfirestore= FirebaseFirestore.getInstance();
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mode", "cliente");
        editor.putString("id_cliente", id_cliente);
        editor.putBoolean("logeado", true);
        editor.putString("nombreUsuario",cliente.getNombreCliente()+" "+cliente.getApellidoCliente());
        editor.putString("correoUsuario",cliente.getCorreoCliente());
        editor.putString("cedulaUsuario",cliente.getCedulaFacturacion());
        editor.putString("telefonoUsuario",cliente.getTelefonoFacturacion());
        editor.putString("direccionUsuario",cliente.getDireccionFacturacion());
        editor.commit();

        Map<String,Object> map=new HashMap<>();
        map.put("codigoCliente",cliente.getCodigoCliente());
        map.put("nombreCliente",cliente.getNombreCliente());
        map.put("apellidoCliente",cliente.getApellidoCliente());
        map.put("telefonoCliente",cliente.getTelefonoCliente());
        map.put("celularCliente",cliente.getCelularCliente());
        map.put("correoCliente",cliente.getCorreoCliente());
        map.put("photoCliente",cliente.getPhotoCliente());
        map.put("cedulaFacturacion",cliente.getCedulaFacturacion());
        map.put("rucFacturacion",cliente.getRucFacturacion());
        map.put("direccionFacturacion",cliente.getDireccionFacturacion());
        map.put("telefonoFacturacion",cliente.getTelefonoFacturacion());
        map.put("ubicacionEntrega",cliente.getUbicacionEntrega());
        map.put("latitud",cliente.getLatitud());
        map.put("longitud",cliente.getLongitud());
        //uploadPhoto();
        mfirestore.collection("Clientes").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Has registrado a "+cliente.getCorreoCliente(),Toast.LENGTH_LONG).show();
                SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);

                back();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    private void updateClientes(Clientes cliente){
        mfirestore= FirebaseFirestore.getInstance();
        Map<String,Object> map=new HashMap<>();
        map.put("codigoCliente",cliente.getCodigoCliente());
        map.put("nombreCliente",cliente.getNombreCliente());
        map.put("apellidoCliente",cliente.getApellidoCliente());
        map.put("telefonoCliente",cliente.getTelefonoCliente());
        map.put("celularCliente",cliente.getCelularCliente());
        map.put("correoCliente",cliente.getCorreoCliente());
        map.put("cedulaFacturacion",cliente.getCedulaFacturacion());
        map.put("rucFacturacion",cliente.getRucFacturacion());
        map.put("direccionFacturacion",cliente.getDireccionFacturacion());
        map.put("telefonoFacturacion",cliente.getTelefonoFacturacion());
        map.put("ubicacionEntrega",cliente.getUbicacionEntrega());
        map.put("latitud",cliente.getLatitud());
        map.put("longitud",cliente.getLongitud());
        map.put("estadoRegistro","informacionBasica");
        //uploadPhoto();
        mfirestore.collection("Clientes").document(id_cliente).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Has actualizado a "+
                        cliente.getCorreoCliente(),Toast.LENGTH_LONG).show();
                back();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void getCliente(){
        mfirestore=FirebaseFirestore.getInstance();
        mfirestore.collection("Clientes").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreCliente=documentSnapshot.getString("nombreCliente");
                String codigoCliente=documentSnapshot.getString("codigoCliente");
                String apellidoCliente=documentSnapshot.getString("apellidoCliente");
                String telefonoCliente=documentSnapshot.getString("telefonocliente");
                String celularCliente=documentSnapshot.getString("celularCliente");
                String correoCliente=documentSnapshot.getString("correoCliente");
                //Facturacion
                String cedulaFcaturacion=documentSnapshot.getString("cedulaFacturacion");
                String telefonoFacturacion=documentSnapshot.getString("telefonoFacturacion");
                String rucFacturacion=documentSnapshot.getString("rucFacturacion");
                String direccionFacturacion=documentSnapshot.getString("direccionFacturacion");
                //checkSpinner(spCategoria,categoria);
                //Entregas
                String ubicacionEntrega=documentSnapshot.getString("ubicacionEntrega");
                String latitud=documentSnapshot.getString("latitud");
                String longitud=documentSnapshot.getString("longitud");
                String imgClienteResource=documentSnapshot.getString("photoCliente");

                txtNombreCliente.setText(nombreCliente);
                txtCodigoCliente.setText(codigoCliente);
                txtApellidoCliente.setText(apellidoCliente);
                txtTelefonoCliente.setText(telefonoCliente);
                txtCelularCliente.setText(celularCliente);
                txtCorreoCliente.setText(correoCliente);
                txtCedulaFacturacion.setText(cedulaFcaturacion);
                txtTelefonoFacturacion.setText(telefonoFacturacion);
                txtRucFacturacion.setText(rucFacturacion);
                txtDireccionFacturacion.setText(direccionFacturacion);
                txtUbicacion.setText(ubicacionEntrega);
                txtLatitud.setText(latitud);
                txtLongitud.setText(longitud);
                try {
                    if(!imgClienteResource.equals("")){
                        Toast toast=Toast.makeText(getContext(),"Cargando imagen...",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.get().load(imgClienteResource)
                                //.fit().centerCrop()
                                //.placeholder()
                                 .resize(200,200)
                                .into(imgCliente);
                    }else{
                        Toast.makeText(getContext(),"No encontramos una imagen",Toast.LENGTH_LONG).show();
                        imgCliente.setImageResource(R.drawable.img);
                    }
                }catch(Exception e){
                    // Toast.makeText(getContext(),"Error al cargar la imagen Error: " +e,Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"No encontramos una imagen: ",Toast.LENGTH_LONG).show();
                    imgCliente.setImageResource(R.drawable.img);
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
    /*Metodo getFecha con Calendario
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

    }*/
   /*Metodo checkSpiner private void checkSpinner(Spinner spnCheck,String item){
        for(int i=0;i<spnCheck.getAdapter().getCount();i++){
            try{ if(spnCheck.getAdapter().getItem(i).equals(item)){
                positionSpinner=i;
                spnCheck.setSelection(i);
            }}
            catch(Exception e){
                Toast.makeText(getContext(),"Error: "+e,Toast.LENGTH_LONG).show();
            }

        }

    }*/

    private void iniciarControles(){

        txtNombreCliente=root.findViewById(R.id.txtNombreCliente);
        txtCodigoCliente=root.findViewById(R.id.txtCodigoCliente);
        txtApellidoCliente=root.findViewById(R.id.txtApellidoCliente);
        //spPeso=root.findViewById(R.id.spPesoProducto);
        txtTelefonoCliente=root.findViewById(R.id.txtTelefonoCliente);
        txtCelularCliente=root.findViewById(R.id.txtCelularCliente);
        txtCorreoCliente=root.findViewById(R.id.txtCorreoCliente);
        txtCedulaFacturacion=root.findViewById(R.id.txtCedulaFactura);
        txtTelefonoFacturacion=root.findViewById(R.id.txtTelefonoFactura);
        txtRucFacturacion=root.findViewById(R.id.txtRucFactura);
        txtDireccionFacturacion=root.findViewById(R.id.txtDireccionFactura);
        txtUbicacion=root.findViewById(R.id.txtUbicacionEntrega);
        txtLatitud=root.findViewById(R.id.txtLatitud);
        txtLongitud=root.findViewById(R.id.txtLongitud);
        btnDeleteimgCliente=root.findViewById(R.id.btnDeleteimgCliente);
        btnUpdateimgCliente=root.findViewById(R.id.btnUpdateimgCliente);
        btnGuardarCliente=root.findViewById(R.id.btnGuardarCliente);
        imgCliente=root.findViewById(R.id.imgAddCliente);
    }
 /*Metodo set Adapter
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
    }*/


    private void back(){

        Intent i = new Intent(getContext(),Content.class);
        i.putExtra("toShow","menuInicio");
        startActivity(i);

    }

}