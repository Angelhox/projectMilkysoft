package com.example.milkysoft;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkysoft.Modelo.Producto;
import com.example.milkysoft.databinding.FragmentAddProductBinding;
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

import java.util.HashMap;
import java.util.Map;


public class fragmentAddProduct extends Fragment {
    private FragmentAddProductBinding binding;

    private View root;
private ImageView imgProducto;
private EditText txtNombreProducto,txtCodigoProducto,txtPesoProducto,
    txtCantidad,txtContenido,txtDescripcion,txtPrecioCompra,txtPrecioVenta,
    txtStock;
private TextView txtContenidotext;
private Button btnGuardar,btnUpdateimgProduct,btnDeleteimgProduct;

private Spinner spCategoria,spPresentacion,spAzucar,spSal,spGrasa,
        spPeso;
FragmentManager fragmentManager;
    String id_producto=null;
    int positionSpinner=0;
FirebaseFirestore mfirestore;
private Producto productoToPost=null;
    private static final int COD_SEL_STORAGE=200;
    private static final int COD_SEL_IMAGE=300;
    StorageReference storageReference;
    String storagePath="imgProductos/*";
    private Uri img_url;
    String photo="photo";
   // String idd;
    ProgressDialog progressDialog;
     FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","AddProduct");
        editor.commit();
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        if(getArguments()!=null){
            id_producto=getArguments().getString("id_producto");
            if(id_producto!=null||id_producto!="") {
                getProducto();
            }
        }

