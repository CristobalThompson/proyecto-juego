package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static puppy.code.GestorJuego.getInstancia;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Music gameMusic;

	private NaveAbs nave;
	private EstrategiaNivel nivelActual;


	public PantallaJuego(SpaceNavigation game) {
		this.game = game;

        GestorJuego gestor = getInstancia();

		batch = game.getBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 640);

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));

		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        int seleccion = gestor.getNaveSeleccionada();

        if (seleccion == 1) {
            nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("x-wing.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }
	    else if (seleccion == 2){
            nave = new Carguero(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("fantasma.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }

        initLevel(gestor.getRonda(), nave);
	}

    private void initLevel(int ronda, NaveAbs nave){
        Random r = new Random();
        int prob = r.nextInt(100);

        int chanceFacil, chanceMedio;

        if (ronda <= 3) {
            //Rondas iniciales: Mayor probabilidad de fácil
            chanceFacil = 70; // 70% Fácil
            chanceMedio = 95; // 25% Medio (95 - 70), 5% Difícil (resto)
        } else if (ronda <= 6) {
            //  medias: Mayor probabilidad de medio
            chanceFacil = 30; // 30% Fácil
            chanceMedio = 80; // 50% Medio, 20% Difícil
        } else {
            //Rondas avanzadas: Mayor probabilidad de difícil
            chanceFacil = 10; // 10% Fácil
            chanceMedio = 40; // 30% Medio, 60% Difícil
        }

        if (prob < chanceFacil) {
            nivelActual = new NivelFacil();
        } else if (prob < chanceMedio) {
            nivelActual = new NivelMedio();
        } else {
            nivelActual = new NivelDificil();
        }

        nivelActual.generarEnemigos(nave);
    }

	public void dibujaEncabezado() {
        GestorJuego gestor = getInstancia();

        CharSequence str = nave.descripcion();
        str = str + " Ronda: " + gestor.getRonda();
		game.getFont().getData().setScale(2f);
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+ gestor.getPuntaje(), Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {

        float dt = Math.min(delta, 1f/60f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();

        GestorJuego gestor = getInstancia();

        if (!nave.estaHerido()) {
            nivelActual.actualizarNivel(dt, nave);
            nivelActual.checkNaveCollision(nave);
        }

        nivelActual.draw(batch);
        nave.draw(batch, this);


	      if (nave.isDestruido() || nivelActual.isJugadorDerrotado()) {
  			if (gestor.getPuntaje() > game.getHighScore())
  				game.setHighScore(gestor.getPuntaje());
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      //nivel completado
	      if (nivelActual.isCompleted()) {
              Screen tienda = new PantallaTienda(game);
			game.setScreen(tienda);
			dispose();
		  }
	}

    public boolean agregarBala(Disparo bb) {
    	return nivelActual.agregarBala(bb);
    }

    public EstrategiaNivel getNivel(){ return nivelActual;}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.gameMusic.dispose();
        if (nivelActual != null){
            nivelActual.dispose();
        }
	}
}
