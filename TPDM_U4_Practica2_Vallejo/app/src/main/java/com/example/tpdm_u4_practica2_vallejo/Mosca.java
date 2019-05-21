package com.example.tpdm_u4_practica2_vallejo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Mosca {
    Bitmap imagen;
    int x, y, vida, width, height;
    String tipo;
    int movimientoX, movimientoY;

    public Mosca(int x, int y, String tipo, Lienzo este) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.movimientoX = 10;
        this.movimientoY = 10;
        if (tipo.equals("fly")) {
            this.imagen = BitmapFactory.decodeResource(este.getResources(), R.drawable.fly);
            this.width = imagen.getWidth();
            this.height = imagen.getHeight();
            this.vida = 1;
        }else if (tipo.equals("boss")){
            this.imagen = BitmapFactory.decodeResource(este.getResources(), R.drawable.boss);
            this.vida = 5;
            this.width = imagen.getWidth();
            this.height = imagen.getHeight();

        }

    }

    public void desplazamiento() {
        x += movimientoX;
        y += movimientoY;
    }

    public int getVida(){
        return this.vida;
    }
    public boolean isAlive(){
        return this.vida > 0;
    }
    public boolean inArea(int xDedo,int yDedo) {
        return xDedo >= this.x && xDedo <= (x + imagen.getWidth()) && yDedo >= this.y && yDedo <= (y + imagen.getHeight());
    }

    public void pintar (Canvas c, Paint p) {
        c.drawBitmap(imagen, x, y, p);
    }
}
