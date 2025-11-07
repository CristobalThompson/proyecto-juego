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


public class PantallaJuego implements Screen {

    private boolean gameOver = false;
    private boolean switching = false;
    private float startGrace = 0.25f;

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
    private ArrayList<Integer> naveDesbloqueadas;
    private int naveSeleccionada;

	private NaveAbs nave;
	private Nivel nivelActual;


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, ArrayList<Integer> navesDesbloqueadas,
                         int naveSeleccionada) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;
        this.naveDesbloqueadas = navesDesbloqueadas;
        this.naveSeleccionada = naveSeleccionada;

		batch = game.getBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 640);

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));

		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        if (naveSeleccionada == 1) {
            nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }
	    else if (naveSeleccionada == 2){
            nave = new Carguero(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }

        nave.setVidas(vidas);

        Random r = new Random();
	    initLevel(ronda);
	}

    private void initLevel(int ronda){
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

        nivelActual.generarEnemigos();
    }

	public void dibujaEncabezado() {
        CharSequence str = nave.descripcion();
        str = str + " Ronda: " + ronda;
		game.getFont().getData().setScale(2f);
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {

        float dt = Math.min(delta, 1f/60f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        if (!nave.estaHerido()) {
            score += nivelActual.update(dt);
            nivelActual.checkNaveCollision(nave);
        }

        nivelActual.draw(batch);
        nave.draw(batch, this);


	      if (nave.isDestruido()) {
  			if (score > game.getHighScore())
  				game.setHighScore(score);
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      //nivel completado
	      if (nivelActual.isCompleted()) {
              Screen tienda = new PantallaTienda(game, ronda, nave.getVidas(), score,
                  naveDesbloqueadas, naveSeleccionada);
			game.setScreen(tienda);
			dispose();
		  }
	}

    public boolean agregarBala(Disparo bb) {
    	return nivelActual.agregarBala(bb);
    }

    public ArrayList<Ball2> getMeteoritos(){
        if (nivelActual != null) {
            return nivelActual.getEnemigos();
        }
        return new ArrayList<>();
    }

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
