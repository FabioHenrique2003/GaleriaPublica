package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListViewFragment extends Fragment {

    private MainViewModel mViewModel;
    private View view;

    // Criação de uma nova instância do fragmento.
    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla o layout "fragment_list_view" e retorna a View resultante.
        view = inflater.inflate(R.layout.fragment_list_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o ViewModel obtendo-o a partir da Activity que contém o fragmento.
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        // Instancia o adaptador responsável por exibir os itens na lista, utilizando um comparador para diferenciação eficiente dos elementos.
        ListAdapter listAdapter = new ListAdapter(new ImageDataComparator());

        // Obtém o LiveData que contém os dados paginados do ViewModel.
        LiveData<PagingData<ImageData>> liveData = mViewModel.getPageLv();

        // Adiciona um observador ao LiveData para atualizar a lista sempre que os dados forem modificados.
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<ImageData>>() {
            @Override
            public void onChanged(PagingData<ImageData> objectPagingData) {
                // Envia os novos dados ao adaptador, garantindo que a atualização ocorra dentro do ciclo de vida do fragmento.
                listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), objectPagingData);
            }
        });

        // Localiza o RecyclerView no layout e associa o adaptador.
        RecyclerView rvGallery = (RecyclerView) view.findViewById(R.id.rvList);
        rvGallery.setAdapter(listAdapter);

        // Define o layout do RecyclerView como LinearLayoutManager, organizando os itens em uma lista vertical.
        rvGallery.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
