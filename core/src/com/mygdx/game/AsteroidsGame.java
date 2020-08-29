package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class AsteroidsGame extends Game {

    public Stage mainStage;
    private BaseActor spaceShip;

    @Override
    public void create() {
        mainStage = new Stage();

        BaseActor floor = new BaseActor();
        floor.setTexture(new Texture(Gdx.files.internal("spaceBackground.png")));
        floor.setPosition(0, 0);
        mainStage.addActor(floor);

        spaceShip = new BaseActor();
        spaceShip.setTexture(new Texture(Gdx.files.internal("spaceShip.png")));
        spaceShip.setPosition(mainStage.getWidth() / 2, mainStage.getHeight() / 2);
        spaceShip.setOrigin(spaceShip.getWidth()/2,spaceShip.getHeight()/2);
        mainStage.addActor(spaceShip);


    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            spaceShip.velocityY += 2;
        
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            spaceShip.velocityY -= 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
          spaceShip.rotateBy(2);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spaceShip.rotateBy(-2);
        }


        spaceShip.moveBy(spaceShip.velocityX * Gdx.graphics.getDeltaTime(), spaceShip.velocityY * Gdx.graphics.getDeltaTime());

        mainStage.act();
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        mainStage.draw();
    }


}
