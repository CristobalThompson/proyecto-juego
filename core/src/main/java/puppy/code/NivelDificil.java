package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class NivelDificil extends Nivel{

    public NivelDificil(){
        super();
    }

    @Override
    public void generarEnemigos() {
        // Configuración MEDIA: Más rápidos, más cantidad
        int cantAsteroides = 30;
        int velX = 400;
        int velY = 400;

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
    }
}
