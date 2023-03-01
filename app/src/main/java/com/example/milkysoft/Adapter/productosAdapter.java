package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Content;
import com.example.milkysoft.Modelo.Producto;
import com.example.milkysoft.R;
import com.example.milkysoft.carritofragment;
import com.example.milkysoft.fragmentAddProduct;
import com.example.milkysoft.listaProductos;
import com.example.milkysoft.menuInicio;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class productosAdapter extends FirestoreRecyclerAdapter<Producto,productosAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    FragmentManager fragmentManager;
    fragmentAddProduct mfragmentAddProduct;
    String mode="";
    int numeroPedido = (int)(Math.random() * (10000+1000)-1) + 1000;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public productosAdapter(@NonNull FirestoreRecyclerOptions<Producto> options,Activity activity,FragmentManager fm) {
        super(options);
       this.activity=activity;
        this.fm=fm;


    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.template_lista_productos,parent,false);
        return new ViewHolder(v);


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Producto model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        //holder.codigo.setText(model.getCodigoProducto());
        SharedPreferences preferenciasLogeo = activity.getSharedPreferences("preferenciasLogin",0);
        mode=preferenciasLogeo.getString("mode","invitado");
        Toast.makeText(activity.getApplicationContext(), mode,Toast.LENGTH_LONG).show();
        if(mode=="cliente"){
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnEliminar.setVisibility(View.GONE);
        }else if(mode=="admin"){
            holder.btnCarrito.setVisibility(View.GONE);
        }else if(mode=="invitado"){
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnEliminar.setVisibility(View.GONE);
        }
        holder.name.setText(model.getNombreProducto());
        holder.peso.setText(model.getPesoProducto());
        holder.cantidad.setText(model.getContenido());
        holder.stock.setText(model.getStock());
        holder.precioCompra.setText(model.getPrecioCompra());
        holder.precioVenta.setText(model.getPrecioVenta());
        holder.descripcion.setText(model.getDescripcion());
        holder.descripcionToShow.setText(model.getDescripcion()+" de "+model.getPesoProducto()+" "
                +model.getUnidadMedida()+" presentacion de "+model.getPresentacion()
                +" por "+model.getContenido()+" Unidad(es)\n"+
        "Informacion Nutricional\n"+model.getContenidoAzucar()+" en azúcar, "+model.getContenidoSal()+" en sal,"+
                model.getContenidoGrasa()+" en grasa.");
        holder.unidadMedida.setText(model.getUnidadMedida());
        String imgProductoResource=model.getPhoto();
        try{
            if(!imgProductoResource.equals("")){
                Picasso.get()
                        .load(imgProductoResource)
                        .resize(400,200)
                        .into(holder.imgProducto);
            }else{
                holder.imgProducto.setImageResource(R.drawable.img);
            }
        }catch(Exception e){
        //Toast.makeText(activity.getApplication(),"No pudimos cargar todas las imagenes",Toast.LENGTH_LONG).show();
            holder.imgProducto.setImageResource(R.drawable.img);
        }
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // deleteProdcuto();
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAddProduct frm=new fragmentAddProduct();
                openFragment(v,id,frm);
            }
        });
        holder.btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == "invitado") {
                    Toast.makeText(activity.getApplicationContext(), "Inicia Sesion antes de comprar",Toast.LENGTH_LONG).show();

                }else {
                    carritofragment carritofragment = new carritofragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("numeroPedido", "pedido" + String.valueOf(numeroPedido));
                    bundle.putString("codigoProducto", model.getCodigoProducto());
                    bundle.putString("photoProducto", model.getPhoto());
                    carritofragment.setArguments(bundle);
                    carritofragment.show(fm, "open fragment");
                }
            }
        });

    }
    private void openFragment(View view,String id,fragmentAddProduct frm){
      /*  listaProductos frmListaProductos= new listaProductos();
        Bundle bundle=new Bundle();
        bundle.putString("id_producto",id);
        frm.setArguments(bundle);
        FragmentTransaction fr= Content.class.beginTransaction();
        fr.replace(R.id.drawer_layout,new fragmentAddProduct()).commit();*/
      /*  Unused but important don't delete <--------------
      AppCompatActivity activity=(AppCompatActivity) view.getContext();
        fragmentAddProduct mfragmentAddProduct = new fragmentAddProduct();
        Bundle bundle=new Bundle();
        bundle.putString("id_producto",id);
        mfragmentAddProduct.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.layoutLista,mfragmentAddProduct)
                .addToBackStack(null).commit();*/
        Intent i = new Intent(activity.getApplicationContext(),Content.class);
        i.putExtra("toShow","modificarProducto");
        i.putExtra("id_producto",id);

        activity.startActivity(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,codigo,peso,cantidad,stock,precioCompra,precioVenta,descripcion,unidadMedida,descripcionToShow;
        ImageView btnEliminar,btnEditar,imgProducto,btnCarrito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvNombreProductoTemplate);
            //codigo=itemView.findViewById(R.id.txtCodigoProducto);
            peso=itemView.findViewById(R.id.tvPesoTemplate);
            cantidad=itemView.findViewById(R.id.tvContenidoTemplate);
            stock=itemView.findViewById(R.id.tvDisponibleTemplate);
            precioCompra=itemView.findViewById(R.id.tvPrecioCompraTemplate);
            precioVenta=itemView.findViewById(R.id.tvPrecioVentaTemplate);
            descripcion=itemView.findViewById(R.id.tvDescripcionTemplate);
            unidadMedida=itemView.findViewById(R.id.tvUnidadMedidaTemplate);
            descripcionToShow=itemView.findViewById(R.id.tvDescripcionShow);
            imgProducto=itemView.findViewById(R.id.imgProducto);
            btnEliminar=itemView.findViewById(R.id.btnEliminarTemplate);
            btnEditar=itemView.findViewById(R.id.btnEditarTemplate);
            btnCarrito=itemView.findViewById(R.id.btnAñadirCarrito);
        }
    }
}
