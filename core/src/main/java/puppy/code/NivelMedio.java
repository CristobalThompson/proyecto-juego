package puppy.code;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NivelMedio extends Nivel{

    public NivelMedio(){
        super();
    }

    @Override
    public void generarEnemigos(NaveAbs jugador) {
        int cantAsteroides = 7;
        int velX = 150;
        int velY = 150;

        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(
                r.nextInt((int)Gdx.graphics.getWidth()),
                50 + r.nextInt((int)Gdx.graphics.getHeight() - 50),
                20 + r.nextInt(10),
                velX + r.nextInt(4),
                velY + r.nextInt(4),
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            addBB(bb);
        }
        Texture textEnemigo = new Texture("MainShip3.png");

        agregarNave(new CazaTIE(textEnemigo, 200, 700, 5, this, jugador));
        agregarNave(new CazaTIE(textEnemigo, 600, 700, 5, this, jugador));

    }
}
