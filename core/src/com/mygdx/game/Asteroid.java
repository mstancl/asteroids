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
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
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
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("mediumAsteroid.png")), "medium");
            } else if (asteroidType.equals("medium")) {
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
                new Asteroid(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, rn.nextInt(50) + 30, rn.nextInt(50) + 30, new Texture(Gdx.files.internal("smallAsteroid.png")), "small");
            }
        }
    }


    public String getAsteroidType() {
        return asteroidType;
    }


}
