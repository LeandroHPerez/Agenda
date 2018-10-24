package br.edu.ifspsaocarlos.agenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.activity.MainActivity;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.R;

import java.util.List;


public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private static List<Contato> contatos;
    private Context context;


    private static ItemClickListener clickListener;



    public ContatoAdapter(List<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ContatoViewHolder holder, int position) {
       holder.nome.setText(contatos.get(position).getNome());

       //Se o contato for favorito coloca a estrela dourada, caso contrário a estrela cinza
       if(contatos.get(position).getFavorito() == 1) {
           holder.img_favoritar.setImageResource(R.drawable.ic_estrela_dourada);
       }
       else {
           holder.img_favoritar.setImageResource(R.drawable.ic_estrela_cinza);
       }

        //Guarda no "bolso" da view criada o holder
        holder.img_favoritar.setTag(holder);



        //recupera o holder do bolso de uma view reaproveitada
        //holder = (Holder) convertView.getTag();

        //Recuperar a posição
        //holder.getPosition();

        //Colocar listener de clique do botão favoritar
        ajustarCliqueBotaoFavoritar(holder.img_favoritar);
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView nome;
        final ImageView img_favoritar;

        ContatoViewHolder(View view) {
            super(view);
            nome = (TextView)view.findViewById(R.id.nome);
            img_favoritar = (ImageView) view.findViewById(R.id.img_view_favoritar);

            //Colocar listener de clique do botão favoritar
            //ajustarCliqueBotaoFavoritar(img_favoritar);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }

//teste
    public interface aaaaaaItemClickListener {
        void onItemClick(int position);
    }



    private void ajustarCliqueBotaoFavoritar(ImageView img_favoritar){

        img_favoritar.setClickable(true);
        img_favoritar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContatoViewHolder holder;

                //recupera o holder do bolso de uma view reaproveitada
                holder = (ContatoViewHolder) v.getTag();

                //holder.getPosition();

                //Toast da linha clicada para teste
                 Toast.makeText(v.getContext(),
                        "Clicou no favoritar = " + holder.getPosition(),
                        Toast.LENGTH_LONG).show();





                Contato c;
                c = contatos.get(holder.getPosition());

                //Faz a lógica de inverter os dados de favoritar o contato
                if(c.getFavorito() == 1) {
                    c.setFavorito(0);
                }
                else {
                    c.setFavorito(1);
                }

                ContatoDAO cDAO;
                cDAO = new ContatoDAO(context);
                cDAO.favoritarContato(c);


                //cDAO.teste();

            }
        });



    }

}


