package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class CazaTIE {
    private Sprite spr;
    private int vidas;

    private float ySpeed;
    private float moveTimer;
    private float moveDirection = 1f;
    private float speed = 100f;

    private float shootCooldown;
    private float shootTimer = 0f;

    private Nivel nivel;
    private NaveAbs jugador;

    public CazaTIE(Texture tx, float x, float y, int vidas, Nivel nivel, NaveAbs jugador, float ySpeed){
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.vidas = vidas;
        this.nivel = nivel;
        this.jugador = jugador;
        this.ySpeed = ySpeed;

        this.shootCooldown = (float) (Math.random() * 3.0 + 1.0);
    }

    public void update(float dt){
        spr.translateX(speed * moveDirection * dt);
        moveTimer += dt;

        //IA de movimiento cada 2 segunditos
        if (moveTimer > 3.0f){
            moveDirection *= -1;
            moveTimer = 0;
        }

        spr.translateY(-ySpeed * dt); // 'y' negativa es hacia abajo

        //IA de disparo
        shootTimer += dt;
        if (shootTimer >= shootCooldown){
            shootTimer = 0;
            disparar();
        }
    }

    private void disparar(){
        Texture balatx = new Texture("Rocket2.png"); //textura a cambiar

        Disparo bala = new Bullet(spr.getX() + spr.getWidth() / 2, spr.getY(), 0, -250f, balatx);

        nivel.agregarBalaEnemiga(bala);
    }


    public void draw(SpriteBatch batch){
        spr.draw(batch);
    }

    public Rectangle getArea(){
        return spr.getBoundingRectangle();
    }

    public void recibirImpacto(){
        --vidas;
    }

    public boolean estaDestruida(){
        return vidas <= 0;
    }


}
