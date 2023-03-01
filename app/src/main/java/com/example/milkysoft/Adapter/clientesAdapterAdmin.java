package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Content;
import com.example.milkysoft.Modelo.Clientes;
import com.example.milkysoft.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class clientesAdapterAdmin extends FirestoreRecyclerAdapter<Clientes, clientesAdapterAdmin.ViewHolder> {
    private static final String TAG = "Mensjae";
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    ProgressBar progressBar;
    public clientesAdapterAdmin(@NonNull FirestoreRecyclerOptions<Clientes> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;

    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.template_lista_clientes_admin,parent,false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Clientes model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        //holder.codigo.setText(model.getCodigoProducto());
        holder.nombre.setText(model.getNombreCliente()+" "+model.getApellidoCliente());
        String cedulaCliente=model.getCedulaFacturacion();
        if(cedulaCliente!=null||cedulaCliente!=""||cedulaCliente!="null") {
            holder.cedula.setText(model.getCedulaFacturacion());
        }else{
            holder.cedula.setText(model.getRucFacturacion());
        }
        holder.correo.setText(model.getCorreoCliente());
        String telefonocliente=model.getTelefonoCliente();
        if(telefonocliente!=null||telefonocliente!=""||telefonocliente!="null") {
            holder.telefono.setText(model.getTelefonoCliente());
        }else{
            holder.telefono.setText(model.getCelularCliente());
        }
        holder.codigo.setText(model.getCodigoCliente());
        holder.direccion.setText(model.getUbicacionEntrega());
        holder.direccion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        String imgClienteResource=model.getPhotoCliente();
        try{
            if(!imgClienteResource.equals("")){
                Picasso.get()
                        .load(imgClienteResource)
                        .resize(400,350)
                        .into(holder.imgCliente);


            }else{
                holder.imgCliente.setImageResource(R.drawable.img);
            }
        }catch(Exception e){
            //Toast.makeText(activity.getApplication(),"No pudimos cargar todas las imagenes",Toast.LENGTH_LONG).show();

            holder.imgCliente.setImageResource(R.drawable.img);
        }
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deleteProdcuto();
                Intent i = new Intent(activity.getApplicationContext(),Content.class);
                i.putExtra("toShow","listaPedidosAdmin");
                i.putExtra("id_cliente",id);
                activity.startActivity(i);
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentAddProduct frm=new fragmentAddProduct();
                openFragment(id);
            }
        });

    }
    private void openFragment(String id){
        Intent i = new Intent(activity.getApplicationContext(), Content.class);
        i.putExtra("toShow","modificarCliente");
        i.putExtra("id_cliente",id);
        activity.startActivity(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,codigo,direccion,cedula,telefono,correo,
                provincia,canton,calles,fechaRegitro;
        ImageView btnEliminar,btnEditar,imgCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.tvNombreClienteTemplate);

            codigo=itemView.findViewById(R.id.tvCodigoClienteTemplate);
            cedula=itemView.findViewById(R.id.tvCedulaClienteTemplate);
            correo=itemView.findViewById(R.id.tvCorreoClienteTemplate);
            telefono=itemView.findViewById(R.id.tvTelefonoClienteTemplate);
            direccion=itemView.findViewById(R.id.tvDescripcionClienteShow);
            imgCliente=itemView.findViewById(R.id.imgCliente);
            btnEliminar=itemView.findViewById(R.id.btnEliminarClienteTemplate);
            btnEditar=itemView.findViewById(R.id.btnEditarClienteTemplate);
        }
    }
}
