package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class NivelDificil extends Nivel{

    public NivelDificil(){
        super(1.0f, 25, 5, 60f);
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
    }

    @Override
    public void spawnCazaTIE(NaveAbs jugador, Texture tx){
        Random rand = new Random();
        int anchoNave = tx.getWidth();

        int margen = 50;

        int rangoX = Gdx.graphics.getWidth() - (margen * 2) - anchoNave;

        float x = rand.nextInt(rangoX) + margen;
        float y = Gdx.graphics.getHeight() - 50; // Cerca del borde superior

        agregarNave(new CazaTIE(tx, x, y, getVidasCaza(), this, jugador, getYSpeedCaza()));
    }
}
