package puppy.code;

import java.util.ArrayList;

public class GestorJuego {

    // Versión eager: se crea apenas se carga la clase
    private static final GestorJuego INSTANCIA = new GestorJuego();

    private int puntaje;
    private int ronda;
    private int vidas;
    private int probBase;

    private ArrayList<Integer> naveDesbloqueadas;
    private int naveSeleccionada;

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

        naveDesbloqueadas = new ArrayList<>();
        naveDesbloqueadas.add(1);
        naveSeleccionada = 1;
    }

    public void agregarNave(int id){
        if (!naveDesbloqueadas.contains(id)) {
            naveDesbloqueadas.add(id);
        }
    }

    public boolean isNaveDesbloqueada(int id) {
        return naveDesbloqueadas.contains(id);
    }

    public void setNaveSeleccionada(int id) {
        if (naveDesbloqueadas.contains(id)) {
            this.naveSeleccionada = id;
        }
    }

    public int getNaveSeleccionada() { return naveSeleccionada;}

    public void cambiarSiguienteNave() {
        int index = naveDesbloqueadas.indexOf(naveSeleccionada);
        index = (index + 1) % naveDesbloqueadas.size();
        naveSeleccionada = naveDesbloqueadas.get(index);
    }

    public int totalNaves(){ return naveDesbloqueadas.size();}

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
