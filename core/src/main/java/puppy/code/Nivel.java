package puppy.code;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Nivel {
    //colecciones
    private ArrayList<Ball2> asteroides = new ArrayList<>();
    private ArrayList<CazaTIE> navesEnemigas = new ArrayList<>();
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

    //recursos
    Texture texturaCaza = new Texture("MainShip3.png");
    private Sound explosionSound;

    public Nivel(float spawnDelay, int navesPorGenerar, int vidasCaza, float ySpeedCaza){
        this.spawnDelay = spawnDelay;
        this.navesPorGenerar = navesPorGenerar;
        this.vidasCaza = vidasCaza;
        this.ySpeedCaza = ySpeedCaza;

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
    }

    //patron template method :v

    public final int actualizarNivel(float dt, NaveAbs jugador){
        int puntos = 0;

        puntos += actualizarFisicaYColisiones(dt, jugador);

        if (Evento()){
            EventoEspecial(jugador);
        }
        else{
            generarEnemigosRegulares(dt, jugador);
        }

        return puntos;
    }



    private int actualizarFisicaYColisiones(float dt, NaveAbs jugador){
        int puntosGanados = 0;

        // --- 1. LÓGICA DE SPAWN DE NAVES ---
        // Solo generamos si aún quedan naves en la reserva
        if (navesGeneradas < navesPorGenerar) {
            spawnTimer += dt;

            // Si pasó el tiempo, crea una nave nueva
            if (spawnTimer >= spawnDelay) {
                spawnTimer = 0; // Reinicia el contador
                navesGeneradas++;
                solicitarSpawnEnemigo(jugador);
            }
        }
        for (int i = 0; i < balas.size(); i++) {
            Disparo b = balas.get(i);
            b.update(dt);


            for (int j = 0; j < navesEnemigas.size(); j++) {
                CazaTIE enemigo = navesEnemigas.get(j);
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

        // --- 4. LÓGICA DE MOVIMIENTO DE ENEMIGOS (Modificada) ---
        for (int i = 0; i < navesEnemigas.size(); i++) {
            CazaTIE enemigo = navesEnemigas.get(i);
            enemigo.update(dt);

            if (enemigo.getArea().getY() <= 0) {
                jugadorDerrotado = true;
                break;
            }
        }

        // --- 5. LÓGICA DE ASTEROIDES (Igual que antes) ---
        for (Ball2 ball : asteroides) {
            ball.update(dt);

            for (int i = 0; i < asteroides.size(); i++) {
                Ball2 ball1 = asteroides.get(i);
                for (int j = i + 1; j < asteroides.size(); j++) { // Optimización: j = i + 1
                    Ball2 ball2 = asteroides.get(j);
                    ball1.checkCollision(ball2);
                }
            }
        }

        for (int i = 0; i < balasEnemigas.size(); i++) {
            Disparo b = balasEnemigas.get(i);

            // ¡Esta es la línea que las mueve!
            b.update(dt);

            // Comprobar si la bala enemiga choca con el jugador
            if (jugador.checkCollision(b)) {
                b.setDestroyed(true);
                // (La lógica de 'herido' ya debería estar en jugador.checkCollision)
            }

            if (b.isDestroyed()) {
                balasEnemigas.remove(i);
                i--;
            }
        }

        return puntosGanados;
    }

    public abstract boolean Evento();

    public abstract void EventoEspecial(NaveAbs jugador);

    public void generarEnemigosRegulares(float dt, NaveAbs jugador){
        spawnTimer += dt;
        if (spawnTimer >= spawnDelay){
            spawnTimer = 0;
            solicitarSpawnEnemigo(jugador);
        }
    }


    public final void generarEnemigos(NaveAbs jugador){

        crearAsteroidesIniciales(rand);

        spawnTimer = 0;
        navesGeneradas = 0;
    }

    public abstract void crearAsteroidesIniciales(Random rand);

    public abstract void solicitarSpawnEnemigo(NaveAbs jugador);

    public int update(float dt, NaveAbs jugador){
        int puntosGanados = 0;

        // --- 1. LÓGICA DE SPAWN DE NAVES ---
        // Solo generamos si aún quedan naves en la reserva
        if (navesGeneradas < navesPorGenerar) {
            spawnTimer += dt;

            // Si pasó el tiempo, crea una nave nueva
            if (spawnTimer >= spawnDelay) {
                spawnTimer = 0; // Reinicia el contador
                navesGeneradas++;
                solicitarSpawnEnemigo(jugador);
            }
        }
        for (int i = 0; i < balas.size(); i++) {
            Disparo b = balas.get(i);
            b.update(dt);


            for (int j = 0; j < navesEnemigas.size(); j++) {
                CazaTIE enemigo = navesEnemigas.get(j);
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

        // --- 4. LÓGICA DE MOVIMIENTO DE ENEMIGOS (Modificada) ---
        for (int i = 0; i < navesEnemigas.size(); i++) {
            CazaTIE enemigo = navesEnemigas.get(i);
            enemigo.update(dt);

            if (enemigo.getArea().getY() <= 0) {
                jugadorDerrotado = true;
                break;
            }
        }

        // --- 5. LÓGICA DE ASTEROIDES (Igual que antes) ---
        for (Ball2 ball : asteroides) {
            ball.update(dt);

            for (int i = 0; i < asteroides.size(); i++) {
                Ball2 ball1 = asteroides.get(i);
                for (int j = i + 1; j < asteroides.size(); j++) { // Optimización: j = i + 1
                    Ball2 ball2 = asteroides.get(j);
                    ball1.checkCollision(ball2);
                }
            }
        }

        for (int i = 0; i < balasEnemigas.size(); i++) {
            Disparo b = balasEnemigas.get(i);

            // ¡Esta es la línea que las mueve!
            b.update(dt);

            // Comprobar si la bala enemiga choca con el jugador
            if (jugador.checkCollision(b)) {
                b.setDestroyed(true);
                // (La lógica de 'herido' ya debería estar en jugador.checkCollision)
            }

            if (b.isDestroyed()) {
                balasEnemigas.remove(i);
                i--;
            }
        }

        return puntosGanados;
    }

    public void draw(SpriteBatch batch){
        for (Disparo b : balas) {
            b.draw(batch);
        }
        for (Ball2 b : asteroides) {
            b.draw(batch);
        }
        for (CazaTIE n : navesEnemigas){
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
        return (navesGeneradas == navesPorGenerar) &&
            navesEnemigas.isEmpty() &&
            asteroides.isEmpty();
    }


    public void dispose() {
        explosionSound.dispose();
    }

    public void addBB(Ball2 bb){
        asteroides.add(bb);
    }

    public void agregarNave(CazaTIE nave){
        navesEnemigas.add(nave);

        ++navesGeneradas;
    }

    public ArrayList<Ball2> getEnemigos() {
        return asteroides;
    } //cambiar

    public ArrayList<CazaTIE> getNaves(){
        return navesEnemigas;
    } //cambiar

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

}
