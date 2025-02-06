package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryRepository {

    Context context;

    public GalleryRepository(Context context) {
        this.context = context;
    }

    public List<ImageData> loadImageData(Integer limit, Integer offSet) throws FileNotFoundException {
        // Lista para armazenar os detalhes das imagens recuperadas.
        List<ImageData> imageDataList = new ArrayList<>();

        // Dimensões da miniatura para exibição.
        int w = (int) context.getResources().getDimension(R.dimen.im_width);
        int h = (int) context.getResources().getDimension(R.dimen.im_height);

        // Define as colunas que serão extraídas do MediaStore.
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
        };

        // Critérios de filtragem e seus argumentos.
        String selection = null;
        String[] selectionArgs = null;

        // Ordenação das imagens com base na data de adição.
        String sort = MediaStore.Images.Media.DATE_ADDED;

        // Cursor para buscar as imagens na galeria externa.
        Cursor cursor = null;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Bundle queryArgs = new Bundle();
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);

            // Define a coluna e a ordem de classificação.
            queryArgs.putString(ContentResolver.QUERY_ARG_SORT_COLUMNS, sort);
            queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_ASCENDING);

            // Define o número de registros a serem retornados e o deslocamento inicial.
            queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, limit);
            queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, offSet);

            // Realiza a consulta com os parâmetros especificados.
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
            );
        } else {
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sort + " ASC + LIMIT " + limit + " OFFSET " + offSet
            );
        }

        // Obtém os índices das colunas do cursor para facilitar a extração dos dados.
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

        // Percorre cada linha do cursor e extrai os dados necessários.
        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            // Cria a URI correspondente à imagem recuperada.
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            String name = cursor.getString(nameColumn);
            int dateAdded = cursor.getInt(dateAddedColumn);
            int size = cursor.getInt(sizeColumn);

            // Gera a miniatura do bitmap.
            Bitmap thumb = Util.getBitmap(context, contentUri, w, h);

            // Instancia um objeto ImageData e adiciona na lista.
            imageDataList.add(
                    new ImageData(
                            contentUri,
                            thumb,
                            name,
                            new Date(dateAdded * 1000L),
                            size
                    )
            );
        }
        return imageDataList;
    }
}

