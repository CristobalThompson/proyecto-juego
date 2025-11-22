package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class FabricaBasicaImperial implements FabricaImperial{
    private Texture txBala = new Texture("Rocket2.png");

    @Override
    public Disparo crearMunicion(float x, float y, float ySpeed, NaveAbs objetivo){
        return new Bullet(x, y, 0, ySpeed, txBala);
    }
}
