package puppy.code;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public interface EstrategiaNivel {

    int actualizarNivel(float dt, NaveAbs jugador);

    void draw(SpriteBatch batch);

    boolean checkNaveCollision(NaveAbs nave);

    void generarEnemigos(NaveAbs jugador);

    boolean agregarBala(Disparo d);

    ArrayList<Ball2> getEnemigos();      // meteoritos
    ArrayList<Imperial> getNaves();      // naves enemigas

    boolean isJugadorDerrotado();

    boolean isCompleted();

    void dispose();
}
