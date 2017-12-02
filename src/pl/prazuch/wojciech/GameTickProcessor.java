package pl.prazuch.wojciech;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */
public class GameTickProcessor {

    private int ballSpeedX;
    private int ballSpeedY;

    private int width;
    private int height;

    Paddle player1;
    Paddle player2;

    private int scoreP1;
    private int scoreP2;



    private Ball ball;

    private boolean gameStarted;

    private DataFromClient[] dataFromClients;

    private DataToClient[] dataToClients;

    GameTickProcessor()
    {
        scoreP1 = 0;
        scoreP2 = 0;

        ballSpeedY = 1;
        ballSpeedX = 1;

        width = 1280;
        height = 750;

        dataFromClients = new DataFromClient[2];
        dataToClients = new DataToClient[2];

        player1 = new Paddle(100, 15, 0, 0);
        player2 = new Paddle(100, 15, width-15, 0);


        ball = new Ball(15, width/2, height/2, ballSpeedX, ballSpeedY);



    }


    public void setDataFromClients(DataFromClient player1Data)//, DataFromClient player2Data)
    {

        dataFromClients[0] = player1Data;

        player1.setyPos(dataFromClients[0].getPlayerY());
        player1.setxPos(dataFromClients[0].getPlayerX());
        player1.setHeight(dataFromClients[0].getPlayerHeight());
        player1.setWidth(dataFromClients[0].getPlayerWidth());

        //dataFromClients[1] = player2Data;



    }


    public void processData()
    {

        checkGameWorldRulesAndCorrectIfNecessary();

        ball.Move();

        TickComputersAI();

        dataToClients[0] = prepareDataToClient(1);


    }

    public DataToClient getDataToClient()
    {
        return dataToClients[0];
    }

    private DataToClient prepareDataToClient(int whichPlayer) {

        whichPlayer--;

        DataToClient dataToClient = new DataToClient();

        dataToClient.setBallX((int) ball.getxPos());
        dataToClient.setBallY((int) ball.getyPos());

        dataToClient.setOpponentX(width-15);
        dataToClient.setOpponentY((int)ball.getyPos());

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







}
