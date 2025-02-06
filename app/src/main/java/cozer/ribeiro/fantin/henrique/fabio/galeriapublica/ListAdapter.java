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

public class ListAdapter extends PagingDataAdapter<ImageData, MyViewHolder> {

    public ListAdapter(@NonNull DiffUtil.ItemCallback<ImageData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout R.layout.list_item para cada item que será exibido na lista.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        // Retorna um MyViewHolder que mantém referências aos componentes do layout.
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Obtém o objeto de ImageData referente à posição especificada.
        ImageData imageData = getItem(position);

        // Referência ao TextView que exibe o nome do arquivo de imagem.
        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.fileName);

        // Formata e exibe a data da imagem
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(imageData.date));

        // Exibe o tamanho do arquivo
        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + String.valueOf(imageData.size));

        // Obtém o bitmap armazenado no objeto imageData.
        Bitmap thumb = imageData.thumb;

        // Encontra o ImageView no layout e atribui o bitmap para exibição.
        ImageView imageView = holder.itemView.findViewById(R.id.imThumb);
        imageView.setImageBitmap(thumb);
    }
}
