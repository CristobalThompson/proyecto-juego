package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class GuidedBulletImperial implements Disparo{

    private Sprite spr;
    private NaveAbs target;
    private float speed;
    private float steerStrength;

    private float vx, vy;
    private boolean destroyed = false;
    private float lifeTime = 5.0f;

    public GuidedBulletImperial(float x, float y, float speed, float steerStrength, Texture tx, NaveAbs target){
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);

        this.spr.setColor(1f, 0.3f, 0.3f, 1f);

        this.speed = speed;
        this.steerStrength = steerStrength;
        this.target = target;

        vx = 0;
        vy = -speed;
    }

    @Override
    public void update(float dt){
        if (destroyed) return;

        if (target != null && !target.isDestruido()){
            float bx = spr.getX() + spr.getWidth() / 2;
            float by = spr.getY() + spr.getHeight() / 2;

            float tx = target.getX() + target.getX() / 2;
            float ty = target.getY() + target.getX() / 2;

            float dx = tx - bx;
            float dy = ty - by;

            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len > 0){
                dx /= len;
                dy /= len;

                float targetVx = dx * speed;
                float targetVy = dy * speed;

                float alpha = MathUtils.clamp(steerStrength * dt, 0f, 1f);
                vx = MathUtils.lerp(vx, targetVx, alpha);
                vy = MathUtils.lerp(vy, targetVy, alpha);
            }
        }

        spr.translate(vx * dt, vy * dt);

        float angle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;
        spr.setRotation(angle - 90);

        lifeTime -= dt;
        if (lifeTime <= 0) destroyed = true;

        if (spr.getY() < -500 || spr.getY() > Gdx.graphics.getHeight() + 500)
            destroyed = true;
    }

    @Override
    public void draw(SpriteBatch batch){
        if (!destroyed) spr.draw(batch);
    }

    @Override
    public Rectangle getArea(){
        return spr.getBoundingRectangle();
    }


    @Override
    public boolean checkCollision(Ball2 b2){
        if (!destroyed && spr.getBoundingRectangle().overlaps(b2.getArea())){
            destroyed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCollision(Rectangle area) {
        if (!destroyed && spr.getBoundingRectangle().overlaps(area)){
            destroyed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDestroyed(){
        return destroyed;
    }

    @Override
    public void setDestroyed(boolean condicion){
        destroyed = condicion;
    }
}
