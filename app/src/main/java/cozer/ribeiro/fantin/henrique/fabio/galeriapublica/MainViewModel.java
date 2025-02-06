package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import kotlinx.coroutines.CoroutineScope;

public class MainViewModel extends AndroidViewModel {

    // Armazena a opção de navegação selecionada pelo usuário.
    private int navigationOpSelected = R.id.gridViewOp;

    // LiveData que emite PagingData contendo objetos ImageData.
    private final LiveData<PagingData<ImageData>> pageLv;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // Instancia o repositório para acessar os dados da galeria.
        GalleryRepository galleryRepository = new GalleryRepository(application);

        // Define a fonte de paginação para carregar os dados conforme necessário.
        GalleryPagingSource galleryPagingSource = new GalleryPagingSource(galleryRepository);

        // Configura a paginação com um tamanho de lote de 10 itens por requisição.
        Pager<Integer, ImageData> pager = new Pager<>(new PagingConfig(10), () -> galleryPagingSource);

        // Obtém o escopo de corrotinas do ViewModel para operações assíncronas.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        // Configura o LiveData para fornecer os dados paginados.
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    // Retorna o LiveData contendo os dados paginados da galeria.
    public LiveData<PagingData<ImageData>> getPageLv() {
        return pageLv;
    }

    // Retorna a opção de navegação selecionada atualmente.
    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    // Atualiza a opção de navegação selecionada.
    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }
}
