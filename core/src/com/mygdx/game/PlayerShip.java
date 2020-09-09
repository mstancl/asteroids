package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class PlayerShip extends BaseActor {


    private boolean hit;
    private final float originalWidth;
    private final float originalHeight;
    private float timeSinceHit = 0;
    private long idOfExplosionSound;
    private final BaseActor thruster;

    public PlayerShip() {
        super();
        this.setTexture(new Texture(Gdx.files.internal("spaceShip.png")));
        this.setPosition(AsteroidsGame.mainStage.getWidth() / 2, AsteroidsGame.mainStage.getHeight() / 2);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        hit = false;
        originalHeight = this.getHeight();
        originalWidth = this.getWidth();

        thruster = new BaseActor();
        thruster.setTexture(new Texture(Gdx.files.internal("thruster.png")));
        thruster.setWidth(22F);
        thruster.setHeight(0);
        thruster.setPosition(this.getX() - (thruster.getWidth() / 2) + (this.getWidth() / 2), this.getY() - thruster.getHeight());
        thruster.setOrigin(thruster.getWidth() - this.getWidth() / 2, thruster.getHeight() + this.getHeight() / 2);

        AsteroidsGame.mainStage.addActor(this);
        AsteroidsGame.mainStage.addActor(thruster);
    }

    public BaseActor getThruster() {
        return thruster;
    }

    public void hit() {
        this.velocityY = 0;
        this.velocityX = 0;
        this.setTexture(new Texture(Gdx.files.internal("explosion.png")));
        this.setWidth(originalWidth * 1.5F);
        this.setHeight(originalHeight * 1.5F);
        this.setRotation(0);
        hit = true;
    }

    public void explode(Sound explosionSound) {
        if (timeSinceHit == 0) {
            idOfExplosionSound = explosionSound.play(1F);
        }
        timeSinceHit += Gdx.graphics.getDeltaTime();
        setDeltaCounter(getDeltaCounter() + 1);

        if (timeSinceHit < 4F) {
            if (getDeltaCounter() % 2 == 0) {
                if (timeSinceHit < 2) {
                    this.setHeight(this.getHeight() + 5);
                    this.setWidth(this.getWidth() + 5);
                    this.setPosition(getX() - 2.5F, getY() - 2.5F);
                }
            }
            explosionSound.setVolume(idOfExplosionSound, (4F - timeSinceHit) / 4F);
            this.addAction(Actions.alpha((4F - timeSinceHit) / 4F));
        }
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean bool) {
        hit = bool;
        if (!bool) {
            timeSinceHit = 0;
        }
    }


    public float getTimeSinceHit() {
        return timeSinceHit;
    }


}