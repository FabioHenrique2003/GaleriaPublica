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

public class GridViewFragment extends Fragment {

    private MainViewModel mViewModel;
    private View view;

    // Método para criar uma nova instância do fragmento.
    public static GridViewFragment newInstance() {
        return new GridViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla o layout "fragment_grid_view" e retorna a View correspondente.
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o ViewModel a partir da atividade associada.
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        // Instancia o adaptador para o RecyclerView.
        GridAdapter gridAdapter = new GridAdapter(new ImageDataComparator());

        // Obtém o LiveData que contém a lista paginada de ImageData.
        LiveData<PagingData<ImageData>> liveData = mViewModel.getPageLv();

        // Monitora alterações no LiveData para atualizar o adaptador sempre que os dados mudarem.
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<ImageData>>() {
            @Override
            public void onChanged(PagingData<ImageData> objectPagingData) {
                // Envia os novos dados ao adaptador, respeitando o ciclo de vida do fragmento.
                gridAdapter.submitData(getViewLifecycleOwner().getLifecycle(), objectPagingData);
            }
        });

        // Localiza o RecyclerView no layout e associa o adaptador.
        RecyclerView rvGallery = (RecyclerView) view.findViewById(R.id.rvGrid);
        rvGallery.setAdapter(gridAdapter);

        // Configura o layout do RecyclerView como uma lista vertical usando LinearLayoutManager.
        rvGallery.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
