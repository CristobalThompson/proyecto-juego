package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static puppy.code.GestorJuego.getInstancia;

public class PantallaTienda implements Screen{
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private final int COSTO_VIDA = 1000;
    private final int COSTO_MEJORA_NAVE = 2000;


    public PantallaTienda(SpaceNavigation game) {
        this.game = game;
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

        GestorJuego gestor = getInstancia();

        batch.begin();
        font.getData().setScale(2);
        font.draw(batch, "Base rebelde", 250, 600);
        font.draw(batch, "Score Disponible: " + gestor.getPuntaje(), 300, 550);
        font.draw(batch, "Vidas Actuales: " + gestor.getVidas(), 300, 500);

        font.getData().setScale(1.5f);
        font.draw(batch, "[1] Comprar Vida (" + COSTO_VIDA + " pts)", 100, 400);

        // Verificamos si YA tiene la nave 2 desbloqueada
        if (!gestor.isNaveDesbloqueada(2)) {
            font.draw(batch, "[2] Comprar Carguero (" + COSTO_MEJORA_NAVE + " pts)", 100, 350);
        } else {
            font.draw(batch, "[2] Carguero (ADQUIRIDO)", 100, 350);
        }

        String nombreNave = (gestor.getNaveSeleccionada() == 1) ? "Nave Basica" : "Carguero";
        font.draw(batch, "[3] Nave Actual: " + nombreNave + " (Pulsa para cambiar)", 100, 300);

        font.draw(batch, "Presiona [ENTER] para continuar al siguiente nivel", 150, 100);
        batch.end();

        manejarInputs();
    }

    private void manejarInputs(){
        GestorJuego gestor = getInstancia();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (gestor.getPuntaje() >= COSTO_VIDA) {
                gestor.gastarPuntos(COSTO_VIDA);
                gestor.ganarVida();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (!gestor.isNaveDesbloqueada(2) && gestor.getPuntaje() >= COSTO_MEJORA_NAVE){
                gestor.gastarPuntos(COSTO_MEJORA_NAVE);

                gestor.agregarNave(2);
                gestor.setNaveSeleccionada(2);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            gestor.cambiarSiguienteNave();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            irAlSiguienteNivel();
        }
    }

    private void irAlSiguienteNivel() {
        GestorJuego gestor = getInstancia();
        gestor.siguienteRonda();

        // Constructor simplificado, ya no pasamos listas ni ints
        Screen ss = new PantallaJuego(game);
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
