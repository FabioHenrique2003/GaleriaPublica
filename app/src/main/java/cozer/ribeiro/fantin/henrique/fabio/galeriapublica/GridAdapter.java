package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import java.text.SimpleDateFormat;

public class GridAdapter extends PagingDataAdapter<ImageData, MyViewHolder> {
    public GridAdapter(@NonNull DiffUtil.ItemCallback<ImageData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Cria um objeto LayoutInflater e infla o layout 'list_item' para cada item da lista.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        // Retorna uma instância de MyViewHolder associada ao layout criado.
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Obtém a instância de ImageData correspondente à posição atual.
        ImageData imageData = getItem(position);

        // Define o nome do arquivo de imagem no TextView correspondente.
        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.fileName);

        // Formata a data no padrão "HH:mm dd/MM/yyyy" e define no TextView apropriado.
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(imageData.date));

        // Exibe o tamanho do arquivo no respectivo TextView.
        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + String.valueOf(imageData.size));

        // Obtém a miniatura (bitmap) armazenada na instância de ImageData.
        Bitmap thumb = imageData.thumb;
        // Exibe a miniatura no ImageView correspondente.
        ImageView imageView = holder.itemView.findViewById(R.id.imThumb);
        imageView.setImageBitmap(thumb);
    }
}
