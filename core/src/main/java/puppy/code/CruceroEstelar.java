package puppy.code;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CruceroEstelar extends Imperial{
    private float shootTimer = 0;
    private float shootCooldown = 3.0f;

    private boolean disparandoRafaga = false;
    private int balasRestantesRafaga = 0;
    private float tiempoEntreBalasRafaga = 0.2f;
    private float timerInternoRafaga = 0;

    private float summonTimer = 0;
    private float summonCooldown = 5.0f;

    private Texture txBala;
    private Texture txCaza;

    public CruceroEstelar(Texture tx, float x, float y, int vidas, Nivel nivel, NaveAbs jugador, FabricaImperial fi){
        super(tx, x, y, vidas, 0f, nivel, jugador, fi);


        getSprite().setScale(3f);

        float anchoPantalla = Gdx.graphics.getWidth();
        float anchoReal = getSprite().getBoundingRectangle().width;
        getSprite().setX((anchoPantalla - anchoReal) / 2);

        getSprite().setY(y - 50);

        txBala = new Texture("Rocket2.png");
        txCaza = new Texture("MainShip3.png");
    }

    @Override
    public void update(float dt){
        gestionarInvocacion(dt);

        gestionarDisparos(dt);
    }

    private void gestionarInvocacion(float dt){
        summonTimer += dt;
        if (summonTimer >= summonCooldown){
            summonTimer = 0;
            invocarCazas();
        }
    }

    private void invocarCazas(){

        Rectangle rect = getSprite().getBoundingRectangle();

        float ySpawn = rect.y;
        float xIzq = rect.x - 60;
        float xDer = rect.x + rect.width + 10;


        Nivel actual = getNivel();

        Imperial c1 = new CazaTIE(txCaza, 50, ySpawn, 2, actual, getJugador(), 30f, new FabricaBasicaImperial());

        Imperial c2 = new CazaTIE(txCaza, Gdx.graphics.getWidth() - 100, ySpawn,
            2, actual, getJugador(), 30f, new FabricaBasicaImperial());

        actual.agregarNave(c1);
        actual.agregarNave(c2);
    }

    private void gestionarDisparos(float dt){
        if (disparandoRafaga){
            timerInternoRafaga += dt;
            if (timerInternoRafaga >= tiempoEntreBalasRafaga){
                timerInternoRafaga = 0;
                dispararBalaNormal();
                balasRestantesRafaga--;

                if (balasRestantesRafaga <= 0) disparandoRafaga = false;
            }
        }
        else{
            shootTimer += dt;
            if (shootTimer >= shootCooldown){
                shootTimer = 0;
                iniciarNuevaRafaga();
            }
        }
    }

    private void iniciarNuevaRafaga(){
        disparandoRafaga = true;
        balasRestantesRafaga = 3;
        timerInternoRafaga = tiempoEntreBalasRafaga;

        if (MathUtils.random(1, 100) <= 30) dispararMisilGuiado();
    }

    @Override
    public void disparar(){

    }

    private void dispararBalaNormal(){
        Rectangle rect = getSprite().getBoundingRectangle();

        float x = rect.x + rect.width / 2;
        float y = rect.y;

        Disparo bala = new Bullet(x - 5, y, 0, -350f, txBala);
        getNivel().agregarBalaEnemiga(bala);
    }

    private void dispararMisilGuiado(){
        Rectangle rect = getSprite().getBoundingRectangle();
        float x = rect.x + rect.width / 2;
        float y = rect.y;

        //Disparo bala = new GuidedBulletImperial(x, y, 250f, 2.0f, txBala, getJugador());

        Disparo bala = getFabrica().crearMunicion(x, y, 250, getJugador());
        getNivel().agregarBalaEnemiga(bala);
    }
}
