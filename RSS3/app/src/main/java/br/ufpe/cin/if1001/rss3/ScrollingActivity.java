package br.ufpe.cin.if1001.rss3;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import static android.provider.BaseColumns._ID;

public class ScrollingActivity extends AppCompatActivity {

    //Definindo constantes que representam os campos do banco de dados
    public static final String ITEM_ROWID = _ID;
    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DATE = "pubDate";
    public static final String ITEM_DESC = "description";
    public static final String ITEM_LINK = "link";
    public static final String ITEM_UNREAD = "unread";

    // Colunas da tabela
    public final static String[] columns = { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView conteudoRSS = findViewById(R.id.conteudoRSS);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                //contexto, como estamos acostumados
                this,
                //Layout XML de como se parecem os itens da lista
                R.layout.itemlista,
                //Objeto do tipo Cursor, com os dados retornados do banco.
                //Como ainda não fizemos nenhuma consulta, está nulo.
                null,
                //Mapeamento das colunas nos IDs do XML.
                // Os dois arrays a seguir devem ter o mesmo tamanho
                new String[]{"title", "pubDate"}, new int[]{R.id.item_titulo, R.id.item_data},
                //Flags para determinar comportamento do adapter, pode deixar 0.
                0);
        //Seta o adapter. Como o Cursor é null, ainda não aparece nada na tela.
        conteudoRSS.setAdapter(adapter);

        // permite filtrar conteudo pelo teclado virtual
        conteudoRSS.setTextFilterEnabled(true);

        // Permissões
        String provider = "br.ufpe.cin.if1001.rss.db.RssProvider";
        Uri kUri = Uri.parse("content://br.ufpe.cin.if1001.rss.db.RssProvider");
        grantUriPermission(provider, kUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(provider, kUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            grantUriPermission(provider, kUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }

        // Pegando conteúdo do Content Provider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Cursor c = getContentResolver().query(kUri, columns, null, null, null, null);
            if (c != null) {
                ((SimpleCursorAdapter) conteudoRSS.getAdapter()).changeCursor(c);
            }
        }
    }
}
