package puppy.code;

import java.util.*;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static puppy.code.Carguero.getCarguero;
import static puppy.code.Nave4.getNave4;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides;
	private int velYAsteroides;
	private int cantAsteroides;

	private NaveAbs nave;
	/*private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Ball2> balls2 = new ArrayList<>();*/
    private final List<Ball2> meteoritos = new ArrayList<>();
    private final List<Disparo> balas     = new ArrayList<>();
	//private  List<Disparo> balas = new ArrayList<>();


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;

		batch = game.getBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.1f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));

		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();

        if (ronda < 3) nave = getNave4(); // cargar imagen de la nave, 64x64
	    else nave = getCarguero(); // cargar imagen del carguero, 64x64

        //nave.setVidas(vidas);
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(
                r.nextInt((int)Gdx.graphics.getWidth()),
                50 + r.nextInt((int)Gdx.graphics.getHeight() - 50),
                20 + r.nextInt(10),
                velXAsteroides + r.nextInt(4),
                velYAsteroides + r.nextInt(4),
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            meteoritos.add(bb);
        }
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
            // 3.1 Balas: update, colisión con meteoritos y borrado seguro
            for (Iterator<Disparo> itB = balas.iterator(); itB.hasNext();) {
                Disparo b = itB.next();
                b.update(dt);

                for (Iterator<Ball2> itM = meteoritos.iterator(); itM.hasNext();) {
                    Ball2 m = itM.next();
                    if (b.checkCollision(m)) {
                        explosionSound.play();
                        itM.remove();     // ← borra meteorito de forma segura
                        score += 10;
                    }
                }

                if (b.isDestroyed()) itB.remove(); // ← borra bala de forma segura
            }

            // 3.2 Meteoritos: mover
            for (Ball2 m : meteoritos) m.update(dt);

            // 3.3 Colisiones meteorito–meteorito (una sola lista, j = i+1)
            for (int i = 0; i < meteoritos.size(); i++) {
                Ball2 a = meteoritos.get(i);
                for (int j = i + 1; j < meteoritos.size(); j++) {
                    Ball2 b = meteoritos.get(j);
                    a.checkCollision(b);
                }
            }
        }

        // 3.4 Dibujar balas
        for (Disparo b : balas) b.draw(batch);

        // 3.5 Dibujar nave
        nave.draw(batch, this);

        // 3.6 Dibujar meteoritos + choque con nave (borrado seguro)
        for (Iterator<Ball2> itM = meteoritos.iterator(); itM.hasNext();) {
            Ball2 m = itM.next();
            m.draw(batch);
            if (nave.checkCollision(m)) {
                itM.remove(); // ← quitar si golpea a la nave
            }
        }

        // 3.7 Fin de juego
        if (nave.isDestruido()) {
            if (score > game.getHighScore()) game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        batch.end();

        // 3.8 Nivel completado
        if (meteoritos.isEmpty()) {
            Screen ss = new PantallaJuego(
                game, ronda + 1, nave.getVidas(), score,
                velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10
            );
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }


    /*@Override
	public void render(float delta) {

        float dt = Math.min(delta, 1f/60f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        if (!nave.estaHerido()) {
            // colisiones entre balas y asteroides y su destruccion
            for (int i = 0; i < balas.size(); i++) {
                Disparo b = balas.get(i);
                b.update(dt);
                for (int j = 0; j < balls1.size(); j++) {
                    if (b.checkCollision(balls1.get(j))) {
                        explosionSound.play();
                        balls1.remove(j);
                        balls2.remove(j);
                        j--;
                        score +=10;
		              }
		  	        }

                //   b.draw(batch);
                if (b.isDestroyed()) {
                    balas.remove(b);
                    i--; //para no saltarse 1 tras eliminar del arraylist
                }
            }
            //actualizar movimiento de asteroides dentro del area
            for (Ball2 ball : balls1) {
                ball.update(dt);
            }
            //colisiones entre asteroides y sus rebotes
            for (int i=0;i<balls1.size();i++) {
                Ball2 ball1 = balls1.get(i);
		        for (int j=0;j<balls2.size();j++) {
                    Ball2 ball2 = balls2.get(j);
		            if (i<j) {
                        ball1.checkCollision(ball2);
		            }
		        }
            }
        }
	      //dibujar balas
	     for (Disparo b : balas) {
	          b.draw(batch);
	      }
	      nave.draw(batch, this);
	      //dibujar asteroides y manejar colision con nave
	      for (int i = 0; i < balls1.size(); i++) {
	    	    Ball2 b=balls1.get(i);
	    	    b.draw(batch);
		          //perdió vida o game over
	              if (nave.checkCollision(b)) {
		            //asteroide se destruye con el choque
	            	 balls1.remove(i);
	            	 balls2.remove(i);
	            	 i--;
              }
  	        }

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
	      if (balls1.size()==0) {
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score,
					velXAsteroides+3, velYAsteroides+3, cantAsteroides+10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		  }

	}*/

    public boolean agregarBala(Disparo bb) {
    	return balas.add(bb);
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
		this.explosionSound.dispose();
		this.gameMusic.dispose();
	}

    public List<Ball2> getMeteoritos(){
        //return balls1;
        return Collections.unmodifiableList(meteoritos);
    }
}