        iniciarControles();
        setAdapterSpinner();
        imgProducto.setImageResource(R.drawable.img);
        btnUpdateimgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_producto==null||id_producto.equals("")) {
                    obtenerValores();
                    postProductos(productoToPost);
                }else{
                    obtenerValores();
                    updateProductos(productoToPost);
                }
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
        if(id_producto==null|| id_producto=="") {
            Toast.makeText(getContext(),"Crea primero un producto",
                    Toast.LENGTH_LONG).show();
        }else {
            String rute_storage_photo = storagePath + "+photo" + mAuth.getUid() + "" + id_producto;
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
                                map.put("photo", download_uri);
                                mfirestore.collection("Productos").document(id_producto).update(map);
                                Toast.makeText(getContext(), "FotoActualizada", Toast.LENGTH_LONG).show();
                                //progressDialog.dismiss();
                                try {
                                    if (!download_uri.equals("")) {
                                        Picasso.get()
                                                .load(download_uri)
                                                .resize(150, 150)
                                                .into(imgProducto);
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
    String nombreProducto,codigoProducto,pesoProducto,cantidad,contenido,categoria,presentacion
            ,azucar,sal,grasa,descripcion,precioCompra,precioVenta,stock,unidadMedida;
    nombreProducto=txtNombreProducto.getText().toString();
    codigoProducto=txtCodigoProducto.getText().toString();
    pesoProducto=txtPesoProducto.getText().toString();
    unidadMedida=spPeso.getSelectedItem().toString();
    stock=txtStock.getText().toString();
    //cantidad=txtCantidad.getText().toString();
    contenido=txtContenido.getText().toString();
    categoria=spCategoria.getSelectedItem().toString();
    presentacion=spPresentacion.getSelectedItem().toString();
    azucar=spAzucar.getSelectedItem().toString();
    sal=spSal.getSelectedItem().toString();
    grasa=spGrasa.getSelectedItem().toString();
    precioCompra=txtPrecioCompra.getText().toString();
    precioVenta=txtPrecioVenta.getText().toString();
    descripcion=txtDescripcion.getText().toString();
    productoToPost=new Producto(nombreProducto,codigoProducto,
            pesoProducto,stock,contenido,categoria,presentacion,
            azucar,sal,grasa,descripcion,precioCompra,precioVenta,null,unidadMedida);

    }
    private void postProductos(Producto producto){
        mfirestore= FirebaseFirestore.getInstance();
        Map<String,Object> map=new HashMap<>();
        map.put("codigoProducto",producto.getCodigoProducto());
        map.put("nombreProducto",producto.getNombreProducto());
        map.put("pesoProducto",producto.getPesoProducto());
        map.put("unidadMedida",producto.getUnidadMedida());
        map.put("stock",producto.getStock());
       // map.put("cantidad",producto.getCantidad());
        map.put("contenido",producto.getContenido());
        map.put("categoria",producto.getCategoria());
        map.put("presentacion",producto.getPresentacion());
        map.put("contenidoAzucar",producto.getContenidoAzucar());
        map.put("contenidoSal",producto.getContenidoSal());
        map.put("contenidoGrasa",producto.getContenidoGrasa());
        map.put("descripcion",producto.getDescripcion());
        map.put("precioCompra",producto.getPrecioCompra());
        map.put("precioVenta",producto.getPrecioVenta());
        map.put("stock",producto.getStock());
        //uploadPhoto();
        mfirestore.collection("Productos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Has registrado un nuevo roducto",Toast.LENGTH_LONG).show();
           back();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    private void updateProductos(Producto producto){
        mfirestore= FirebaseFirestore.getInstance();
        Map<String,Object> map=new HashMap<>();
        map.put("codigoProducto",producto.getCodigoProducto());
        map.put("nombreProducto",producto.getNombreProducto());
        map.put("pesoProducto",producto.getPesoProducto());
        map.put("unidadMedida",producto.getUnidadMedida());
        map.put("stock",producto.getStock());
        // map.put("cantidad",producto.getCantidad());
        map.put("contenido",producto.getContenido());
        map.put("categoria",producto.getCategoria());
        map.put("presentacion",producto.getPresentacion());
        map.put("contenidoAzucar",producto.getContenidoAzucar());
        map.put("contenidoSal",producto.getContenidoSal());
        map.put("contenidoGrasa",producto.getContenidoGrasa());
        map.put("descripcion",producto.getDescripcion());
        map.put("precioCompra",producto.getPrecioCompra());
        map.put("precioVenta",producto.getPrecioVenta());
        map.put("stock",producto.getStock());
        //uploadPhoto();
        mfirestore.collection("Productos").document(id_producto).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Has actualizado un nuevo producto",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void getProducto(){
        mfirestore=FirebaseFirestore.getInstance();
        mfirestore.collection("Productos").document(id_producto).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String nombreProducto=documentSnapshot.getString("nombreProducto");
                String codigoProducto=documentSnapshot.getString("codigoProducto");
                String pesoProducto=documentSnapshot.getString("pesoProducto");
                String unidadMedida=documentSnapshot.getString("unidadMedida");
                checkSpinner(spPeso,unidadMedida);
                String stock=documentSnapshot.getString("stock");
                //String cantidad=documentSnapshot.getString("cantidad");
                String contenido=documentSnapshot.getString("contenido");

                String categoria=documentSnapshot.getString("categoria");
                checkSpinner(spCategoria,categoria);

                String presentacion=documentSnapshot.getString("presentacion");
                checkSpinner(spPresentacion,presentacion);
                String contenidoAzucar=documentSnapshot.getString("contenidoAzucar");
                checkSpinner(spAzucar,contenidoAzucar);
                String contenidoSal=documentSnapshot.getString("contenidoSal");
                checkSpinner(spSal,contenidoSal);
                String contenidoGrasa=documentSnapshot.getString("contenidoGrasa");
                checkSpinner(spGrasa,contenidoGrasa);
                String descripcion=documentSnapshot.getString("descripcion");
                String precioCompra=documentSnapshot.getString("precioCompra");
                String precioVenta=documentSnapshot.getString("precioVenta");
                String imgProductoResource=documentSnapshot.getString("photo");

                txtNombreProducto.setText(nombreProducto);
                txtCodigoProducto.setText(codigoProducto);
                txtPesoProducto.setText(pesoProducto);
                txtStock.setText(stock);
                //txtCantidad.setText(cantidad);
                txtContenido.setText(contenido);
                txtDescripcion.setText(descripcion);
                txtPrecioCompra.setText(precioCompra);
                txtPrecioVenta.setText(precioVenta);
                try {
                    if(!imgProductoResource.equals("")){
                        Toast toast=Toast.makeText(getContext(),"Cargando imagen...",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.get()
                                .load(imgProductoResource)
                                .resize(150,150)
                                .into(imgProducto);
                    }else{
                        Toast.makeText(getContext(),"No encontramos una imagen",Toast.LENGTH_LONG).show();
                        imgProducto.setImageResource(R.drawable.img);
                    }
                }catch(Exception e){
                   // Toast.makeText(getContext(),"Error al cargar la imagen Error: " +e,Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"No encontramos una imagen: ",Toast.LENGTH_LONG).show();
                    imgProducto.setImageResource(R.drawable.img);
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
        txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
        txtCodigoProducto=root.findViewById(R.id.txtCodigoProducto);
        txtPesoProducto=root.findViewById(R.id.txtPeso);
        spPeso=root.findViewById(R.id.spPesoProducto);
        //txtCantidad=root.findViewById(R.id.txtCantidadProducto);
        txtContenido=root.findViewById(R.id.txtContenido);
        txtContenidotext=root.findViewById(R.id.txtContenidoProductotext);
        spCategoria=root.findViewById(R.id.spCategoriaProducto);
        spPresentacion=root.findViewById(R.id.spPresentacion);
        spAzucar=root.findViewById(R.id.spAzucar);
        spSal=root.findViewById(R.id.spSal);
        spGrasa=root.findViewById(R.id.spGrasa);
        txtPrecioCompra=root.findViewById(R.id.txtPrecioCompra);
        txtPrecioVenta=root.findViewById(R.id.txtPrecioVenta);
        txtStock=root.findViewById(R.id.txtStock);
        txtDescripcion=root.findViewById(R.id.txtDetallesProducto);
        btnGuardar=root.findViewById(R.id.btnGuardarProducto);
        imgProducto=root.findViewById(R.id.imgAddProducto);
        btnDeleteimgProduct=root.findViewById(R.id.btnDeleteimgProducto);
        btnUpdateimgProduct=root.findViewById(R.id.btnUpdateimgProducto);
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
       /* Unused code try to delete <--------
       SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","AddProduct");
        editor.commit();
        AppCompatActivity activity=(AppCompatActivity) view.getContext();
        listaProductos mListaProduct = new listaProductos();


        activity.getSupportFragmentManager().beginTransaction().replace(R.id.layoutLista,mListaProduct)
                .addToBackStack(null).commit();*/
        Intent i = new Intent(getContext(),listaProductosact.class);
        startActivity(i);

    }
}