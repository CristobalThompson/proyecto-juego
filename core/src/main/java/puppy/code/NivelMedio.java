package puppy.code;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NivelMedio extends Nivel{

    private boolean eventoLluvia = false;

    public NivelMedio(){
        super(1.5f, 12, 3, 35f, new FabricaBasicaImperial());
    }

    @Override
    public void crearAsteroidesIniciales(Random rand){
        // Cantidad intermedia de asteroides
        int cantAsteroides = 8;
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(
                rand.nextInt(getAnchoPantalla()),
                150 + rand.nextInt(getAltoPantalla() - 200),
                20 + rand.nextInt(10),
                150 + rand.nextInt(30), // Velocidad media
                150 + rand.nextInt(30),
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            addBB(bb);
        }
    }

    @Override
    public void solicitarSpawnEnemigo(NaveAbs jugador){
        Texture tex = getTexturaCaza();
        int margen = 50;
        int minX = margen;

        int anchoLogico = 80;
        int maxX = getAnchoPantalla() - margen - anchoLogico;

        float jugadorX = jugador.getX();

        Random r = getRandom();
        float desviacion = r.nextInt(300) - 150;
        float targetX = jugadorX + desviacion;

        if (targetX < minX) targetX = minX;
        if (targetX > maxX) targetX = maxX;

        float y = getAltoPantalla() - 50;

        CazaTIE enemigo = new CazaTIE(tex, targetX, y, getVidasCaza(), this, jugador, getSpeedConfig(), getFabrica());
        agregarNave(enemigo);
    }

    @Override
    public boolean Evento(){
        if (!eventoLluvia && getNavesGeneradas() >= 10){
            return true;
        }
        return false;
    }

    @Override
    public void EventoEspecial(NaveAbs jugador){
        System.out.println("Eventito de lluvia de meteoritos");

        crearAsteroidesIniciales(new Random());

        eventoLluvia = true;
    }

    @Override
    public boolean condicion() {
        return eventoLluvia;
    }
}
