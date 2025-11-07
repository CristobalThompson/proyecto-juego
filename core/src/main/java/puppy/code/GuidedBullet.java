package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class GuidedBullet implements Disparo{
    private final Sprite spr;
    private final ArrayList<Ball2> objetivos;
    private final float speed;
    private final float steer;
    private float homingLeft;

    private float vx, vy;
    private boolean destroyed = false;

    public GuidedBullet(float x, float y, float speed, float homingSeconds, float steerStrength,
                        Texture tx, ArrayList<Ball2> objetivos) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);

        this.speed = speed;
        this.homingLeft = homingSeconds;
        this.steer = steerStrength;
        this.objetivos = objetivos;

        vx = 0f;
        vy = speed;
    }


    @Override
    public void update(float dt) {
        if (destroyed) return;

        if (homingLeft > 0f){
            Ball2 target = buscarMasCercano();
            if (target != null){
                float bx = spr.getX() + spr.getWidth() * 0.5f;
                float by = spr.getY() + spr.getHeight() * 0.5f;

                Rectangle tb = target.getArea();
                float tx = tb.x + tb.width * 0.5f;
                float ty = tb.y + tb.height * 0.5f;

                float dx = tx - bx;
                float dy = ty - by;
                float len = (float) Math.sqrt(dx * dx + dy * dy);
                if (len > 1e-4f){
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

        //rotar bala
        float ang = MathUtils.atan2(vx, vy) * MathUtils.radiansToDegrees;
        spr.setRotation(ang - 90f);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        if (ny > h || ny + spr.getHeight() < 0 || nx + spr.getWidth() < 0 || nx > w){
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

    private Ball2 buscarMasCercano(){
        if (objetivos == null || objetivos.size() == 0) return null;

        float bx = spr.getX() + spr.getWidth() * 0.5f;
        float by = spr.getY() + spr.getHeight() * 0.5f;

        Ball2 best = null;
        float bestD2 = Float.MAX_VALUE;
        for (int i = 0; i < objetivos.size(); i++){
            Ball2 m = objetivos.get(i);
            Rectangle r = m.getArea();

            float cx = r.x + r.width * 0.5f;
            float cy = r.y + r.height * 0.5f;
            float dx = cx - bx, dy = cy - by;
            float d2 = dx * dx + dy * dy;
            if (d2 < bestD2){
                bestD2 = d2;
                best = m;
            }
        }
        return best;
    }
}
