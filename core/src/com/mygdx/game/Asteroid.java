package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Asteroid extends BaseActor {

    private boolean hit;
    private final String asteroidType;
    private final Random rn = new Random();

    public Asteroid(float x, float y, float velocityX, float velocityY, Texture texture, String asteroidType) {
        super();
        this.setPosition(x, y);
        this.setTexture(texture);
        this.asteroidType = asteroidType;

        this.velocityX = velocityX;
        this.velocityY = velocityY;

        AsteroidsGame.listOfExistingAsteroids.add(this);
        AsteroidsGame.mainStage.addActor(this);
        hit = false;
    }

    public void hit() {
        if (!hit) {
            hit = true;
            this.remove();
            if (asteroidType.equals("big")) {
                Asteroid mediumAsteroid1 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
                Asteroid mediumAsteroid2 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
                Asteroid mediumAsteroid3 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
            } else if (asteroidType.equals("medium")) {
                Asteroid smallAsteroid1 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
                Asteroid smallAsteroid2 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
                Asteroid smallAsteroid3 = new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
            }
        }
    }


}
