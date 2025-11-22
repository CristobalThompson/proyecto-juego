package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class FabricaEliteImperial implements FabricaImperial{
    private Texture txMisil = new Texture("Rocket2.png");

    @Override
    public Disparo crearMunicion(float x, float y, float ySpeed, NaveAbs objetivo){
        return new GuidedBulletImperial(x, y, ySpeed, 2.0f, txMisil, objetivo);
    }
}
