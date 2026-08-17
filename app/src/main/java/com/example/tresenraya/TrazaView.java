package com.example.tresenraya;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Canvas;
public class TrazaView extends View{
    private Paint paint;
    private int[] lineaGanadora;

    public TrazaView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(12f);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setLineaGanadora(int[] lineaGanadora){
        this.lineaGanadora = lineaGanadora;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(lineaGanadora == null){
            return;
        }
        int inicio = lineaGanadora[0];
        int fin = lineaGanadora[2];

        float anchoCasilla = getWidth()/3f;
        float altoCasilla = getHeight()/3f;

        int filaInicio = inicio/3;
        int columnaInicio = inicio%3;

        int filaFin = fin/3;
        int columnaFin = fin%3;

        float xInicio = columnaInicio * anchoCasilla + anchoCasilla/2;
        float yInicio = filaInicio * altoCasilla +altoCasilla/2;

        float xFin = columnaFin * anchoCasilla + anchoCasilla/2;
        float yFin = filaFin * altoCasilla + altoCasilla/2;

        canvas.drawLine(xInicio, yInicio, xFin, yFin, paint);
    }

    public void limpiarTraza() {
        //Elimina la linea ganadora anterior
        lineaGanadora = null;
        //Redibuja la vista sin la linea
        invalidate();
    }
}
