package puppy.code;

public class GestorJuego {

    // Versión eager: se crea apenas se carga la clase
    private static final GestorJuego INSTANCIA = new GestorJuego();

    private int puntaje;
    private int ronda;
    private int vidas;
    private int probBase;

    // Constructor privado
    private GestorJuego() {
        reiniciar();
    }

    // Punto de acceso global
    public static GestorJuego getInstancia() {
        return INSTANCIA;
    }

    public void reiniciar() {
        puntaje = 0;
        ronda = 1;
        vidas = 3;
        probBase = 40;
    }

    public void sumarPuntos(int cantidad) {
        puntaje += cantidad;
    }

    public void agregarProb(){ probBase += 20;}

    public void reiniciarProb(){probBase = 40;}

    public void perderVida() {
        vidas--;
    }

    public void ganarVida(){
        vidas++;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getRonda() {
        return ronda;
    }

    public int getProbBase(){return probBase;}

    public void siguienteRonda() {
        ronda++;
    }

    public int getVidas() {
        return vidas;
    }

    public int gastarPuntos(int gasto){
        puntaje -= gasto;
        return puntaje;
    }
}
