package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Random;

import static puppy.code.GestorJuego.getInstancia;

public class NivelDificil extends Nivel{

    private boolean intentoBossRealizado = false;
    private boolean jefeSpawneado = false;
    CruceroEstelar jefe = null;

    public NivelDificil(){
        super(1.0f, 16, 3, 40f, new FabricaBasicaImperial());
    }

    @Override
    public void crearAsteroidesIniciales(Random rand){
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
        int anchoLogico = 80;
        float y = getAltoPantalla() - 50;

        float x1 = generarXAleatoriaSegura(anchoLogico);
        float x2 = generarXAleatoriaSegura(anchoLogico);

        CazaTIE n1 = new CazaTIE(tex, x1, y, getVidasCaza(), this, jugador, getSpeedConfig(), getFabrica());
        CazaTIE n2 = new CazaTIE(tex, x2, y, getVidasCaza(), this, jugador, getSpeedConfig(), getFabrica());

        agregarNave(n1);
        agregarNave(n2);

        if (getRandom().nextInt(100) < 20){
            CazaTIE n3 = new CazaTIE(tex, x1, y, getVidasCaza(), this, jugador, getSpeedConfig(), new FabricaEliteImperial());
            agregarNave(n3);
        }
    }

    @Override
    public boolean Evento(){

        if (jefeSpawneado) return false;

        if (!intentoBossRealizado && getNavesGeneradas() >= 15 && enemigosTotales() == 0){
            intentoBossRealizado = true;

            Random r = getRandom();
            if (r.nextInt(100) < getInstancia().getProbBase()) {
                getInstancia().reiniciarProb();
                return true;
            }

            getInstancia().agregarProb();
        }
        return false;
    }

    @Override
    public void EventoEspecial(NaveAbs jugador){

        System.out.println("Jefesito en camino pe");

        Texture txCrucero = new Texture("MainShip3.png");

        jefe = new CruceroEstelar(txCrucero, getAnchoPantalla()/2 - 100, getAltoPantalla() - 100, 50,
                                                this, jugador, new FabricaEliteImperial());
        agregarNave(jefe);
        jefeSpawneado = true;
    }

    @Override
    public boolean condicion() {
        return intentoBossRealizado;
    }
}
