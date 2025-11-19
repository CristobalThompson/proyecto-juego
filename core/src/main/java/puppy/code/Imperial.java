package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Imperial {
    private Sprite spr;
    private int vidas;
    private float ySpeed;

    private Nivel nivel;
    private NaveAbs jugador;

    public Imperial(Texture tx, float x, float y, int vidas, float ySpeed, Nivel nivel, NaveAbs jugador){
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.vidas = vidas;
        this.ySpeed = ySpeed;
        this.nivel = nivel;
        this.jugador = jugador;
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void recibirImpacto() {
        vidas--;
    }

    public boolean estaDestruida() {
        return vidas <= 0;
    }

    public abstract void update(float dt);

    public abstract void disparar();

    public Sprite getSprite(){ return spr;}
    public float getySpeed(){ return ySpeed;}
    public NaveAbs getJugador(){ return jugador;}
    public Nivel getNivel(){ return nivel; }
}
