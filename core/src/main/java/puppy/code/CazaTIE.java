package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class CazaTIE extends Imperial{

    private float moveTimer;
    private float moveDirection = 1f;
    private float xSpeed = 100f;

    private float shootCooldown;
    private float shootTimer = 0f;


    public CazaTIE(Texture tx, float x, float y, int vidas, Nivel nivel, NaveAbs jugador, float ySpeed){
        super(tx, x, y, vidas, ySpeed, nivel, jugador);

        this.shootCooldown = (float) (Math.random() * 3.0 + 1.0);
    }

    @Override
    public void update(float dt){

        getSprite().translateY(-getySpeed() * dt);

        getSprite().translateX(xSpeed * moveDirection * dt);
        moveTimer += dt;

        if (moveTimer > 2.0f){
            moveDirection *= -1;
            moveTimer = 0;
        }

        shootTimer += dt;
        if (shootTimer >= shootCooldown){
            shootTimer = 0;
            disparar();
        }


//        spr.translateX(speed * moveDirection * dt);
//        moveTimer += dt;
//
//        //IA de movimiento cada 2 segunditos
//        if (moveTimer > 3.0f){
//            moveDirection *= -1;
//            moveTimer = 0;
//        }
//
//        spr.translateY(-ySpeed * dt); // 'y' negativa es hacia abajo
//
//        //IA de disparo
//        shootTimer += dt;
//        if (shootTimer >= shootCooldown){
//            shootTimer = 0;
//            disparar();
//        }
    }

    @Override
    public void disparar(){
        Texture balatx = new Texture("Rocket2.png"); //textura a cambiar

        Disparo bala = new Bullet(getSprite().getX() + getSprite().getWidth() / 2,
            getSprite().getY(), 0, -250f, balatx);

        getNivel().agregarBalaEnemiga(bala);
    }

}
