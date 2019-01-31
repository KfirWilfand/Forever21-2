package common.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import common.entity.enums.BookStatsticType;

public class BookStatistic implements Serializable{
	private float average;
	private int median;
	private Map<Integer, List<Integer>> distribution;
	private BookStatsticType type;

	public BookStatistic(Map<Integer, List<Integer>> distribution, float average, int median,BookStatsticType type) {
		super();
		this.average = average;
		this.median = median;
		this.distribution = distribution;
		this.type = type;
	}

	public BookStatsticType getType() {
		return type;
	}

	public float getAverage() {
		return average;
	}

	public int getMedian() {
		return median;
	}

	public Map<Integer, List<Integer>> getDistribution() {
		return distribution;
	}

}
