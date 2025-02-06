package cozer.ribeiro.fantin.henrique.fabio.galeriapublica;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    // Método para trocar o fragmento exibido na tela.
    void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Ajusta os insets da interface para lidar com áreas seguras, como barras de status e navegação.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtém uma instância do MainViewModel para gerenciar os dados da atividade.
        final MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

        // Configura a barra de navegação inferior (BottomNavigationView).
        bottomNavigationView = findViewById(R.id.btNav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Atualiza o ViewModel com a opção de navegação selecionada.
                vm.setNavigationOpSelected(item.getItemId());

                // Alterna entre os fragmentos com base no item selecionado.
                if (item.getItemId() == R.id.gridViewOp) {
                    setFragment(GridViewFragment.newInstance());
                } else if (item.getItemId() == R.id.listViewOp) {
                    setFragment(ListViewFragment.newInstance());
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verifica se as permissões necessárias foram concedidas.
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        checkForPermissions(permissions);
    }

    private void checkForPermissions(List<String> permissions) {
        // Lista de permissões que ainda não foram concedidas.
        List<String> permissionsNotGranted = new ArrayList<>();

        // Obtém a última opção de navegação selecionada e a aplica novamente.
        MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);
        int navigationOpSelected = vm.getNavigationOpSelected();
        bottomNavigationView.setSelectedItemId(navigationOpSelected);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Lista de permissões que foram negadas pelo usuário.
        List<String> permissionsRejected = new ArrayList<>();

        // Verifica quais permissões não foram concedidas.
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionsRejected.add(permissions[i]);
            }
        }

        if (!permissionsRejected.isEmpty()) {
            // Exibir alerta caso haja permissões rejeitadas.
        } else {
            // Caso todas as permissões sejam concedidas, restaura a opção de navegação previamente selecionada.
            MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);
            int navigationOpSelected = vm.getNavigationOpSelected();
            bottomNavigationView.setSelectedItemId(navigationOpSelected);
        }
    }
}

