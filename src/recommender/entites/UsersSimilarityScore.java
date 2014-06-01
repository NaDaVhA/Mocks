package recommender.entites;

public class UsersSimilarityScore implements Comparable<UsersSimilarityScore> {

	private User targetUser;
	private User user;
	private double score;
	
	public 	UsersSimilarityScore(User user, User targetUser, double score){		
		this.user = user;
		this.targetUser = targetUser;		
		this.score = score;
	}
	
	
	@Override
	public int compareTo(UsersSimilarityScore other) {	
		return Double.compare(score, other.score);
	}


	public User getTargetUser() {
		return targetUser;
	}
	
	public User getUser() {
		return user;
	}

	public double getScore() {
		return score;
	}
	
	
	
}
