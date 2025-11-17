package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface Disparo {

    public abstract void update(float dt);

    public abstract void draw(SpriteBatch batch);

    public abstract boolean checkCollision(Ball2 b2);

    public abstract boolean checkCollision(Rectangle area);

    public abstract boolean isDestroyed();

    public abstract void setDestroyed(boolean condicion);

    public abstract Rectangle getArea();
}
