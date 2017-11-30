package pl.prazuch.wojciech;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */
public class Data {

    private int ballX;
    private int ballY;
    private int myScore;
    private int opponentScore;

    private int myY;
    private int opponentY;


    public Data()
    {




    }

    public void setData(String output){
        String[] data = output.split("\\s+");

        ballX = Integer.parseInt(data[0]);
        ballY = Integer.parseInt(data[1]);

        myScore = Integer.parseInt(data[2]);
        opponentScore = Integer.parseInt(data[3]);

        myY = Integer.parseInt(data[4]);
        opponentY = Integer.parseInt(data[5]);

    }

    public String getData(){

        StringBuilder sb = new StringBuilder("");

        sb.append(ballX);
        sb.append(" ");

        sb.append(ballY);
        sb.append(" ");

        sb.append(myScore);
        sb.append(" ");

        sb.append(opponentScore);
        sb.append(" ");

        sb.append(myY);
        sb.append(" ");

        sb.append(opponentY);


        return sb.toString();


    }

    public int getBallX() {
        return ballX;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public int getMyY() {
        return myY;
    }

    public void setMyY(int myY) {
        this.myY = myY;
    }

    public int getOpponentY() {
        return opponentY;
    }

    public void setOpponentY(int opponentY) {
        this.opponentY = opponentY;
    }
}
