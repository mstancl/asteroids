package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class AsteroidsGame extends Game {

    public static Stage mainStage;

    private BaseActor spaceShip;
    private BaseActor thruster;
    private EnemyShip enemyShip;

    private BaseActor bigAsteroid;
    private BaseActor mediumAsteroid;
    private BaseActor smallAsteroid;

    private Texture bigAsteroidTexture;
    private Texture mediumAsteroidTexture;
    private Texture smallAsteroidTexture;
    float spaceShipVelocityX;
    float spaceShipVelocityY;

    float futureVelocityX;
    float futureVelocityY;

    float distanceFromCanonX;
    float distanceFromCanonY;
    float canonAngle;

    long timeWhenLastShot;
    float deltaCounter;
    int thrusterCounter = 0;
    boolean isThrusterOn;

    Sound pewSound;
    Sound thrusterSound;

    long thrusterSoundID;


    public static CopyOnWriteArrayList<Asteroid> listOfExistingAsteroids;
    public static CopyOnWriteArrayList<EnemyShip> listOfExistingEnemyShips;

    public static CopyOnWriteArrayList<BaseActor> listOfEnemyProjectiles;
    public static CopyOnWriteArrayList<BaseActor> listOfProjectiles;

    Random rn = new Random();
    int projectileCounter = 0;

    @Override
    public void create() {
        listOfExistingAsteroids = new CopyOnWriteArrayList<>();
        listOfExistingEnemyShips = new CopyOnWriteArrayList<>();
        listOfProjectiles = new CopyOnWriteArrayList<>();
        listOfEnemyProjectiles = new CopyOnWriteArrayList<>();

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

        thruster = new BaseActor();
        thruster.setTexture(new Texture(Gdx.files.internal("thruster.png")));
        thruster.setWidth(22F);
        thruster.setHeight(0);
        thruster.setPosition(spaceShip.getX() - (thruster.getWidth() / 2) + (spaceShip.getWidth() / 2), spaceShip.getY() - thruster.getHeight());
        thruster.setOrigin(thruster.getWidth() - spaceShip.getWidth() / 2, thruster.getHeight() + spaceShip.getHeight() / 2);
        mainStage.addActor(thruster);

        bigAsteroidTexture = new Texture(Gdx.files.internal("bigAsteroid.png"));
        mediumAsteroidTexture = new Texture(Gdx.files.internal("mediumAsteroid.png"));
        smallAsteroidTexture = new Texture(Gdx.files.internal("smallAsteroid.png"));


        pewSound = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
        thrusterSound = Gdx.audio.newSound(Gdx.files.internal("thrusterSound.mp3"));


        spaceShip.velocityX = 0;
        spaceShip.velocityY = 0;

        timeWhenLastShot = System.currentTimeMillis();
        deltaCounter = 0;
        isThrusterOn = false;

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {

                               //generateAsteroids();

                           }
                       }
                , 3        //    (delay)
                , 0.5F     //    (seconds)
        );
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               listOfExistingEnemyShips.add(
                                       new EnemyShip());
                           }
                       }
                , 0        //    (delay)
                , 5F     //    (seconds)
        );
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               for (EnemyShip enemyShip : listOfExistingEnemyShips) {
                                   if (!enemyShip.isHit()) {
                                       enemyShip.shoot();
                                   }
                               }
                           }
                       }
                , 20        //    (delay)
                , 1     //    (seconds)
        );

    }

    @Override
    public void render() {

        deltaCounter += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {

            spaceShipVelocityX = -(float) (Math.sin(Math.toRadians(spaceShip.getRotation())) * Gdx.graphics.getDeltaTime() * 3);
            spaceShipVelocityY = (float) (Math.cos(Math.toRadians(spaceShip.getRotation())) * Gdx.graphics.getDeltaTime() * 3);

            futureVelocityX = spaceShip.velocityX + spaceShipVelocityX;
            futureVelocityY = spaceShip.velocityY + spaceShipVelocityY;

            if (((futureVelocityX * futureVelocityX) + (futureVelocityY * futureVelocityY)) < 7) {
                spaceShip.velocityX += spaceShipVelocityX;
                spaceShip.velocityY += spaceShipVelocityY;

                if (thrusterCounter < 50) {
                    thrusterCounter++;
                    thruster.setHeight(thrusterCounter);
                    thruster.setOrigin(thruster.getWidth() - spaceShip.getWidth() / 2, thruster.getHeight() + spaceShip.getHeight() / 2);
                }

            }
            if (!isThrusterOn) {
                thrusterSoundID = thrusterSound.loop(0.5F);
            }
            isThrusterOn = true;
        } else {
            if (thrusterCounter > 0) {
                thrusterCounter -= 1;
                thruster.setHeight(thrusterCounter);
                thruster.setOrigin(thruster.getWidth() - spaceShip.getWidth() / 2, thruster.getHeight() + spaceShip.getHeight() / 2);
                isThrusterOn = false;

                if (thrusterCounter > 0) {
                    thrusterSound.setVolume(thrusterSoundID, (thrusterCounter / 50F) / 2);
                } else {
                    thrusterSound.stop();

                }


            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spaceShip.rotateBy(4);
            thruster.rotateBy(4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spaceShip.rotateBy(-4);
            thruster.rotateBy(-4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.V)) {

            BaseActor projectile = new BaseActor();
            if (listOfProjectiles.size() % 2 == 0) {
                projectile.setTexture(new Texture(Gdx.files.internal("projectileGreen.png")));
            }
            if (listOfProjectiles.size() % 2 == 1) {
                projectile.setTexture(new Texture(Gdx.files.internal("projectileRed.png")));
            }
            if (listOfProjectiles.size() % 5 == 0 || listOfProjectiles.size() == 1) {
                pewSound.play(1F);
            }

            float projectileX = (spaceShip.getX() + spaceShip.getWidth() / 2) - (projectile.getWidth() / 2);
            float projectileY = (spaceShip.getY() + spaceShip.getHeight() / 2);
            projectile.setRotation(spaceShip.getRotation());
            projectile.setPosition(projectileX, projectileY);
            mainStage.addActor(projectile);

            projectile.velocityX = -(float) (Math.sin(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;
            projectile.velocityY = (float) (Math.cos(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;

            listOfProjectiles.add(projectile);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            for (EnemyShip enemyShip : listOfExistingEnemyShips) {
                enemyShip.shoot();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            if ((System.currentTimeMillis() - timeWhenLastShot) / 1000F > 0.4) {
                BaseActor projectile = new BaseActor();
                if (projectileCounter % 2 == 0) {
                    projectile.setTexture(new Texture(Gdx.files.internal("projectileGreen.png")));
                }
                if (projectileCounter % 2 == 1) {
                    projectile.setTexture(new Texture(Gdx.files.internal("projectileRed.png")));
                }
                if (projectileCounter % 5 == 0 || listOfProjectiles.size() == 1) {
                    pewSound.play(1F);
                }

                float projectileX = (spaceShip.getX() + spaceShip.getWidth() / 2) - (projectile.getWidth() / 2);
                float projectileY = (spaceShip.getY() + spaceShip.getHeight() / 2);
                projectile.setRotation(spaceShip.getRotation());
                projectile.setPosition(projectileX, projectileY);
                mainStage.addActor(projectile);

                projectile.velocityX = -(float) (Math.sin(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;
                projectile.velocityY = (float) (Math.cos(Math.toRadians(projectile.getRotation())) * Gdx.graphics.getDeltaTime()) * 300;

                listOfProjectiles.add(projectile);
                timeWhenLastShot = System.currentTimeMillis();
                projectileCounter++;
            }

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


        for (Asteroid existingAsteroid : listOfExistingAsteroids) {
            for (BaseActor projectile : listOfProjectiles) {
                if (projectile.getBoundingRectangle().overlaps(existingAsteroid.getBoundingRectangle())) {
                    existingAsteroid.hit();
                    projectile.setVisible(false);
                    float projectileX = (spaceShip.getX() + spaceShip.getWidth() / 2) - (projectile.getWidth() / 2);
                    float projectileY = (spaceShip.getY() + spaceShip.getHeight() / 2);
                    projectile.setRotation(spaceShip.getRotation());
                    projectile.setPosition(projectileX, projectileY);
                    listOfExistingAsteroids.remove(existingAsteroid);
                    listOfProjectiles.remove(projectile);
                }
                if (projectile.getX() > mainStage.getWidth() || projectile.getX() < 0 || projectile.getY() > mainStage.getHeight() || projectile.getY() < 0) {
                    listOfProjectiles.remove(projectile);
                }
            }
            if (existingAsteroid.getBoundingRectangle().overlaps(spaceShip.getBoundingRectangle())) {
                System.out.println("YOU LOST");
            }
        }


        if (deltaCounter > 1.0F) {
            deltaCounter = 0;

        }


        for (EnemyShip enemyShip : listOfExistingEnemyShips) {

            if (enemyShip.getX() - 50 > mainStage.getWidth() || enemyShip.getX() + 50 < 0 || enemyShip.getY() - 50 > mainStage.getHeight() || enemyShip.getY() + 50 < 0) {
                listOfExistingEnemyShips.remove(enemyShip);
            }
            for (BaseActor projectile : listOfProjectiles) {
                if (enemyShip.getBoundingRectangle().overlaps(projectile.getBoundingRectangle()) && !enemyShip.isHit()) {
                    enemyShip.hit();
                    //listOfExistingEnemyShips.remove(enemyShip);

                }
            }
            if (enemyShip.isHit()) {
                enemyShip.explode();
            }
            distanceFromCanonX = (enemyShip.getX() + enemyShip.getWidth() / 2) - (spaceShip.getX() + spaceShip.getWidth() / 2);
            distanceFromCanonY = (enemyShip.getY() + enemyShip.getHeight() / 2) - (spaceShip.getY() + spaceShip.getHeight() / 2);
            canonAngle = (float) Math.toDegrees(Math.atan2(distanceFromCanonY, distanceFromCanonX)) + 90F;

            enemyShip.getEnemyCanon().setRotation(canonAngle);
        }


        for (BaseActor listOfProjectile : listOfProjectiles) {
            listOfProjectile.moveBy(listOfProjectile.velocityX, listOfProjectile.velocityY);
        }
        for (BaseActor enemyProjectile : listOfEnemyProjectiles) {
            enemyProjectile.moveBy(enemyProjectile.velocityX, enemyProjectile.velocityY);
        }
        spaceShip.moveBy(spaceShip.velocityX, spaceShip.velocityY);
        thruster.setPosition(spaceShip.getX() - (thruster.getWidth() / 2) + (spaceShip.getWidth() / 2), spaceShip.getY() - thruster.getHeight());

        mainStage.act();
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        mainStage.draw();
    }


    public void generateAsteroids() {

        float velocityX = rn.nextInt(100) + 25;
        float velocityY = rn.nextInt(100) + 25;
        float positionX = 0;
        float positionY = 0;

        float randomX = rn.nextInt((int) mainStage.getWidth()) + 1;
        float randomY = rn.nextInt((int) mainStage.getHeight()) + 1;

        switch (rn.nextInt(4) + 1) {
            case 4:
                positionX = mainStage.getWidth() + bigAsteroidTexture.getWidth();
                positionY = randomY;
                velocityX = -velocityX;
                if (randomY > mainStage.getHeight() / 2)
                    velocityY = -velocityY;
                break;
            case 3:
                positionX = randomX;
                positionY = mainStage.getHeight() + bigAsteroidTexture.getHeight();
                velocityY = -velocityY;
                if (randomX > mainStage.getWidth() / 2)
                    velocityX = -velocityY;
                break;
            case 2:
                positionX = -bigAsteroidTexture.getWidth();
                positionY = randomY;
                if (randomY > mainStage.getHeight() / 2)
                    velocityY = -velocityY;
                break;
            case 1:
                positionX = randomX;
                positionY = -bigAsteroidTexture.getHeight();
                if (randomX > mainStage.getWidth() / 2)
                    velocityX = -velocityY;
                break;
        }

        switch (rn.nextInt(3) + 1) {
            case 3:
                new Asteroid(positionX, positionY, velocityX, velocityY, bigAsteroidTexture, "big");
                break;
            case 2:
                new Asteroid(positionX, positionY, velocityX, velocityY, mediumAsteroidTexture, "medium");
                break;
            case 1:
                new Asteroid(positionX, positionY, velocityX, velocityY, smallAsteroidTexture, "small");
                break;
        }


    }


}
