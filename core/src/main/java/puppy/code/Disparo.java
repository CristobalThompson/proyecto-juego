package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Disparo {

    public abstract void update(float dt);

    public abstract void draw(SpriteBatch batch);

    public abstract boolean checkCollision(Ball2 b2);

    public abstract boolean isDestroyed();
}
