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
    private BaseActor projectile;

    float secondsSinceUpPressed;
    long startSecondsWhenUpPressed;


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
        spaceShip.setOrigin(spaceShip.getWidth() / 2, spaceShip.getHeight() / 2);
        mainStage.addActor(spaceShip);


        projectile = new BaseActor();
        projectile.setTexture(new Texture(Gdx.files.internal("projectile.png")));
        projectile.setPosition(mainStage.getWidth() / 2, mainStage.getHeight() / 2);
        projectile.setVisible(false);
        mainStage.addActor(projectile);


        spaceShip.velocityX = 0;
        spaceShip.velocityY = 0;

    }

    @Override
    public void render() {

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            secondsSinceUpPressed = (System.currentTimeMillis() - startSecondsWhenUpPressed) / 1000F;
            float rotation = spaceShip.getRotation();
            spaceShip.velocityX += -(float) (Math.sin(Math.toRadians(rotation)) * Gdx.graphics.getDeltaTime() * secondsSinceUpPressed);
            spaceShip.velocityY += (float) (Math.cos(Math.toRadians(rotation)) * Gdx.graphics.getDeltaTime() * secondsSinceUpPressed);
        } else {
            startSecondsWhenUpPressed = System.currentTimeMillis();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spaceShip.rotateBy(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spaceShip.rotateBy(-2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            float projectileX = (spaceShip.getX() + spaceShip.getWidth() / 2);
            float projectileY = (spaceShip.getY() + spaceShip.getHeight() / 2);

            projectile.setRotation(spaceShip.getRotation());
            projectile.setPosition(projectileX, projectileY);
            projectile.setVisible(true);

            projectile.velocityX = -(float) (Math.sin(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;
            projectile.velocityY = (float) (Math.cos(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;

        }


        if (spaceShip.getY() + spaceShip.getHeight() < 0) {
            spaceShip.setPosition(spaceShip.getX(), mainStage.getHeight());
        } else if (spaceShip.getY() > mainStage.getHeight()) {
            spaceShip.setPosition(spaceShip.getX(), -spaceShip.getHeight());
        } else if (spaceShip.getX() > mainStage.getWidth()) {
            spaceShip.setPosition(-spaceShip.getWidth(), spaceShip.getY());
        } else if (spaceShip.getX() + spaceShip.getWidth() < 0) {
            spaceShip.setPosition(mainStage.getWidth(), spaceShip.getY());
        }

        spaceShip.moveBy(spaceShip.velocityX, spaceShip.velocityY);

        if (projectile.isVisible()) {
            projectile.moveBy(projectile.velocityX, projectile.velocityY);
        }


        mainStage.act();
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        mainStage.draw();
    }


}
