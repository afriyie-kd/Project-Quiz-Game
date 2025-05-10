import java.sql.Timestamp;

public class Score {
    private int id;
    private String playerName;
    private int score;
    private Timestamp datePlayed;
    
    public Score(int id, String playerName, int score, Timestamp datePlayed) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
        this.datePlayed = datePlayed;
    }
    
    public int getId() {
        return id;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public int getScore() {
        return score;
    }
    
    public Timestamp getDatePlayed() {
        return datePlayed;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setDatePlayed(Timestamp datePlayed) {
        this.datePlayed = datePlayed;
    }
    
    // Optional: toString() method for debugging
    @Override
    public String toString() {
        return "Score{" +
               "id=" + id +
               ", playerName='" + playerName + '\'' +
               ", score=" + score +
               ", datePlayed=" + datePlayed +
               '}';
    }
}