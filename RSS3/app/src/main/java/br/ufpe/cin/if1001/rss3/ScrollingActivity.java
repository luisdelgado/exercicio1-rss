package br.ufpe.cin.if1001.rss3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class ScrollingActivity extends AppCompatActivity {

    private ListView conteudoRSS;

    // Colunas da tabela
    //Definindo constantes que representam os campos do banco de dados
    public static final String ITEM_ROWID = _ID;
    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DATE = "pubDate";
    public static final String ITEM_DESC = "description";
    public static final String ITEM_LINK = "link";
    public static final String ITEM_UNREAD = "unread";
    public final static String[] columns = { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Permissões
        String provider = "br.ufpe.cin.if1001.rss.db.RssProvider";
        Uri kUri = Uri.parse("content://br.ufpe.cin.if1001.rss.db.RssProvider");
        grantUriPermission(provider, kUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(provider, kUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission(provider, kUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        checkPermissionREAD_EXTERNAL_STORAGE(this);
        List<String> permissions = this.getGrantedPermissions("br.ufpe.cin.if1001.rss3");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            String selection = ITEM_LINK + " = ?";
            String [] selectionArgs = new String[] {"https://brasil.elpais.com/brasil/2018/04/27/tecnologia/1524832183_460349.html#?ref=rss&format=simple&link=link"};
            String sortOrder = "pubDate DESC";
            Cursor c = getContentResolver().query(kUri, columns, selection, selectionArgs, sortOrder, null);
            if (c != null) {
                if(c.moveToFirst()) {
                    String title = c.getString(1);
                    String pubDate = c.getString(2);
                    String description = c.getColumnName(3);
                    String finalLink = c.getString(4);
                }
            }
            if (c != null) {
                c.close();
            }
        }

        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);

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

    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return true;
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    List<String> getGrantedPermissions(final String appPackage) {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        granted.add(pi.requestedPermissions[i]);
                    }
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }
}
