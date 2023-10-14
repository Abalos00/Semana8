package com.example.semana8;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private ImageView imagen;
    private Button descargarImagen;
    private Button rotarImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagen = findViewById(R.id.imagen);
        descargarImagen = findViewById(R.id.descargarImagen);
        rotarImagen = findViewById(R.id.rotarImagen);

        descargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = loadImageFromNetwork("https://noticias.coches.com/wp-content/uploads/2012/06/Porsche-911-Club-Coup%C3%A9-1-650x388.jpg");
                        imagen.post(new Runnable() {
                            @Override
                            public void run() {
                                imagen.setImageBitmap(bitmap);
                            }
                        });
                    }
                }).start();
            }
        });

        rotarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateImage();
            }
        });
    }

    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void rotateImage() {
        if (imagen.getDrawable() != null) {
            final Bitmap originalBitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
            final Matrix matrix = new Matrix();
            matrix.postRotate(90);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                    imagen.post(new Runnable() {
                        @Override
                        public void run() {
                            imagen.setImageBitmap(rotatedBitmap);
                        }
                    });
                }
            }).start();
        }
    }
}