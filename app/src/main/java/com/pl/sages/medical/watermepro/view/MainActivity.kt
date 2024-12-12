package com.pl.sages.medical.watermepro.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationBar.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

/*
 Przechodzenie z ekranów (UI/Nawigacji) opartych na Activity do ekranów/nawigacji opartych na Fragmentach (modern)
 (1) - Robimy nowe Activity (blank) w folderze głównym view, nazywamy je MainActivity, tworzymy xml activity_main
 (2) - Ustawiamy Android Manifest, aby uruchamiane było nowe Activity w miejsce jednego ze starych (zmiane miejsca w którym jest intent-filter category-launch)
 (3) - Tworzymy XML content_main, w którym osadzony będzie navigation host (NavHostFragment)
 (4) - Tworzymy XML z kategorii navigation - nav_graph.xml w folderze res/navigation
 (5) - Tworzymy fragmenty odpowiadające nazwami aktywnościom (WaterIntakeFragment, WeatherDetailsFragment) oraz ich pliki layout w XML
 (6) - Przenosimy zawartość z plików Activity do Fragmentów (kod + XML)
 (7) - Ustawiamy zawartość MainActivity (setSupportActionBar, findNavController, AppBarConfiguration, setupActionBarWithNavController)
 (8) - W pliku nav_graph dodajemy fragment startowy i definiujemy że jest on startowy (ustawienie jego nazwy w startDestination)
 */

// Dodawanie Menu Dolnego (Bottom Navigation)

/*
(1) - Modyfikujemy content_main.xml dodając u dołu BottomNavigationView
(2) - Dopasowujemy Constraints dla widoku fragmentu tak aby nie przesłaniał widoku menu
(3) - Definiujemy plik bottom_navigation_menu.xml w folderze menu
(4) - Dodajemy elementy do menu (itemy) z nazwami i ikonami, oraz z parametrem: android:id = "@id/weatherDetailsFragment" z nazwą konkretnego fragmentu wziętą z navigation graph
(5) - W MainActivity w metodzie onCreate ustawiamy bottomNavigationBar.setupWithNavController(navController)
 */