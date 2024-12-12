package com.pl.sages.medical.watermepro.view.profile

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.ACTION_PICK_IMAGES
import android.provider.MediaStore.EXTRA_OUTPUT
import android.provider.MediaStore.Images.Media.DATA
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Images.Media.TITLE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment

import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    var photoUri: Uri? = null

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    // (6) Rejestracja callbacku z uprawnieniami -> wykona się on wtedy gdy użytkownik zdecyduje o przyznaniu lub nie uprawnień (wszystkich)
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                showDialog()// (7) Jeśli użytkownik przyznał uprawnienia -> pokazujemy dialog z wyborem opcji
            } else {
                // (7a) A jeśli nier no to pokazujemy informację o tym
                Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = "Profile Picture"
        setupUI()
    }

    override fun onResume() {
        super.onResume()
    }



    fun setupUI() {
        Picasso.get()
            .load("https://picsum.photos/600/600")
            .into(binding.avatarImageView)

        binding.avatarImageView.setOnClickListener {
            changePictureWithMediaIfPossible() // (2) Uruchamiamy onClickListener
        }
    }

    fun changePictureWithMediaIfPossible() {
        // (3) Sprawdzamy czy mamy uprawnienia
        if (checkIfPermissionsGranted()) {
            showDialog() // (4) Jeśli tak. -> pokazujemy dialog z wyborem opcji
        } else {
            // (5) Nie -> pytamy o uprawnienia
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS.toTypedArray())
        }
    }

    //(8) Pokazujemy 3 opje do wyboru
    fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Choose image source")
            .setItems(arrayOf("Use Gallery", "Take Photo", "Cancel")) { dialog, index ->
                when(index) { // (8a) - Tutaj wchodzimy jak użytkownik kliknie na którąś z opcji
                    0 -> showGallery() // (8b) - uruchom kamerę (aparat)
                    1 -> startCamera() // (8c) - uruchom galerię
                    else -> dialog.dismiss() // (8d) - zamknij okienko dialogowe
                }
            }
            .show()
    }


    fun startCamera() {
        // Tworzymy intencję - mówimy systemowi, że chcemy uruchomić aparat
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Tworzymy obiekt ContentValues, który będzie zawierał informacje o zdjęciu
        // a konkretnie o jego tytule i adresie URI -> potrzebne potem do wczytania tego zdjęcia aby go wyświetlić
        val contentValues = ContentValues()
        contentValues.put(TITLE, "Profile Photo")
        photoUri = requireContext().contentResolver.insert(EXTERNAL_CONTENT_URI, contentValues)

        // Dodajemy dodatkowe informacje do intencji, aby wiedziała gdzie ma zapisać zdjęcie
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        // Uruchamiamy intencję, z wcześniejeszymi informacjami i zainicjalizowanym callbakiem requestPhoto
        requestPhoto.launch(cameraIntent)
    }

    private fun showGallery() {
        val intent = Intent(ACTION_PICK_IMAGES)
        requestGallery.launch(intent)
    }

    // (9) Callback uruchamiany po zakńczeniu działania aplikacji aparat
    private val requestPhoto = registerForActivityResult(StartActivityForResult()) { result ->
        // Sprawdzamy czy rezultat jest OK - czyli że np. użytkownik tego nie zakończył w trakcie robienia zdjęcia
        if (result.resultCode == RESULT_OK) {
            // Jeśli rezultat jest OK -> Wczytujemy zdjęcie zapisane w zmiennej photoUri
            Picasso.get()
                .load(photoUri)
                .resize(200,200)
                .transform(GrayscaleTransformation())
                .into(binding.avatarImageView)

        }
    }

    // (X) -> Dla galerii procedura analogiczna
    private val requestGallery = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d("Gallery", "Result OK")
        }
    }

    fun checkIfPermissionsGranted(): Boolean {
        val allPermissionsGranted = REQUIRED_PERMISSIONS.all {
            checkSelfPermission(requireContext(), it) == PERMISSION_GRANTED
        }
        return allPermissionsGranted
    }

    companion object {
        // (1) Definicja listy wymaganych uprawnień (uprawnienia który musi przyznać nam użytkownik)
        private val REQUIRED_PERMISSIONS = listOf(CAMERA, READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    }
}