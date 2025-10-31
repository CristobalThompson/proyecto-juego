package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class NaveAbs{
    private boolean destruida = false;
    private int vidas;
    private float xVel;
    private float yVel;
    private float aceleracion;
    private float velocidadMax;
    private float rozamiento;

    //constructores
    public NaveAbs(int cVidas, float velocidadX, float velocidadY, float ace, float velocidadM, float roce){
        vidas = cVidas;
        xVel = velocidadX;
        yVel = velocidadY;
        aceleracion = ace;
        velocidadMax = velocidadM;
        rozamiento = roce;
    }

    //metodos

    //metodo abstracto de armamento de las naves
    public abstract void armamento();

    public abstract boolean estaHerido();

    public abstract void draw(SpriteBatch batch, PantallaJuego juego);

    public abstract boolean checkCollision(Ball2 b);

    public abstract CharSequence descripcion();

    //METODOS de interacción con los datos.
    public void herir(){
        if (vidas > 0)vidas--;
        if (vidas == 0) destruir();
    }

    public void destruir(){
        destruida = true;
    }

    public void acelerarX(float inicioX, float dtiempo){
        this.xVel += inicioX * aceleracion * dtiempo;
    }

    public void acelerarY(float inicioY, float dtiempo){
        this.yVel += inicioY * aceleracion * dtiempo;
    }

    protected void aplicarRozamiento(float dt) {
        float f = Math.max(0f, 1f - rozamiento * dt);
        xVel *= f;
        yVel *= f;
        if (Math.abs(xVel) < 1f) xVel = 0f;
        if (Math.abs(yVel) < 1f) yVel = 0f;
    }

    protected void limitarVelocidad(){
        float speed = (float)Math.sqrt(xVel*xVel + yVel*yVel);
        if (speed > velocidadMax) {
            float esc = velocidadMax / speed;
            xVel *= esc; yVel *= esc;
        }

    }

    // - - - - - - getters y setters - - - - -
    // getters

    public boolean isDestruido(){
        return destruida;
    }

    public int getVidas(){
        return vidas;
    }

    public float getVelX(){
        return xVel;
    }

    public float getVelY(){
        return yVel;
    }

    public float getAceleracion(){
        return aceleracion;
    }

    public float getVelocidadMax(){
        return velocidadMax;
    }

    public float getRozamiento(){
        return rozamiento;
    }

    //setters

    public void setAceleracion(float aceleracion) {
        this.aceleracion = aceleracion;
    }

    public void setxVel(float nuevaV){
        this.xVel = nuevaV;
    }

    public void setyVel(float nuevaV){
        this.yVel = nuevaV;
    }
}
