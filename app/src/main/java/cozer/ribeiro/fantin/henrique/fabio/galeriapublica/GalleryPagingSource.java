package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class GalleryPagingSource extends ListenableFuturePagingSource<Integer, ImageData> {

    GalleryRepository galleryRepository;
    // Instância do repositório responsável por obter as imagens

    Integer initialLoadSize = 0;

    public GalleryPagingSource(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @Nullable
    @Override
    // Define a chave usada para reinicializar a listagem quando os dados forem invalidados
    public Integer getRefreshKey(@NonNull PagingState<Integer, ImageData> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, ImageData>> loadFuture(@NonNull LoadParams<Integer> loadParams) {

        Integer nextPageNumber = loadParams.getKey();
        // Define a primeira página se não houver uma chave de início
        if (nextPageNumber == null) {
            nextPageNumber = 1;
            initialLoadSize = loadParams.getLoadSize();
        }

        Integer offSet = 0;
        if(nextPageNumber == 2) {
            // Define o deslocamento para a segunda página
            offSet = initialLoadSize;
        } else {
            // Para páginas posteriores, o deslocamento é calculado com base nas páginas anteriores
            offSet = ((nextPageNumber - 1) * loadParams.getLoadSize())
                    + (initialLoadSize - loadParams.getLoadSize());
        }

        // Cria um executor assíncrono para carregar os dados em segundo plano
        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

        Integer finalOffSet = offSet;
        Integer finalNextPageNumber = nextPageNumber;

        ListenableFuture<LoadResult<Integer, ImageData>> lf =
                service.submit(new Callable<LoadResult<Integer, ImageData>>() {
                    @Override
                    public LoadResult<Integer, ImageData> call() {
                        // Obtém a lista de imagens do repositório
                        List<ImageData> imageDataList = null;
                        try {
                            imageDataList = galleryRepository.loadImageData(
                                    loadParams.getLoadSize(),
                                    finalOffSet
                            );

                            Integer nextKey = null;
                            // Se a quantidade de itens carregados for suficiente, define a próxima página
                            if(imageDataList.size() >= loadParams.getLoadSize()) {
                                nextKey = finalNextPageNumber + 1;
                            }

                            // Retorna os resultados carregados, incluindo a chave da próxima página
                            return new LoadResult.Page<>(
                                    imageDataList,
                                    null,
                                    nextKey
                            );
                        } catch (FileNotFoundException e) {
                            // Caso ocorra uma exceção, retorna um erro como resultado do carregamento
                            return new LoadResult.Error<>(e);
                        }
                    }
                });

        // Retorna um ListenableFuture que fornecerá o resultado ao ser concluído
        return lf;
    }
}
