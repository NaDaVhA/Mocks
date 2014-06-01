package recommender.entites;

public class SongsRecommendationScore implements Comparable<SongsRecommendationScore>{

	private User targetUser;
	private String songId;
	private double score;
	
	public 	SongsRecommendationScore(String songId, User targetUser, double score){		
		this.songId = songId;
		this.targetUser = targetUser;		
		this.score = score;
	}
	
	
	@Override
	public int compareTo(SongsRecommendationScore other) {	
		return Double.compare(score, other.score);
	}


	public User getTargetUser() {
		return targetUser;
	}
	
	public String getSongId() {
		return songId;
	}

	public double getScore() {
		return score;
	}
	
}
