package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;


public class Nave4 extends NaveAbs{

    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;

    Random r = new Random();


    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(3,0f,0f,1800f,360f,3f);
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;

    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);

    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego){

        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f/60f);

        if (!herido) {
            float x =  spr.getX();
            float y =  spr.getY();
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

	        // que se mueva con teclado
            float ix = 0f, iy = 0f;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  ix -= 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ix += 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  iy -= 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.UP))    iy += 1f;

            if (ix != 0f || iy != 0f) {
                float len = (float)Math.sqrt(ix*ix + iy*iy);
                ix /= len; iy /= len;
                acelerarX(ix,dt);
                acelerarY(iy,dt);
            } else {
                aplicarRozamiento(dt);
            }

            limitarVelocidad();

            //encapsulación pendiente
            float xVel = getVelX();
            float yVel = getVelY();

            float nx = x + xVel * dt;
            float ny = y + yVel * dt;

            if (nx < 0f) { nx = 0f; xVel = 0f; }
            else if (nx + spr.getWidth() > w) { nx = w - spr.getWidth(); xVel = 0f; }
            if (ny < 0f) { ny = 0f; yVel = 0f; }
            else if (ny + spr.getHeight() > h) { ny = h - spr.getHeight(); yVel = 0f; }


            spr.setPosition(nx, ny);
            spr.draw(batch);
        } else {
            float x0 = spr.getX();
            spr.setX(x0 + MathUtils.random(-2f, 2f));
            spr.draw(batch);
            spr.setX(x0);

            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float bx = spr.getX() + spr.getWidth() * 0.5f - txBala.getWidth() * 0.5f;
            float by = spr.getY() + spr.getHeight() - 2f;

            int prob = r.nextInt() % 100;

            Disparo bala = null;

            if (prob <= 25 && prob > 0){
                bala = new GuidedBullet(bx, by, 520f, 1f, 8f, txBala,
                    juego.getMeteoritos(), juego.getEnemigos());
            }
            else{
                bala = new Bullet(bx, by, 0f, 320f, txBala);
            }
            juego.agregarBala(bala);
	        soundBala.play();
        }

    }

    @Override
    public boolean checkCollision(Ball2 b) {
        //encapsulación pendiente
        float xVel = getVelX();
        float yVel = getVelY();

        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            herir();
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCollision(Disparo b){
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            herir();
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            return true;
        }

        return false;
    }

    //public boolean estaDestruido() {return !herido && destruida;}
    @Override
    public boolean estaHerido() {
 	   return herido;
    }

    @Override
    public void armamento(){
        //pendiente
    }

    @Override
    public CharSequence descripcion(){
        return "Vidas: " + getVidas();
    }

    @Override
    public float getX(){ return spr.getX();}

    @Override
    public float getY(){ return spr.getY(); }

}
