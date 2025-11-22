package puppy.code;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static puppy.code.GestorJuego.getInstancia;

public abstract class Nivel implements EstrategiaNivel{
    //colecciones
    private ArrayList<Ball2> asteroides = new ArrayList<>();
    private ArrayList<Imperial> navesEnemigas = new ArrayList<>();
    private ArrayList<Disparo> balas = new ArrayList<>();
    private ArrayList<Disparo> balasEnemigas = new ArrayList<>();

    //estados internos
    private float spawnTimer = 0f;
    private int navesGeneradas = 0;
    private boolean jugadorDerrotado = false;
    private Random rand = new Random();


    private float spawnDelay;
    private int navesPorGenerar;
    private int vidasCaza;
    private float ySpeedCaza;

    private FabricaImperial fabricaNivel;

    //recursos
    Texture texturaCaza = new Texture("cazaTIE.png");
    private Sound explosionSound;

    public Nivel(float spawnDelay, int navesPorGenerar, int vidasCaza, float ySpeedCaza, FabricaImperial fn){
        this.spawnDelay = spawnDelay;
        this.navesPorGenerar = navesPorGenerar;
        this.vidasCaza = vidasCaza;
        this.ySpeedCaza = ySpeedCaza;
        this.fabricaNivel = fn;

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
    }

    //patron template method :v

    public final void actualizarNivel(float dt, NaveAbs jugador){
        actualizarFisicaYColisiones(dt, jugador);

        if (Evento()){
            EventoEspecial(jugador);
        }
        else{
            generarEnemigosRegulares(dt, jugador);
        }
    }



    private void actualizarFisicaYColisiones(float dt, NaveAbs jugador){
        int puntosGanados = 0;

        for (int i = 0; i < balas.size(); i++) {
            Disparo b = balas.get(i);
            b.update(dt);


            for (int j = 0; j < navesEnemigas.size(); j++) {
                Imperial enemigo = navesEnemigas.get(j);
                if (b.checkCollision(enemigo.getArea())) {
                    enemigo.recibirImpacto();
                    b.setDestroyed(true);

                    if (enemigo.estaDestruida()) {
                        navesEnemigas.remove(j);
                        j--;
                        puntosGanados += 100;
                        explosionSound.play();
                    }
                }
            }

            for (int j = 0; j < asteroides.size(); j++) {
                if (b.checkCollision(asteroides.get(j))) {
                    explosionSound.play();
                    asteroides.remove(j);
                    j--;
                    puntosGanados += 50;
                }
            }
            if (b.isDestroyed()) {
                balas.remove(b);
                i--;
            }
        }

        for (int i = 0; i < navesEnemigas.size(); i++) {
            Imperial enemigo = navesEnemigas.get(i);
            enemigo.update(dt);

            if (enemigo.getArea().getY() <= 0) {
                jugadorDerrotado = true;
                break;
            }
        }

        for (Ball2 ball : asteroides) {
            ball.update(dt);

            for (int i = 0; i < asteroides.size(); i++) {
                Ball2 ball1 = asteroides.get(i);
                for (int j = i + 1; j < asteroides.size(); j++) {
                    Ball2 ball2 = asteroides.get(j);
                    ball1.checkCollision(ball2);
                }
            }
        }

        for (int i = 0; i < balasEnemigas.size(); i++) {
            Disparo b = balasEnemigas.get(i);

            b.update(dt);

            if (jugador.checkCollision(b)) {
                b.setDestroyed(true);
            }

            if (b.isDestroyed()) {
                balasEnemigas.remove(i);
                i--;
            }
        }

        getInstancia().sumarPuntos(puntosGanados);
    }

    public abstract boolean Evento();

    public abstract void EventoEspecial(NaveAbs jugador);

    public void generarEnemigosRegulares(float dt, NaveAbs jugador){
        if (navesGeneradas < navesPorGenerar){
            spawnTimer += dt;
            if (spawnTimer >= spawnDelay){
                spawnTimer = 0;
                solicitarSpawnEnemigo(jugador);
            }
        }
    }


