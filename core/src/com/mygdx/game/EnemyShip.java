package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Random;

public class EnemyShip extends BaseActor {

    private BaseActor enemyCanon;
    private boolean hit;
    private float originalWidth;
    private float originalHeight;
    private float timeSinceHit = 0;

    public EnemyShip() {
        super();

        Random rn = new Random();
        float velocityX = 50;
        float velocityY = 50;
        float positionX = 0;
        float positionY = 0;

        float randomX = rn.nextInt((int) AsteroidsGame.mainStage.getWidth()) + 1;
        float randomY = rn.nextInt((int) AsteroidsGame.mainStage.getHeight()) + 1;

        switch (rn.nextInt(4) + 1) {
            case 4:
                positionX = AsteroidsGame.mainStage.getWidth() + this.getWidth();
                positionY = randomY;
                velocityX = -velocityX;
                velocityY = 0;
                break;
            case 3:
                positionX = randomX;
                positionY = AsteroidsGame.mainStage.getHeight() + this.getHeight();
                velocityY = -velocityY;
                velocityX = 0;
                break;
            case 2:
                positionX = -this.getWidth();
                positionY = randomY;
                velocityY = 0;
                break;
            case 1:
                positionX = randomX;
                positionY = -this.getHeight();
                velocityX = 0;
                break;
        }

        this.setPosition(positionX, positionY);
        this.setTexture(new Texture(Gdx.files.internal("enemySpaceShip.png")));
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        AsteroidsGame.listOfExistingEnemyShips.add(this);
        AsteroidsGame.mainStage.addActor(this);

        enemyCanon = new BaseActor();
        enemyCanon.setTexture(new Texture(Gdx.files.internal("enemyCanon.png")));
        enemyCanon.setPosition(this.getX() + (this.getWidth() / 2 - enemyCanon.getWidth() / 2), this.getY() + (this.getHeight() / 2 - enemyCanon.getHeight() / 4));
        enemyCanon.velocityX = this.velocityX;
        enemyCanon.velocityY = this.velocityY;
        enemyCanon.setOrigin(enemyCanon.getWidth() / 2, enemyCanon.getHeight() / 4);
        AsteroidsGame.mainStage.addActor(enemyCanon);

        hit = false;
        originalHeight = this.getHeight();
        originalWidth = this.getWidth();

    }

    public BaseActor getEnemyCanon() {
        return enemyCanon;
    }

    public void hit() {
        this.velocityY = 0;
        this.velocityX = 0;
        this.getEnemyCanon().remove();
        this.setTexture(new Texture(Gdx.files.internal("explosion.png")));
        this.setWidth(originalWidth * 1.5F);
        this.setHeight(originalHeight * 1.5F);

        hit = true;
    }

    public void explode() {
        timeSinceHit += Gdx.graphics.getDeltaTime();
        if (timeSinceHit < 4F) {
            this.addAction(Actions.alpha((4F - timeSinceHit) / 4F));
        } else {
            this.remove();
            AsteroidsGame.listOfExistingEnemyShips.remove(this);
        }
    }

    public boolean isHit() {
        return hit;
    }

    public void shoot() {
        BaseActor enemyProjectile = new BaseActor();
        enemyProjectile.setTexture(new Texture(Gdx.files.internal("projectileGreen.png")));

        float projectileX = (this.getX() + this.getWidth() / 2) - (enemyProjectile.getWidth() / 2);
        float projectileY = (this.getY() + this.getHeight() / 2);
        enemyProjectile.setRotation(this.getEnemyCanon().getRotation());
        enemyProjectile.setPosition(projectileX, projectileY);
        AsteroidsGame.mainStage.addActor(enemyProjectile);

        enemyProjectile.velocityX = -(float) (Math.sin(Math.toRadians(enemyProjectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;
        enemyProjectile.velocityY = (float) (Math.cos(Math.toRadians(enemyProjectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;

        AsteroidsGame.listOfEnemyProjectiles.add(enemyProjectile);
    }


}
