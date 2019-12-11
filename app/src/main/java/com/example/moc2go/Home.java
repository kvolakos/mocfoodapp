package com.example.moc2go;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.example.moc2go.ui.home.HomeFragment;

public class Home extends AppCompatActivity {
    ImageView Map;
    Matrix matrix = new Matrix();
    float scale = 1f;
    ScaleGestureDetector SGD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Map = findViewById(R.id.Map);

        SGD = new ScaleGestureDetector(this, new scaleListener());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commitNow();
        }
    }

    private class scaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector){
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale,5f));
            matrix.setScale(scale,scale);
            Map.setImageMatrix(matrix);
            return true;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }
}