    public final void generarEnemigos(NaveAbs jugador){

        crearAsteroidesIniciales(rand);

        spawnTimer = 0;
        navesGeneradas = 0;
    }

    public abstract void crearAsteroidesIniciales(Random rand);

    public abstract void solicitarSpawnEnemigo(NaveAbs jugador);

    public void draw(SpriteBatch batch){
        for (Disparo b : balas) {
            b.draw(batch);
        }
        for (Ball2 b : asteroides) {
            b.draw(batch);
        }
        for (Imperial n : navesEnemigas){
            n.draw(batch);
        }
        for (Disparo b : balasEnemigas){
            b.draw(batch);
        }
    }

    public boolean checkNaveCollision(NaveAbs nave) {
        boolean colision = false;
        for (int i = 0; i < asteroides.size(); i++) {
            Ball2 b = asteroides.get(i);
            if (nave.checkCollision(b)) {
                asteroides.remove(i);
                i--;
                colision = true;
            }
        }
        return colision;
    }

    public boolean agregarBala(Disparo d) {
        return balas.add(d);
    }

    public boolean agregarBalaEnemiga(Disparo d){
        return balasEnemigas.add(d);
    }


    public boolean isCompleted() {
        return (navesGeneradas >= navesPorGenerar) &&
            navesEnemigas.isEmpty() &&
            asteroides.isEmpty() && condicion();
    }

    public abstract boolean condicion();


    public void dispose() {
        explosionSound.dispose();
    }

    public void addBB(Ball2 bb){
        asteroides.add(bb);
    }

    public void agregarNave(Imperial nave){
        navesEnemigas.add(nave);

        ++navesGeneradas;
    }

    public float generarXAleatoriaSegura(int anchoLogicoNave) {
        int margen = 50;
        int anchoPantalla = Gdx.graphics.getWidth();

        // Calculamos el espacio libre real
        int rangoDisponible = anchoPantalla - (margen * 2) - anchoLogicoNave;

        // PROTECCIÓN: Si la nave es gigante o la pantalla muy chica
        if (rangoDisponible <= 0) {
            rangoDisponible = 1; // Evitamos error de Random
        }

        // Generamos la posición
        return margen + rand.nextInt(rangoDisponible);
    }

    public Rectangle buscarObjetivoMasCercano(float xBala, float yBala){
        Rectangle bestRect = null;
        float bestD2 = Float.MAX_VALUE;

        // 1. Buscar en Asteroides (Ball2)
        for (Ball2 m : asteroides) { // Acceso directo porque estamos en Nivel
            Rectangle r = m.getArea();
            float cx = r.x + r.width * 0.5f;
            float cy = r.y + r.height * 0.5f;

            float dx = cx - xBala;
            float dy = cy - yBala;
            float d2 = dx * dx + dy * dy;

            if (d2 < bestD2) {
                bestD2 = d2;
                bestRect = r;
            }
        }

        // 2. Buscar en Naves Enemigas (Imperial)
        for (Imperial n : navesEnemigas) { // Acceso directo
            Rectangle r = n.getArea();
            float cx = r.x + r.width * 0.5f;
            float cy = r.y + r.height * 0.5f;

            float dx = cx - xBala;
            float dy = cy - yBala;
            float d2 = dx * dx + dy * dy;

            if (d2 < bestD2) {
                bestD2 = d2;
                bestRect = r;
            }
        }

        return bestRect;
    }


    public boolean isJugadorDerrotado(){
        return jugadorDerrotado;
    }
    public Texture getTexturaCaza(){ return texturaCaza;}
    public int getVidasCaza(){ return vidasCaza;}
    public float getSpeedConfig(){ return ySpeedCaza;}
    public int getAnchoPantalla(){ return Gdx.graphics.getWidth();}
    public int getAltoPantalla(){ return Gdx.graphics.getHeight();}
    public Random getRandom(){ return rand;}
    public int getNavesGeneradas(){ return navesGeneradas; }
    public int enemigosTotales(){return navesEnemigas.size();}
    public FabricaImperial getFabrica(){ return fabricaNivel;}

}
