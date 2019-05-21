package com.example.tpdm_u4_practica2_vallejo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Lienzo extends View {

    ArrayList<Mosca> moscas;
    Thread decometro;
    int score = 0, tiempo = 60;
    CountDownTimer timer;
    String ganastePerdiste = "";
    int contadorMoscas = 0;
    boolean bossTrigger = false;
    Random random;
    public Lienzo(Context context) {
        super(context);
        random =new Random();
        moscas = new ArrayList<>();
        crearMosca(moscas);
        decometro = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (contadorMoscas == 30) {
                        if (!bossTrigger) {
                            bossTrigger = true;
                            tiempo = 10;

                            crearBoss(moscas);
                            timer.cancel();
                            timer.start();
                        }

                    }
                    if (moscas.isEmpty() && !bossTrigger) {
                        crearMosca(moscas);
                    }
                    for (Mosca mosca: moscas) {
                        mosca.desplazamiento();
                    }
                    try {

                        sleep(60);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    invalidate();
                }
            }
        });
        decometro.start();
        timer = new CountDownTimer(tiempo * 10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempo --;
                if (!bossTrigger){
                    crearMosca(moscas);
                } else {
                    if (contadorMoscas >= 31) {
                        ganastePerdiste = " GANASTE UwU";
                        cancel();
                    }

                }
            }

            @Override
            public void onFinish() {
                ganastePerdiste = " PERDISTE x.x";

            }
        };
        timer.start();
        invalidate();
    }

    public void crearMosca(ArrayList<Mosca> m){
        m.add(new Mosca(nextR(1000), nextR(1000), "fly", this));
    }
    public void crearBoss(ArrayList<Mosca> m){
        m.clear();
        m.add(new Mosca(nextR(500)+100, nextR(500)+100, "boss", this));
    }
    public int nextR(int bound){
        return random.nextInt(bound);
    }

    public void limites(Mosca mosca, Canvas c){
        if((mosca.x + mosca.width ) >= c.getWidth()){
            mosca.movimientoX *= -1;
        }
        if((mosca.x  - mosca.width) < 1){
            mosca.movimientoX *= -1;
        }
        if((mosca.y + mosca.height  )>= c.getHeight()){
            mosca.movimientoY *= -1;
        }
        if((mosca.y - mosca.height ) < 1){
            mosca.movimientoY *= -1;
        }
    }
    private boolean viewAlreadyTouched = false;
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        int accion = me.getAction();
        int posx = (int) me.getX();
        int posy = (int) me.getY();

        switch (accion) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOCADO " + contadorMoscas);
                if(viewAlreadyTouched) {
                    return true;
                } else {
                    viewAlreadyTouched = true;
                    for (int i = 0; i < moscas.size() ; i ++) {
                        if (moscas.get(i).inArea(posx, posy)) {
                            if (moscas.get(i).isAlive()){
                                moscas.get(i).vida--;
                            } else {
                                switch (moscas.get(i).tipo) {
                                    case "fly":
                                        moscas.remove(moscas.get(i));
                                        score+= 10;
                                        contadorMoscas ++;
                                        break;
                                    case "boss":
                                        moscas.remove(moscas.get(i));
                                        score+= 100;
                                        contadorMoscas ++;
                                        break;
                                }
                            }
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                viewAlreadyTouched = false;
                System.out.println("SOLTADO " + contadorMoscas);

        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();

        p.setTextSize(100);
        canvas.drawText("Score: " + score, 0, 100, p);
        p.setTextSize(150);
        canvas.drawText(tiempo+"", getWidth()/2, 150, p);
        canvas.drawText(ganastePerdiste, 20, getHeight()/2, p);
        try {
            if (moscas.size() > 0) {
                for (Mosca mosca : moscas) {
                    limites(mosca, canvas);
                    mosca.pintar(canvas, p);

                }
            }
        } catch (Exception ignored) {

        }

    }
}
