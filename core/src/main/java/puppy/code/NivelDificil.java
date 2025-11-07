package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class NivelDificil extends Nivel{

    public NivelDificil(){
        super();
    }

    @Override
    public void generarEnemigos(NaveAbs jugador) {
        // Configuración MEDIA: Más rápidos, más cantidad
        int cantAsteroides = 10;
        int velX = 200;
        int velY = 200;

        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(
                r.nextInt((int) Gdx.graphics.getWidth()),
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
        agregarNave(new CazaTIE(textEnemigo, 350, 700, 5, this, jugador));
    }
}
