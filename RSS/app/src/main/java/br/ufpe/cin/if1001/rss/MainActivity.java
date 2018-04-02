package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private String RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    //private TextView conteudoRSS;
    private ListView conteudoRSS;
    
    // Responsável por receber ParserRSS.parse
    private List<ItemRSS> conteudoDois = new ArrayList<>();
    private Context context;
    private ListAdapter adapter;
    public final String rssfeed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS
        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // SharedPreferences
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        RSS_FEED = prefs.getString(rssfeed, getString(R.string.rss_feed_default));
        this.context = this;
        new CarregaRSStask().execute(RSS_FEED);
        conteudoRSS.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                // Para pegar o link foi necessário deserializar o item do adapter
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(((ItemRSS) adapter.getItem(position)).getLink()));
                startActivity(intent);
            }
        });
    }

    private class CarregaRSStask extends AsyncTask<String, Void, List<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemRSS> doInBackground(String... params) {
            String conteudo = "provavelmente deu erro...";
            try {
                conteudo = getRssFeed(params[0]);
                conteudoDois = ParserRSS.parse(conteudo);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return conteudoDois;
        }

        @Override
        protected void onPostExecute(List<ItemRSS> s) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml
            // Adapter inicializado no onPostExecute para garantir que os dados parâmetros já tenham sido carregados
            adapter = new AdapterPersonalizado(s);
            if (!adapter.isEmpty()) {
                conteudoRSS.setAdapter(adapter);
            }
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    private class AdapterPersonalizado implements ListAdapter {
        private List<ItemRSS> items;

        public AdapterPersonalizado(List<ItemRSS> s) {
            this.items = s;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int i) {
            return this.items.get(i) != null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int i) {
            return this.items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            
            // Configurando cada cédula de linha para ter seus títulos e datas respectivos
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.itemlista, viewGroup, false);
                TextView titulo = (TextView) view.findViewById(R.id.item_titulo);
                TextView data = (TextView) view.findViewById(R.id.item_data);
                titulo.setText(this.items.get(i).getTitle());
                data.setText(this.items.get(i).getPubDate());
            } else {
                TextView titulo = (TextView) view.findViewById(R.id.item_titulo);
                TextView data = (TextView) view.findViewById(R.id.item_data);
                titulo.setText(this.items.get(i).getTitle());
                data.setText(this.items.get(i).getPubDate());
            }
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return this.getCount() <= 0;
        }
    }
}
