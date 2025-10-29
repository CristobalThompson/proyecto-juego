package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class DoubleBullet implements Disparo{

    private final Bullet balaDer;
    private final Bullet balaIzq;

    public DoubleBullet(float x, float y, float speed, Texture tx){
        this(x, y, speed, 45f, 135f, tx);
    }

    public DoubleBullet(float x, float y, float speed,
                        float anguloDer, float anguloIzq, Texture tx){
        float offset = 6f;

        float vxR = speed * MathUtils.cosDeg(anguloDer);
        float vyR = speed * MathUtils.sinDeg(anguloDer);
        balaDer = new Bullet(x + offset, y, vxR, vyR, tx);

        float vxL = speed * MathUtils.cosDeg(anguloIzq);
        float vyL = speed * MathUtils.sinDeg(anguloIzq);
        balaIzq = new Bullet(x - offset, y, vxL, vyL, tx);

    }

    @Override
    public void update(float dt) {
        balaDer.update(dt);
        balaIzq.update(dt);
    }

    @Override
    public void draw(SpriteBatch batch) {
        balaDer.draw(batch);
        balaIzq.draw(batch);
    }

    @Override
    public boolean checkCollision(Ball2 b2) {
        boolean hit = false;
        hit |= balaDer.checkCollision(b2);
        hit |= balaIzq.checkCollision(b2);
        return hit;
    }

    @Override
    public boolean isDestroyed() {
        return balaDer.isDestroyed() && balaIzq.isDestroyed();
    }
}
