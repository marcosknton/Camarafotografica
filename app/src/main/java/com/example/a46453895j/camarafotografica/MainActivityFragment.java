package com.example.a46453895j.camarafotografica;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 * 
 */
public class MainActivityFragment extends Fragment {

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private FirebaseListAdapter<Imagen> mAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView=(GridView) view.findViewById(R.id.Gvgallery);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("imagen");

        mAdapter=new FirebaseListAdapter<Imagen>(getActivity(),Imagen.class,R.layout.listitem,myRef) {
            @Override
            protected void populateView(View view, Imagen imagen, int i) {
                ImageView img = (ImageView) view.findViewById(R.id.imageView);

                Glide.with(getContext()).load(Uri.fromFile(new File(imagen.getRutaimagen())))
                        .centerCrop()
                        .crossFade()
                        .into(img);
            }
        };

        gridView.setAdapter(mAdapter);

        Button buttoncamera = (Button) view.findViewById(R.id.Bbutton);
        buttoncamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchTakePictureIntent(myRef);
                    }
                });
        return view;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent(DatabaseReference myRef) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            Log.i("++++++++++++++++","primer if");
            try {
                photoFile = createImageFile();


            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String ruta=photoFile.getAbsolutePath();
                Imagen imagen=new Imagen(ruta);
                myRef.push().setValue(imagen);
                Log.i("------------------",imagen.getRutaimagen());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

}
