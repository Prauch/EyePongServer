package pl.prazuch.wojciech;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */
public class GameTickProcessor {

    private int ballSpeedX;
    private int ballSpeedY;

    private int width;
    private int height;

    private double resolutionMultiplierX;
    private double resolutionMultiplierY;

    private String resolutionInfo;

    Paddle player1;
    Paddle player2;

    private int scoreP1;
    private int scoreP2;


    private ServerThread serverThread1;

    private ServerThread serverThread2;


    private Ball ball;

    private boolean gameStarted;

    private DataFromClient[] dataFromClients;

    private DataToClient[] dataToClients;

    GameTickProcessor(int portNumber) throws InterruptedException {

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                Socket socket1 = serverSocket.accept();
                serverThread1 = new ServerThread(socket1);
                serverThread1.start();

                Socket socket2 = serverSocket.accept();
                serverThread2 = new ServerThread(socket2);
                serverThread2.start();

            }
            catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        prepareGameTickProcessor();

        Thread.sleep(1000);

        resolutionInfo = serverThread1.getResolutionInfo();

        parseWidthAndHeight(resolutionInfo);

        resolutionInfo = serverThread2.getResolutionInfo();

        parseMultipliers(resolutionInfo);


        while(true)
        {

            dataFromClients[0] = serverThread1.getDataFromClient();
            dataFromClients[1] = serverThread2.getDataFromClient();

            processData();

            serverThread1.setDataToClient(dataToClients[0]);
            serverThread2.setDataToClient(dataToClients[1]);


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public void setDataFromClients(DataFromClient player1Data, DataFromClient player2Data)
    {

        dataFromClients[0] = player1Data;

        player1.setyPos(dataFromClients[0].getPlayerY());
        player1.setxPos(dataFromClients[0].getPlayerX());
        player1.setHeight(dataFromClients[0].getPlayerHeight());
        player1.setWidth(dataFromClients[0].getPlayerWidth());

        dataFromClients[1] = player2Data;

        player2.setyPos(dataFromClients[1].getPlayerY());
        player2.setxPos(dataFromClients[1].getPlayerX());
        player2.setHeight(dataFromClients[1].getPlayerHeight());
        player2.setWidth(dataFromClients[1].getPlayerWidth());

    }


    public void processData()
    {

        checkGameWorldRulesAndCorrectIfNecessary();

        ball.Move();

        //TickComputersAI();

        dataToClients[0] = prepareDataToClient1();
        dataToClients[1] = prepareDataToClient2();


    }

    public DataToClient getDataToClient1()
    {
        return dataToClients[0];
    }

    public DataToClient getDataToClient2() { return dataToClients[1]; }

    private DataToClient prepareDataToClient1() {


        DataToClient dataToClient = new DataToClient();

        dataToClient.setBallX((int) ball.getxPos());
        dataToClient.setBallY((int) ball.getyPos());

        dataToClient.setOpponentX((int)player2.getxPos());
        dataToClient.setOpponentY((int)player2.getyPos());

        dataToClient.setGameActive(returnGameActive());

        dataToClient.setGameScorePlayer1(scoreP1);
        dataToClient.setGameScorePlayer2(scoreP2);

        return dataToClient;


    }

    private int returnGameActive()
    {
        if(dataFromClients[0].getGameStarted() > 0 && dataFromClients[1].getGameStarted() > 0)
        {
            return 1;
        }
        else
            return 0;
    }

    private DataToClient prepareDataToClient2() {


        DataToClient dataToClient = new DataToClient();

        dataToClient.setBallX((int) (ball.getxPos()*resolutionMultiplierX));
        dataToClient.setBallY((int) (ball.getyPos()*resolutionMultiplierY));

        dataToClient.setOpponentX((int)player1.getxPos());
        dataToClient.setOpponentY((int)player1.getyPos());

        dataToClient.setGameActive(1);

        dataToClient.setGameScorePlayer1(scoreP1);
        dataToClient.setGameScorePlayer2(scoreP2);

        return dataToClient;


    }


    private void checkGameWorldRulesAndCorrectIfNecessary(){

        if(didBallHitHorizontalBorders())
            ball.setySpeed(ball.getySpeed()*(-1));

        if(hasPlayer2Won()) {
            increaseScoreOfPlayer(2);
            gameStarted = false;
            resetBall(ball);
        }
        if(hasPlayer1Won()) {
            increaseScoreOfPlayer(1);
            gameStarted = false;
            resetBall(ball);
        }

        if( didBallHitAnyOfThePaddles()) {
            if(whichPlayerDidTheBallHit()==Player.PLAYER1)
            {
                ball.calculateNewSpeedAfterIntersection(player1.getNormalizedRelativeIntersectionY(ball.getyPos()));
            }
            else
            {
                ball.calculateNewSpeedAfterIntersection(player2.getNormalizedRelativeIntersectionY(ball.getyPos()));
                ball.setxSpeed(ball.getxSpeed()*(-1));
            }
        }
    }


    private void parseWidthAndHeight(String resolutionInfo)
    {
        String[] dimens = resolutionInfo.split(" ");

        width = Integer.valueOf(dimens[0]);
        height = Integer.valueOf(dimens[1]);

    }

    private void parseMultipliers(String resolutionInfo)
    {
        String[] dimens = resolutionInfo.split(" ");

        int tempWidth = Integer.valueOf(dimens[0]);
        int tempHeight = Integer.valueOf(dimens[1]);

        resolutionMultiplierX = ((double)tempWidth/(double)width);

        resolutionMultiplierY = ((double)tempHeight/(double)height);

    }


    private boolean hasPlayer1Won() {
        return (ball.getxPos() > player2.getxPos() + player2.getWidth());
    }

    private boolean hasPlayer2Won() {
        return (ball.getxPos() < player1.getxPos() - player1.getWidth());
    }

    private void increaseScoreOfPlayer(int numOfPlayer)
    {
        if(numOfPlayer == 1)
        {
            scoreP1++;
        }
        else
        {
            scoreP2++;
        }
    }

    private boolean didBallHitHorizontalBorders() {
        return (ball.getyPos() + ball.getRadius() > height || ball.getyPos() <  0);
    }



    private boolean didBallHitAnyOfThePaddles(){
        if (((ball.getxPos() + ball.getRadius() > player2.getxPos()) && (ball.getyPos() >= player2.getyPos()) && (ball.getyPos() <= player2.getyPos() + player2.getHeight())) ||
                ((ball.getxPos() - ball.getRadius() < player1.getxPos()) && (ball.getyPos() >= player1.getyPos()) && (ball.getyPos() <= player1.getyPos() + player1.getHeight())))
        {
            return true;
        }
        else
            return false;

    }


    private Player whichPlayerDidTheBallHit(){

        if ((ball.getxPos() + ball.getRadius() > player2.getxPos()) && (ball.getyPos() >= player2.getyPos()) && (ball.getyPos() <= player2.getyPos() + player2.getHeight()))
            return Player.PLAYER2;
        else
            return Player.PLAYER1;


    }


    private void resetBall(Ball ball) {
        centerBall(ball);
        ball.setySpeed(1);
        ball.setxSpeed(1);

    }


    private void centerBall(Ball ball){
        ball.setyPos(height/2);
        ball.setxPos(width/2);
    }



    private void TickComputersAI()//double ballXPos, double ballYPos)
    {
//        if(ballXPos < width - width  / 4) {
//            player2.setyPos(ballYPos - player2.getHeight()/2);
//        }  else {
//            player2.setyPos(ballYPos > player2.getyPos() + player2.getHeight() / 2 ?player2.getyPos() + 1: player2.getyPos() - 1);
//        }

        player2.setyPos(ball.getyPos()-player2.getHeight()/2);

    }



    private void prepareGameTickProcessor()
    {
        scoreP1 = 0;
        scoreP2 = 0;

        ballSpeedY = 2;
        ballSpeedX = 2;


        dataFromClients = new DataFromClient[2];
        dataToClients = new DataToClient[2];

        player1 = new Paddle(100, 15, 0, 0);
        player2 = new Paddle(100, 15, width-15, 0);

        ball = new Ball(15, width/2, height/2, ballSpeedX, ballSpeedY);
    }




}
