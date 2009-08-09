package org.legzo.castorranker.ranker;

public class EloModRanker {

	private static int k = 10;
	private static float j = 0.02f;

	public static long getDiff(long ranking, long l, boolean playerAVictory,
			int pointDiff) {
		double outcome = 0;

		if (playerAVictory) {
			outcome = 1;
		} else {
			outcome = 0.5;
		}
		// Takes the rating of two players, the outcome of the game, and returns
		// the new rating of the first player
		float d = ranking - l;
		float exponent = -d / 400;
		double expectedOutcome = 1 / (1 + (Math.pow(10, exponent)));

		long eloDiff = Math.round(getRatingConstant(ranking)
				* (outcome - expectedOutcome));

		return ponderate(pointDiff, eloDiff);
	}

	public static long ponderate(int pointDiff, long eloDiff) {
		return (long) ((1 + j * pointDiff) * eloDiff);
	}

	public static int getRatingConstant(long playerRating) {
		// int k = 10;
		//
		// if (playerRating < 2000) {
		// k = 30;
		// }
		// if (playerRating >= 2000 && playerRating < 2400) {
		// k = 20;
		// }
		//		
		// k = 10;

		return k;
	}

	public static void setK(int k) {
		EloModRanker.k = k;
	}

	public static void setJ(float j) {
		EloModRanker.j = j;
	}
}
