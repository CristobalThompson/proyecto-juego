package puppy.code;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NivelFacil extends Nivel {

    public NivelFacil(){
        super(2.0f, 10, 2, 30f);
    }

    @Override
    public void crearAsteroidesIniciales(Random rand){
        int cantAsteroides = 5;
        for (int i = 0; i < cantAsteroides; ++i){
            Ball2 bb = new Ball2(
                rand.nextInt(Gdx.graphics.getWidth()),
                150 + rand.nextInt(Gdx.graphics.getHeight() - 200),
                20 + rand.nextInt(10),
                100 + rand.nextInt(2),
                100 + rand.nextInt(2),
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            addBB(bb);
        }
    }

    @Override
    public void solicitarSpawnEnemigo(NaveAbs jugador){
        Texture tex = getTexturaCaza();
        int margen = 50;
        int rangoX  = getAnchoPantalla() - (margen * 2) - tex.getWidth();

        Random r = getRandom();
        float x = r.nextInt(rangoX) + margen;
        float y = getAltoPantalla() - 50;

        CazaTIE enemigo = new CazaTIE(tex, x, y, getVidasCaza(), this, jugador, getSpeedConfig());

        agregarNave(enemigo);

    }

    @Override
    public boolean Evento(){
        return false;
    }

    @Override
    public void EventoEspecial(NaveAbs jugador){ }

    @Override
    public boolean condicion() {
        return true;
    }

}
