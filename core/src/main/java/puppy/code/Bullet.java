package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Bullet implements Disparo{

    private float xSpeed;
	private float ySpeed;
	private boolean destroyed = false;
	private Sprite spr;

	    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) {
	    	spr = new Sprite(tx);
	    	spr.setPosition(x, y);
	        this.xSpeed = xSpeed;
	        this.ySpeed = ySpeed;
	    }
        @Override
	    public void update(float dt) {
            spr.translate(xSpeed * dt, ySpeed * dt);

	        //spr.setPosition(spr.getX()+xSpeed, spr.getY()+ySpeed);
	        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > Gdx.graphics.getWidth()) {
	            destroyed = true;
	        }
	        if (spr.getY() < 0 || spr.getY()+spr.getHeight() > Gdx.graphics.getHeight()) {
	        	destroyed = true;
	        }

	    }

        @Override
	    public void draw(SpriteBatch batch) {
	    	spr.draw(batch);
	    }

        @Override
	    public boolean checkCollision(Ball2 b2) {
	        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
	        	// Se destruyen ambos
	            this.destroyed = true;
	            return true;

	        }
	        return false;
	    }

        @Override
        public boolean checkCollision(Rectangle area) {
            if (destroyed) return false; // No choques si ya estás destruida

            if (spr.getBoundingRectangle().overlaps(area)) {
                // La bala choca con el área del enemigo
                this.destroyed = true; // La bala se destruye
                return true;
            }
            return false;
        }

        @Override
	    public boolean isDestroyed() {return destroyed;}

        @Override
        public void setDestroyed(boolean condicion) {
            destroyed = condicion;
        }

}
