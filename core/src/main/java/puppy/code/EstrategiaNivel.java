package puppy.code;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public interface EstrategiaNivel {

    void actualizarNivel(float dt, NaveAbs jugador);

    void draw(SpriteBatch batch);

    boolean checkNaveCollision(NaveAbs nave);

    void generarEnemigos(NaveAbs jugador);

    boolean agregarBala(Disparo d);

    boolean isJugadorDerrotado();

    boolean isCompleted();

    void dispose();

    Rectangle buscarObjetivoMasCercano(float xBala, float yBala);
}
