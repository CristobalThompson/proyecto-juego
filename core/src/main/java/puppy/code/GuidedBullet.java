package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class GuidedBullet implements Disparo{

    private final Sprite spr;
    private final float speed;
    private final float steer;
    private float homingLeft;
    private final EstrategiaNivel nivel;

    private float vx, vy;
    private boolean destroyed = false;

    public GuidedBullet(float x, float y, float speed, float homingSeconds, float steerStrength,
                        Texture tx, EstrategiaNivel nivel) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 15, 25);

        this.speed = speed;
        this.homingLeft = homingSeconds;
        this.steer = steerStrength;
        this.nivel = nivel;

        vx = 0f;
        vy = speed;
    }


    @Override
    public void update(float dt) {
        if (destroyed) return;

        if (homingLeft > 0f) {
            float bx = spr.getX() + spr.getWidth() * 0.5f;
            float by = spr.getY() + spr.getHeight() * 0.5f;

            Rectangle targetRect = nivel.buscarObjetivoMasCercano(bx, by);

            if (targetRect != null) {
                float tx = targetRect.x + targetRect.width * 0.5f;
                float ty = targetRect.y + targetRect.height * 0.5f;
                float dx = tx - bx;
                float dy = ty - by;
                float len = (float) Math.sqrt(dx * dx + dy * dy);

                if (len > 1e-4f) {
                    dx /= len; dy /= len;
                    float dvx = dx * speed;
                    float dvy = dy * speed;
                    float alpha = MathUtils.clamp(steer * dt, 0f, 1f);
                    vx = MathUtils.lerp(vx, dvx, alpha);
                    vy = MathUtils.lerp(vy, dvy, alpha);
                }
            }
            homingLeft -= dt;
        }

        float nx = spr.getX() + vx * dt;
        float ny = spr.getY() + vy * dt;
        spr.setPosition(nx, ny);
        float ang = MathUtils.atan2(vx, vy) * MathUtils.radiansToDegrees;
        spr.setRotation(ang - 90f);

        if (ny > Gdx.graphics.getHeight() || ny + spr.getHeight() < 0 ||
            nx + spr.getWidth() < 0 || nx > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!destroyed) spr.draw(batch);
    }

    @Override
    public boolean checkCollision(Ball2 b2) {
        if (destroyed) return false;
        Rectangle a = spr.getBoundingRectangle();

        Rectangle b = b2.getArea();
        if (a.overlaps(b)){
            destroyed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCollision(Rectangle area) {
        if (destroyed) return false;

        Rectangle a = spr.getBoundingRectangle();

        if (a.overlaps(area)) {
            destroyed = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void setDestroyed(boolean condicion) {
        destroyed = condicion;
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
}
