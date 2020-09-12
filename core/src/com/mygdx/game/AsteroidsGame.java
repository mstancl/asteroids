package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class AsteroidsGame extends Game {

    public static Stage mainStage;

    private PlayerShip spaceShip;

    private BaseActor gameOverSign;
    private BaseActor pressEnterToPlayAgainSign;

    private BaseActor firstIndexScoreActor;
    private BaseActor secondIndexScoreActor;
    private BaseActor thirdIndexScoreActor;
    private BaseActor fourthIndexScoreActor;
    private BaseActor fifthIndexScoreActor;

    private Texture bigAsteroidTexture;
    private Texture mediumAsteroidTexture;
    private Texture smallAsteroidTexture;

    private Texture numberZero;
    private Texture numberOne;
    private Texture numberTwo;
    private Texture numberThree;
    private Texture numberFour;
    private Texture numberFive;
    private Texture numberSix;
    private Texture numberSeven;
    private Texture numberEight;
    private Texture numberNine;

    float spaceShipVelocityX;
    float spaceShipVelocityY;

    float futureVelocityX;
    float futureVelocityY;

    float distanceFromCanonX;
    float distanceFromCanonY;
    float canonAngle;

    float timeSinceStart = 0F;
    float timeSinceLastAsteroidGenerated = 0F;
    float difficulty = 5.0F;
    long timeWhenLastShot;
    int thrusterCounter = 0;
    boolean isThrusterOn;

    Music mainBackgroundMusic;
    Sound pewSound;
    Sound thrusterSound;
    Sound explosionSound;

    long thrusterSoundID;

    Map<BaseActor, Integer> mapOfIndexesWithScore = new HashMap<>();
    int fifthIndexScore;
    int fourthIndexScore;
    int thirdIndexScore;
    int secondIndexScore;
    int firstIndexScore;

    public static CopyOnWriteArrayList<Asteroid> listOfExistingAsteroids;
    public static CopyOnWriteArrayList<EnemyShip> listOfExistingEnemyShips;

    public static CopyOnWriteArrayList<BaseActor> listOfEnemyProjectiles;
    public static CopyOnWriteArrayList<BaseActor> listOfProjectiles;

    Random rn = new Random();
    int projectileCounter = 0;

    long playerScore = 0;

    @Override
    public void create() {
        listOfExistingAsteroids = new CopyOnWriteArrayList<>();
        listOfExistingEnemyShips = new CopyOnWriteArrayList<>();
        listOfProjectiles = new CopyOnWriteArrayList<>();
        listOfEnemyProjectiles = new CopyOnWriteArrayList<>();

        bigAsteroidTexture = new Texture(Gdx.files.internal("bigAsteroid.png"));
        mediumAsteroidTexture = new Texture(Gdx.files.internal("mediumAsteroid.png"));
        smallAsteroidTexture = new Texture(Gdx.files.internal("smallAsteroid.png"));

        numberZero = new Texture(Gdx.files.internal("numberZero.png"));
        numberOne = new Texture(Gdx.files.internal("numberOne.png"));
        numberTwo = new Texture(Gdx.files.internal("numberTwo.png"));
        numberThree = new Texture(Gdx.files.internal("numberThree.png"));
        numberFour = new Texture(Gdx.files.internal("numberFour.png"));
        numberFive = new Texture(Gdx.files.internal("numberFive.png"));
        numberSix = new Texture(Gdx.files.internal("numberSix.png"));
        numberSeven = new Texture(Gdx.files.internal("numberSeven.png"));
        numberEight = new Texture(Gdx.files.internal("numberEight.png"));
        numberNine = new Texture(Gdx.files.internal("numberNine.png"));


        mainStage = new Stage();

        BaseActor floor = new BaseActor();
        floor.setTexture(new Texture(Gdx.files.internal("spaceBackground.png")));
        floor.setPosition(0, 0);
        mainStage.addActor(floor);


        spaceShip = new PlayerShip();

        gameOverSign = new BaseActor();
        gameOverSign.setTexture(new Texture(Gdx.files.internal("gameOver.png")));
        gameOverSign.setPosition(mainStage.getWidth() / 2 - gameOverSign.getWidth() / 2, mainStage.getHeight() / 2 - gameOverSign.getHeight() / 2);
        gameOverSign.setVisible(false);
        mainStage.addActor(gameOverSign);

        firstIndexScoreActor = new BaseActor();
        firstIndexScoreActor.setTexture(numberZero);
        firstIndexScoreActor.setWidth(15);
        firstIndexScoreActor.setHeight(25);
        firstIndexScoreActor.setPosition(mainStage.getWidth() - firstIndexScoreActor.getWidth(), mainStage.getHeight() - firstIndexScoreActor.getHeight());


        mainStage.addActor(firstIndexScoreActor);

        secondIndexScoreActor = new BaseActor();
        secondIndexScoreActor.setTexture(numberZero);
        secondIndexScoreActor.setWidth(15);
        secondIndexScoreActor.setHeight(25);
        secondIndexScoreActor.setPosition(firstIndexScoreActor.getX() - secondIndexScoreActor.getWidth() - secondIndexScoreActor.getWidth() / 3, mainStage.getHeight() - secondIndexScoreActor.getHeight());
        mainStage.addActor(secondIndexScoreActor);

        thirdIndexScoreActor = new BaseActor();
        thirdIndexScoreActor.setTexture(numberZero);
        thirdIndexScoreActor.setWidth(15);
        thirdIndexScoreActor.setHeight(25);
        thirdIndexScoreActor.setPosition(secondIndexScoreActor.getX() - thirdIndexScoreActor.getWidth() - thirdIndexScoreActor.getWidth() / 3, mainStage.getHeight() - thirdIndexScoreActor.getHeight());
        mainStage.addActor(thirdIndexScoreActor);

        fourthIndexScoreActor = new BaseActor();
        fourthIndexScoreActor.setTexture(numberZero);
        fourthIndexScoreActor.setWidth(15);
        fourthIndexScoreActor.setHeight(25);
        fourthIndexScoreActor.setPosition(thirdIndexScoreActor.getX() - fourthIndexScoreActor.getWidth() - fourthIndexScoreActor.getWidth() / 3, mainStage.getHeight() - fourthIndexScoreActor.getHeight());
        mainStage.addActor(fourthIndexScoreActor);

        fifthIndexScoreActor = new BaseActor();
        fifthIndexScoreActor.setTexture(numberZero);
        fifthIndexScoreActor.setWidth(15);
        fifthIndexScoreActor.setHeight(25);
        fifthIndexScoreActor.setPosition(fourthIndexScoreActor.getX() - fifthIndexScoreActor.getWidth() - fifthIndexScoreActor.getWidth() / 3, mainStage.getHeight() - fifthIndexScoreActor.getHeight());
        mainStage.addActor(fifthIndexScoreActor);

        pressEnterToPlayAgainSign = new BaseActor();
        pressEnterToPlayAgainSign.setTexture(new Texture(Gdx.files.internal("pressEnterToPlayAgain.png")));
        pressEnterToPlayAgainSign.setVisible(false);
        mainStage.addActor(pressEnterToPlayAgainSign);


        mapOfIndexesWithScore.put(firstIndexScoreActor, firstIndexScore);
        mapOfIndexesWithScore.put(secondIndexScoreActor, secondIndexScore);
        mapOfIndexesWithScore.put(thirdIndexScoreActor, thirdIndexScore);
        mapOfIndexesWithScore.put(fourthIndexScoreActor, fourthIndexScore);
        mapOfIndexesWithScore.put(fifthIndexScoreActor, fifthIndexScore);


        mainBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        mainBackgroundMusic.setLooping(true);
        pewSound = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
        thrusterSound = Gdx.audio.newSound(Gdx.files.internal("thrusterSound.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosionSound.mp3"));


        spaceShip.velocityX = 0;
        spaceShip.velocityY = 0;

        timeWhenLastShot = System.currentTimeMillis();
        isThrusterOn = false;

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {

                               //generateAsteroids();

                           }
                       }
                , 3        //    (delay)
                , 1F     //    (seconds)
        );
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               listOfExistingEnemyShips.add(
                                       new EnemyShip());
                           }
                       }
                , 20        //    (delay)
                , 30F     //    (seconds)
        );
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               for (EnemyShip enemyShip : listOfExistingEnemyShips) {
                                   if (!enemyShip.isHit() && !spaceShip.isHit()) {
                                       enemyShip.shoot();
                                   }
                               }
                           }
                       }
                , 5        //    (delay)
                , 1     //    (seconds)
        );

        mainBackgroundMusic.setVolume(0.5f);
        mainBackgroundMusic.play();

        // playerScore = 1000;

    }

    @Override
    public void render() {
        timeSinceStart += Gdx.graphics.getDeltaTime();
        difficulty = Math.abs((timeSinceStart - 180F) / 30F);
        if (timeSinceStart > 3) {
            if (timeSinceLastAsteroidGenerated > difficulty) {
                generateAsteroids();
                generateAsteroids();
                generateAsteroids();
            } else {
                timeSinceLastAsteroidGenerated += Gdx.graphics.getDeltaTime();
            }
        }

        if (!spaceShip.isHit()) {
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
                        spaceShip.getThruster().setHeight(thrusterCounter);
                        spaceShip.getThruster().addAction(Actions.alpha(thrusterCounter / 50F));
                        spaceShip.getThruster().setOrigin(spaceShip.getThruster().getWidth() - spaceShip.getWidth() / 2, spaceShip.getThruster().getHeight() + spaceShip.getHeight() / 2);
                    }

                }
                if (!isThrusterOn) {
                    thrusterSoundID = thrusterSound.loop(0.5F);
                }
                isThrusterOn = true;
            } else {
                if (thrusterCounter > 0) {
                    thrusterCounter -= 1;
                    spaceShip.getThruster().setHeight(thrusterCounter);
                    spaceShip.getThruster().addAction(Actions.alpha(thrusterCounter / 50F));
                    spaceShip.getThruster().setOrigin(spaceShip.getThruster().getWidth() - spaceShip.getWidth() / 2, spaceShip.getThruster().getHeight() + spaceShip.getHeight() / 2);
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
                spaceShip.getThruster().rotateBy(4);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                spaceShip.rotateBy(-4);
                spaceShip.getThruster().rotateBy(-4);
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
                spaceShip.hit();
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
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                timeSinceStart = 0;
                spaceShip.setHit(false);
                spaceShip.setTexture(new Texture(Gdx.files.internal("spaceShip.png")));
                spaceShip.getThruster().setVisible(true);
                spaceShip.getThruster().setRotation(0);
                playerScore = 0;
                gameOverSign.setVisible(false);
                pressEnterToPlayAgainSign.setVisible(false);
                spaceShip.addAction(Actions.alpha(1F));
                spaceShip.setPosition(mainStage.getWidth() / 2, mainStage.getHeight() / 2);
                firstIndexScoreActor.setPosition(mainStage.getWidth() - firstIndexScoreActor.getWidth(), mainStage.getHeight() - firstIndexScoreActor.getHeight());
                firstIndexScoreActor.addAction(Actions.alpha(1F));
                secondIndexScoreActor.setPosition(firstIndexScoreActor.getX() - secondIndexScoreActor.getWidth() - secondIndexScoreActor.getWidth() / 3, mainStage.getHeight() - secondIndexScoreActor.getHeight());
                secondIndexScoreActor.addAction(Actions.alpha(1F));
                thirdIndexScoreActor.setPosition(secondIndexScoreActor.getX() - thirdIndexScoreActor.getWidth() - thirdIndexScoreActor.getWidth() / 3, mainStage.getHeight() - thirdIndexScoreActor.getHeight());
                thirdIndexScoreActor.addAction(Actions.alpha(1F));
                fourthIndexScoreActor.setPosition(thirdIndexScoreActor.getX() - fourthIndexScoreActor.getWidth() - fourthIndexScoreActor.getWidth() / 3, mainStage.getHeight() - fourthIndexScoreActor.getHeight());
                fourthIndexScoreActor.addAction(Actions.alpha(1F));
                fifthIndexScoreActor.setPosition(fourthIndexScoreActor.getX() - fifthIndexScoreActor.getWidth() - fifthIndexScoreActor.getWidth() / 3, mainStage.getHeight() - fifthIndexScoreActor.getHeight());
                fifthIndexScoreActor.addAction(Actions.alpha(1F));

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

                    switch (existingAsteroid.getAsteroidType()) {
                        case "big":
                            playerScore += 10;
                            break;
                        case "medium":
                            playerScore += 50;
                            break;
                        case "small":
                            playerScore += 100;
                            break;
                    }
                }
                if (projectile.getX() > mainStage.getWidth() || projectile.getX() < 0 || projectile.getY() > mainStage.getHeight() || projectile.getY() < 0) {
                    listOfProjectiles.remove(projectile);
                }
            }
            if (existingAsteroid.getBoundingRectangle().overlaps(spaceShip.getBoundingRectangle()) && !spaceShip.isHit()) {
                spaceShip.hit();
            }

        }


        for (EnemyShip enemyShip : listOfExistingEnemyShips) {

            if (enemyShip.getX() - 50 > mainStage.getWidth() || enemyShip.getX() + 50 < 0 || enemyShip.getY() - 50 > mainStage.getHeight() || enemyShip.getY() + 50 < 0) {
                listOfExistingEnemyShips.remove(enemyShip);
            }
            for (BaseActor projectile : listOfProjectiles) {
                if (enemyShip.getBoundingRectangle().overlaps(projectile.getBoundingRectangle()) && !enemyShip.isHit()) {
                    enemyShip.hit();
                    playerScore += 500;
                }
            }
            if (enemyShip.isHit()) {
                enemyShip.explode(explosionSound);
            }
            if (spaceShip.isHit()) {
                listOfExistingEnemyShips.remove(enemyShip);
                enemyShip.getEnemyCanon().remove();
                enemyShip.remove();
            }
            distanceFromCanonX = (enemyShip.getX() + enemyShip.getWidth() / 2) - (spaceShip.getX() + spaceShip.getWidth() / 2);
            distanceFromCanonY = (enemyShip.getY() + enemyShip.getHeight() / 2) - (spaceShip.getY() + spaceShip.getHeight() / 2);
            canonAngle = (float) Math.toDegrees(Math.atan2(distanceFromCanonY, distanceFromCanonX)) + 90F;

            enemyShip.getEnemyCanon().setRotation(canonAngle);
        }

        for (Asteroid asteroid : listOfExistingAsteroids) {
            asteroid.rotateBy(asteroid.velocityX / 100);
            if (spaceShip.isHit()) {
                listOfExistingAsteroids.remove(asteroid);
                asteroid.remove();
            }
        }

        for (BaseActor listOfProjectile : listOfProjectiles) {
            listOfProjectile.moveBy(listOfProjectile.velocityX, listOfProjectile.velocityY);
        }
        for (BaseActor enemyProjectile : listOfEnemyProjectiles) {
            if (enemyProjectile.getBoundingRectangle().overlaps(spaceShip.getBoundingRectangle()) && !spaceShip.isHit()) {
                spaceShip.hit();
            } else {
                enemyProjectile.moveBy(enemyProjectile.velocityX, enemyProjectile.velocityY);
            }
        }

        if (spaceShip.isHit()) {
            spaceShip.velocityX = 0;
            spaceShip.velocityY = 0;
            spaceShip.getThruster().setVisible(false);
            spaceShip.explode(explosionSound);
            thrusterSound.stop();

            gameOverSign.setVisible(true);
            gameOverSign.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));

            firstIndexScoreActor.setPosition(gameOverSign.getX() + gameOverSign.getWidth() - firstIndexScoreActor.getWidth(), gameOverSign.getY() - firstIndexScoreActor.getHeight() - firstIndexScoreActor.getHeight() / 2F);
            secondIndexScoreActor.setPosition(firstIndexScoreActor.getX() - secondIndexScoreActor.getWidth() - secondIndexScoreActor.getWidth() / 3, gameOverSign.getY() - secondIndexScoreActor.getHeight() - secondIndexScoreActor.getHeight() / 2F);
            thirdIndexScoreActor.setPosition(secondIndexScoreActor.getX() - thirdIndexScoreActor.getWidth() - thirdIndexScoreActor.getWidth() / 3, gameOverSign.getY() - thirdIndexScoreActor.getHeight() - thirdIndexScoreActor.getHeight() / 2F);
            fourthIndexScoreActor.setPosition(thirdIndexScoreActor.getX() - fourthIndexScoreActor.getWidth() - fourthIndexScoreActor.getWidth() / 3, gameOverSign.getY() - fourthIndexScoreActor.getHeight() - fourthIndexScoreActor.getHeight() / 2F);
            fifthIndexScoreActor.setPosition(fourthIndexScoreActor.getX() - fifthIndexScoreActor.getWidth() - fifthIndexScoreActor.getWidth() / 3, gameOverSign.getY() - fifthIndexScoreActor.getHeight() - fifthIndexScoreActor.getHeight() / 2F);


            pressEnterToPlayAgainSign.setVisible(true);
            pressEnterToPlayAgainSign.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) % 1));
            pressEnterToPlayAgainSign.setPosition((gameOverSign.getX() + (gameOverSign.getWidth() / 2)) - pressEnterToPlayAgainSign.getWidth() / 2, firstIndexScoreActor.getY() - firstIndexScoreActor.getHeight() * 2);
            pressEnterToPlayAgainSign.setHeight(gameOverSign.getHeight() * 0.5F);
            pressEnterToPlayAgainSign.setWidth(gameOverSign.getHeight() * 6F);

            firstIndexScoreActor.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));
            secondIndexScoreActor.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));
            thirdIndexScoreActor.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));
            fourthIndexScoreActor.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));
            fifthIndexScoreActor.addAction(Actions.alpha((spaceShip.getTimeSinceHit()) / 4F));
        }

        spaceShip.moveBy(spaceShip.velocityX, spaceShip.velocityY);
        spaceShip.getThruster().setPosition(spaceShip.getX() - (spaceShip.getThruster().getWidth() / 2) + (spaceShip.getWidth() / 2), spaceShip.getY() - spaceShip.getThruster().getHeight());


        mapOfIndexesWithScore.put(fifthIndexScoreActor, (int) playerScore % 100000 / 10000);
        mapOfIndexesWithScore.put(fourthIndexScoreActor, (int) playerScore % 10000 / 1000);
        mapOfIndexesWithScore.put(thirdIndexScoreActor, (int) playerScore % 1000 / 100);
        mapOfIndexesWithScore.put(secondIndexScoreActor, (int) playerScore % 100 / 10);
        mapOfIndexesWithScore.put(firstIndexScoreActor, (int) playerScore % 10);

        for (Map.Entry<BaseActor, Integer> entry : mapOfIndexesWithScore.entrySet()) {
            switch (entry.getValue()) {
                case 0:
                    entry.getKey().setTexture(numberZero);
                    break;
                case 1:
                    entry.getKey().setTexture(numberOne);
                    break;
                case 2:
                    entry.getKey().setTexture(numberTwo);
                    break;
                case 3:
                    entry.getKey().setTexture(numberThree);
                    break;
                case 4:
                    entry.getKey().setTexture(numberFour);
                    break;
                case 5:
                    entry.getKey().setTexture(numberFive);
                    break;
                case 6:
                    entry.getKey().setTexture(numberSix);
                    break;
                case 7:
                    entry.getKey().setTexture(numberSeven);
                    break;
                case 8:
                    entry.getKey().setTexture(numberEight);
                    break;
                case 9:
                    entry.getKey().setTexture(numberNine);
                    break;
            }
            entry.getKey().setWidth(15);
            entry.getKey().setHeight(25);
        }
        mainStage.act();
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        mainStage.draw();
    }


    public void generateAsteroids() {
        timeSinceLastAsteroidGenerated = 0;
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
