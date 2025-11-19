package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class PantallaTienda implements Screen{
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private ArrayList<Integer> navesDesbloqueadas;
    private int naveSeleccionada;

    private final int COSTO_VIDA = 1000;
    private final int COSTO_MEJORA_NAVE = 2000;


    public PantallaTienda(SpaceNavigation game,
                          ArrayList<Integer> navesDesbloqueadas, int naveSeleccionada) {
        this.game = game;
        this.navesDesbloqueadas = navesDesbloqueadas;
        this.naveSeleccionada = naveSeleccionada;

        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        GestorJuego gestor = GestorJuego.getInstancia();
        int scoreActual = gestor.getPuntaje();
        int rondaActual = gestor.getRonda();
        int vidasActuales = gestor.getVidas();

        batch.begin();
        font.getData().setScale(2);
        font.draw(batch, "Base rebelde", 250, 600);
        //font.draw(batch, "Ronda Actual: " + rondaActual, 300, 580);
        font.draw(batch, "Score Disponible: " + scoreActual, 300, 550);
        font.draw(batch, "Vidas Actuales: " + vidasActuales, 300, 500);

        font.getData().setScale(1.5f);
        font.draw(batch, "[1] Comprar Vida (" + COSTO_VIDA + " pts)", 100, 400);

        if (!navesDesbloqueadas.contains(2)) font.draw(batch, "[2] Comprar Carguero (" + COSTO_MEJORA_NAVE + " pts)", 100, 350);
        else font.draw(batch, "[2] Carguero (ADQUIRIDO)", 100, 350);

        String nombreNave = (naveSeleccionada == 1) ? "Nave Basica" : "Carguero"; //con un getter aki seguiria buenas practicas
        font.draw(batch, "[3] Nave Actual: " + nombreNave + " (Pulsa para cambiar)", 100, 300);

        font.draw(batch, "Presiona [ENTER] para continuar al siguiente nivel", 150, 100);
        batch.end();

        manejarInputs();
    }

    private void manejarInputs(){
        GestorJuego gestor = GestorJuego.getInstancia();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (gestor.getPuntaje() >= COSTO_VIDA) {
                gestor.gastarPuntos(COSTO_VIDA);
                gestor.ganarVida();
                // Opcional: sonido de compra
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (!navesDesbloqueadas.contains(2) && gestor.getPuntaje() >= COSTO_MEJORA_NAVE){
                gestor.gastarPuntos(COSTO_MEJORA_NAVE);
                navesDesbloqueadas.add(2);
                naveSeleccionada = 2;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            cambiarNaveSeleccionada();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            irAlSiguienteNivel();
        }
    }

    private void cambiarNaveSeleccionada() {
        int index = navesDesbloqueadas.indexOf(naveSeleccionada);
        index = (index + 1) % navesDesbloqueadas.size();
        naveSeleccionada = navesDesbloqueadas.get(index);
    }

    private void irAlSiguienteNivel() {
        GestorJuego gestor = GestorJuego.getInstancia();
        gestor.siguienteRonda();

        Screen ss = new PantallaJuego(game, /*gestor.getRonda(), gestor.getVidas(), gestor.getPuntaje(),*/ navesDesbloqueadas, naveSeleccionada);
        ss.resize(1200, 800);
        game.setScreen(ss);
        dispose();
    }


    @Override
    public void show() { }
    @Override
    public void resize(int i, int i1) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
