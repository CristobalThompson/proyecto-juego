package puppy.code;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Nivel {
    private ArrayList<Ball2> asteroides = new ArrayList<>();
    private ArrayList<Disparo> balas = new ArrayList<>();
    private Sound explosionSound;

    public Nivel(){
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
    }

    public abstract void generarEnemigos();

    public void update(float dt, int score){
        for (int i = 0; i < balas.size(); i++) {
            Disparo b = balas.get(i);
            b.update(dt);
            for (int j = 0; j < asteroides.size(); j++) {
                if (b.checkCollision(asteroides.get(j))) {
                    explosionSound.play();
                    asteroides.remove(j);
                    j--;
                }
            }
            if (b.isDestroyed()) {
                balas.remove(b);
                i--;
            }
        }

        for (Ball2 ball : asteroides) {
            ball.update(dt);
        }

        for (int i = 0; i < asteroides.size(); i++) {
            Ball2 ball1 = asteroides.get(i);
            for (int j = i + 1; j < asteroides.size(); j++) { // Optimización: j = i + 1
                Ball2 ball2 = asteroides.get(j);
                ball1.checkCollision(ball2);
            }
        }
    }

    public void draw(SpriteBatch batch){
        for (Disparo b : balas) {
            b.draw(batch);
        }
        for (Ball2 b : asteroides) {
            b.draw(batch);
        }
    }

    public boolean checkNaveCollision(NaveAbs nave) {
        boolean colision = false;
        for (int i = 0; i < asteroides.size(); i++) {
            Ball2 b = asteroides.get(i);
            if (nave.checkCollision(b)) {
                asteroides.remove(i);
                i--;
                colision = true;
            }
        }
        return colision;
    }

    public boolean agregarBala(Disparo d) {
        return balas.add(d);
    }

    public boolean isCompleted() {
        return asteroides.isEmpty();
    }

    public int getPuntosGanadosEnFrame() {
        return 0;
    }

    public void dispose() {
        explosionSound.dispose();
    }

    public void addBB(Ball2 bb){
        asteroides.add(bb);
    }

    public ArrayList<Ball2> getEnemigos() {
        return asteroides;
    }

}
