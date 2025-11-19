package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class NivelDificil extends Nivel{

    private boolean intentoBossRealizado = false;
    private boolean jefeSpawneado = false;
    CruceroEstelar jefe = null;

    public NivelDificil(){
        super(1.0f, 15, 4, 45f);
    }

    @Override
    public void crearAsteroidesIniciales(Random rand){
        // Obstáculos específicos de DIFÍCIL (Más asteroides, más rápidos)
        int cantAsteroides = 12;
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(
                rand.nextInt(Gdx.graphics.getWidth()),
                150 + rand.nextInt(Gdx.graphics.getHeight() - 200),
                20 + rand.nextInt(10),
                200 + rand.nextInt(50), // Más rápidos
                200 + rand.nextInt(50),
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            addBB(bb);
        }
    }

    @Override
    public void solicitarSpawnEnemigo(NaveAbs jugador){

        Texture tex = getTexturaCaza();
        int margen = 50;
        int anchoTotal = getAnchoPantalla();

        float x1 = margen + getRandom().nextInt((anchoTotal / 2) - 100);
        float y = getAltoPantalla() - 50;

        float x2 = (anchoTotal / 2) + getRandom().nextInt(anchoTotal / 2 - 100 - margen);

        CazaTIE n1 = new CazaTIE(tex, x1, y, getVidasCaza(), this, jugador, getSpeedConfig());
        CazaTIE n2 = new CazaTIE(tex, x2, y, getVidasCaza(), this, jugador, getSpeedConfig());

        agregarNave(n1);
        agregarNave(n2);
    }

    @Override
    public boolean Evento(){
        if (jefeSpawneado || getNavesGeneradas() <= 12) return false;

        return true; //borrar solo test

//        if (jefeSpawneado) return false;
//
//        if (!intentoBossRealizado && getNavesGeneradas() >= 25){
//            intentoBossRealizado = true;
//
//            Random r = getRandom();
//            if (r.nextInt(100) < 40){
//                return true;
//            }
//        }
//
//        return false;
    }

    @Override
    public void EventoEspecial(NaveAbs jugador){

        System.out.println("Jefesito en camino pe");

        Texture txCrucero = new Texture("MainShip3.png");

        jefe = new CruceroEstelar(txCrucero, getAnchoPantalla()/2 - 100, getAltoPantalla() - 100, 20,
                                                this, jugador);
        agregarNave(jefe);
        jefeSpawneado = true;
    }

    @Override
    public boolean condicion() {
        return true;
    }
}
