package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Content;
import com.example.milkysoft.Modelo.Usuario;
import com.example.milkysoft.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class usuariosAdapter extends FirestoreRecyclerAdapter<Usuario,usuariosAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public usuariosAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;


    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.template_lista_usuarios,parent,false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Usuario model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        //holder.codigo.setText(model.getCodigoProducto());
        holder.nombre.setText(model.getNombreUsuario()+" "+model.getApellidoUsuario());
        holder.cedula.setText(model.getCedulaUsuario());
        holder.correo.setText(model.getCorreoUsuario());
        holder.telefono.setText(model.getTelefonoUsuario());
        holder.codigo.setText(model.getCodigoUsuario());
        holder.direccion.setText(model.getProvinciaUsuario()+", "+
                model.getCantonUsuario()+", "+model.getCallesUsuario());
        holder.direccion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        String imgUsuarioResource=model.getPhotoUsuario();
        try{
            if(!imgUsuarioResource.equals("")){
                Picasso.get()
                        .load(imgUsuarioResource)
                        .resize(400,200)
                        .into(holder.imgUsuario);
            }else{
                holder.imgUsuario.setImageResource(R.drawable.img);
            }
        }catch(Exception e){
            //Toast.makeText(activity.getApplication(),"No pudimos cargar todas las imagenes",Toast.LENGTH_LONG).show();
            holder.imgUsuario.setImageResource(R.drawable.img);
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
               // fragmentAddProduct frm=new fragmentAddProduct();
                openFragment(id);
            }
        });

    }
    private void openFragment(String id){
        Intent i = new Intent(activity.getApplicationContext(), Content.class);
        i.putExtra("toShow","modificarUsuario");
        i.putExtra("id_usuario",id);
        activity.startActivity(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,codigo,direccion,cedula,telefono,correo,
                provincia,canton,calles,fechaRegitro;
        ImageView btnEliminar,btnEditar,imgUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.tvNombreUsuarioTemplate);

            codigo=itemView.findViewById(R.id.tvCodigoTemplate);
            cedula=itemView.findViewById(R.id.tvCedulaTemplate);
            correo=itemView.findViewById(R.id.tvCorreoTemplate);
            telefono=itemView.findViewById(R.id.tvTelefonoTemplate);
            direccion=itemView.findViewById(R.id.tvDescripcionUsuarioShow);
            imgUsuario=itemView.findViewById(R.id.imgUsuario);
            btnEliminar=itemView.findViewById(R.id.btnEliminarUsuarioTemplate);
            btnEditar=itemView.findViewById(R.id.btnEditarUsuarioTemplate);
        }
    }
}
