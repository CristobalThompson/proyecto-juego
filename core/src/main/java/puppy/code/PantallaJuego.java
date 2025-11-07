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

	private NaveAbs nave;
	private Nivel nivelActual;


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;

		batch = game.getBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		//explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		//explosionSound.setVolume(1,0.1f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));

		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();

        if (ronda < 3) {
            nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }
	    else {
            nave = new Carguero(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        }

        //nave.setVidas(vidas);
        Random r = new Random();
	    initLevel(ronda);
	}

    private void initLevel(int ronda){
        if (ronda <= 2) {
            nivelActual = new NivelFacil();
        } else if (ronda <= 5) {
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
            nivelActual.update(dt, score);

            if (nivelActual.checkNaveCollision(nave)){

            }
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
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score);
			ss.resize(1200, 800);
			game.setScreen(ss);
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
