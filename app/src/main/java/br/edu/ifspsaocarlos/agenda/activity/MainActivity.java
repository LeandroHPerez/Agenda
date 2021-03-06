package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;


public class MainActivity extends AppCompatActivity{

    private ContatoDAO cDAO ;
    //private RecyclerView recyclerView;
    public static RecyclerView recyclerView;


    private List<Contato> contatos = new ArrayList<>();
    private TextView empty;

    private ContatoAdapter adapter;
    private SearchView searchView;

    private FloatingActionButton fab;

    private Boolean mostraMenuFiltraTodos = false;

    private static final String OPERACAO_BUSCA_TODOS = "OPERACAO_BUSCA_TODOS";
    private static final String OPERACAO_BUSCA_FAVORITOS = "OPERACAO_BUSCA_FAVORITOS";

    @Override
    public void onBackPressed() { //trata o botão voltar
        if (!searchView.isIconified()) {

            searchView.onActionViewCollapsed(); //fecha a pesquisa anterior
            updateUI(null); //pesquisa todos os contatos
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) { //Se for a action de pesquisa
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.clearFocus();
            //updateUI(query); //faz a pesquisa com os dados recuperados no campo de pesquisa - pesquisa original apenas por nome


            updateUI_nome_email(query); //adição pesquisa por nome o e-mail - v4

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Intent intent = getIntent();
        handleIntent(intent);

        cDAO= new ContatoDAO(this);

        empty= (TextView) findViewById(R.id.empty_view);
        //Coloca a Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //RecyclerView e Adapter
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();
        //Float button
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                startActivityForResult(i, 1);
            }
        });



        updateUI(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)findViewById(R.id.search_src_text);
                if (et.getText().toString().isEmpty())
                    searchView.onActionViewCollapsed();

                searchView.setQuery("", false);
                updateUI(null);
            }
        });



        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        //Ajuste da lógica do menu
        if(mostraMenuFiltraTodos == true) {
            menu.findItem(R.id.filtraTodos).setVisible(true);
            menu.findItem(R.id.filtraFavoritos).setVisible(false);

        }

        if(mostraMenuFiltraTodos == false) {
            menu.findItem(R.id.filtraTodos).setVisible(false);
            menu.findItem(R.id.filtraFavoritos).setVisible(true);
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.contato_adicionado));
             //   adapter.notifyItemInserted(adapter.getItemCount());
                updateUI(null);
            }



        if (requestCode == 2) { //Request code para detalhar contato
            if (resultCode == RESULT_OK) //salvou oua tualizou contato
                showSnackBar(getResources().getString(R.string.contato_alterado));

                //Ajusta o bug do menus
                mostraMenuFiltraTodos = false; //Corrige o bug do menu, quando volta tem que msotrar todos os contatos e o menu disponível é o dos favoritos
                invalidateOptionsMenu(); // onCreateOptionsMenu(...) é chamado novamente

            if (resultCode == 3) //apagou o contato
                showSnackBar(getResources().getString(R.string.contato_apagado));

            //Ajusta o bug do menus
                mostraMenuFiltraTodos = false; //Corrige o bug do menu, quando volta tem que msotrar todos os contatos e o menu disponível é o dos favoritos
                invalidateOptionsMenu(); // onCreateOptionsMenu(...) é chamado novamente

            updateUI(null);
        }
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout= (CoordinatorLayout)findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }


    //Pesquisa "original" apenas por nome
    private void updateUI(String nomeContato)
    {


        contatos.clear();

        if (nomeContato==null) {
            contatos.addAll(cDAO.buscaTodosContatos());
            empty.setText(getResources().getString(R.string.lista_vazia));
            fab.show();

        }
        else {
            contatos.addAll(cDAO.buscaContato(nomeContato));
            empty.setText(getResources().getString(R.string.contato_nao_encontrado));
            fab.hide();


        }

        recyclerView.getAdapter().notifyDataSetChanged();

        if (recyclerView.getAdapter().getItemCount()==0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);


        //invalidateOptionsMenu();

    }



    //Pesquisa nova por nome e email - adição v4
    private void updateUI_nome_email(String nomeOuEmailContato)
    {


        contatos.clear();

        if (nomeOuEmailContato==null) {
            contatos.addAll(cDAO.buscaTodosContatos());
            empty.setText(getResources().getString(R.string.lista_vazia));
            fab.show();

        }
        else {
            contatos.addAll(cDAO.buscaContato_nome_email(nomeOuEmailContato));
            empty.setText(getResources().getString(R.string.contato_nao_encontrado));
            fab.hide();


        }

        recyclerView.getAdapter().notifyDataSetChanged();

        if (recyclerView.getAdapter().getItemCount()==0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);


        //invalidateOptionsMenu();

    }



    private void setupRecyclerView() {


        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Contato contato = contatos.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                    showSnackBar(getResources().getString(R.string.contato_apagado));

                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }



        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }




    //Trata o clique no menu de favoritos
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.filtraFavoritos:
                //Toast.makeText(this,"Clicou no filtro por favoritos", Toast.LENGTH_LONG).show();

                mostraMenuFiltraTodos = true;
                invalidateOptionsMenu(); // onCreateOptionsMenu(...) é chamado novamente

                updateUI_Operacoes(OPERACAO_BUSCA_FAVORITOS);

                break;

            case R.id.filtraTodos:
                //Toast.makeText(this,"Filtra Todos", Toast.LENGTH_LONG).show();

                mostraMenuFiltraTodos = false;
                invalidateOptionsMenu(); // onCreateOptionsMenu(...) é chamado novamente

                updateUI_Operacoes(OPERACAO_BUSCA_TODOS);
                break;

        }
        return true;
    }


    //Criado para operações de busca específicas
    private void updateUI_Operacoes(String operacao) {

        if (operacao.toUpperCase().equals(OPERACAO_BUSCA_FAVORITOS)) {


            contatos.clear();

            contatos.addAll(cDAO.buscaContatosFavoritados());

            /*
            if (nomeContato == null) {
                contatos.addAll(cDAO.buscaContatosFavoritados());
                empty.setText(getResources().getString(R.string.lista_vazia));
                fab.show();

            } else {
                contatos.addAll(cDAO.buscaContato(nomeContato));
                empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                fab.hide();


            }

            */

            recyclerView.getAdapter().notifyDataSetChanged();

            if (recyclerView.getAdapter().getItemCount() == 0)
                empty.setVisibility(View.VISIBLE);
            else
                empty.setVisibility(View.GONE);

        }




        if (operacao.toUpperCase().equals(OPERACAO_BUSCA_TODOS)) {


            contatos.clear();

            contatos.addAll(cDAO.buscaTodosContatos());

            /*
            if (nomeContato == null) {
                contatos.addAll(cDAO.buscaContatosFavoritados());
                empty.setText(getResources().getString(R.string.lista_vazia));
                fab.show();

            } else {
                contatos.addAll(cDAO.buscaContato(nomeContato));
                empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                fab.hide();


            }

            */

            recyclerView.getAdapter().notifyDataSetChanged();

            if (recyclerView.getAdapter().getItemCount() == 0)
                empty.setVisibility(View.VISIBLE);
            else
                empty.setVisibility(View.GONE);

        }


    }
}
