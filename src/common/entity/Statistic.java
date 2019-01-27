package common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Statistic implements Serializable {
	private float popAverage;
	private int popMedian;
	private Map<Integer, List<Integer>> popDistribution;

	private float regAverage;
	private int regMedian;
	private Map<Integer, List<Integer>> regDistribution;

	private float lateAverage;
	private int lateMedian;
	private Map<Integer, List<Integer>> lateDistribution;

	private ActiviySnapshot activiySnapshot;
	private Date firstSnapshot;
	private Date lastSnapshot;

	public Statistic(float popAverage, int popMedian, Map<Integer, List<Integer>> popDistribution, float regAverage,
			int regMedian, Map<Integer, List<Integer>> regDistribution, float lateAverage, int lateMedian,
			Map<Integer, List<Integer>> lateDistribution, ActiviySnapshot activiySnapshot, Date firstSnapshot,
			Date lastSnapshot) {

		this.popAverage = popAverage;
		this.popMedian = popMedian;
		this.popDistribution = popDistribution;
		this.regAverage = regAverage;
		this.regMedian = regMedian;
		this.regDistribution = regDistribution;
		this.lateAverage = lateAverage;
		this.lateMedian = lateMedian;
		this.lateDistribution = lateDistribution;
		this.activiySnapshot = activiySnapshot;
		this.firstSnapshot = firstSnapshot;
		this.lastSnapshot = lastSnapshot;
	}


	public float getPopAverage() {
		return popAverage;
	}

	public void setPopAverage(float popAverage) {
		this.popAverage = popAverage;
	}

	public int getPopMedian() {
		return popMedian;
	}

	public void setPopMedian(int popMedian) {
		this.popMedian = popMedian;
	}

	public Map<Integer, List<Integer>> getPopDistribution() {
		return popDistribution;
	}

	public void setPopDistribution(Map<Integer, List<Integer>> popDistribution) {
		this.popDistribution = popDistribution;
	}

	public float getRegAverage() {
		return regAverage;
	}

	public void setRegAverage(float regAverage) {
		this.regAverage = regAverage;
	}

	public int getRegMedian() {
		return regMedian;
	}

	public void setRegMedian(int regMedian) {
		this.regMedian = regMedian;
	}

	public Map<Integer, List<Integer>> getRegDistribution() {
		return regDistribution;
	}

	public void setRegDistribution(Map<Integer, List<Integer>> regDistribution) {
		this.regDistribution = regDistribution;
	}

	public float getLateAverage() {
		return lateAverage;
	}

	public void setLateAverage(float lateAverage) {
		this.lateAverage = lateAverage;
	}

	public int getLateMedian() {
		return lateMedian;
	}

	public void setLateMedian(int lateMedian) {
		this.lateMedian = lateMedian;
	}

	public Map<Integer, List<Integer>> getLateDistribution() {
		return lateDistribution;
	}

	public void setLateDistribution(Map<Integer, List<Integer>> lateDistribution) {
		this.lateDistribution = lateDistribution;
	}

	public ActiviySnapshot getActiviySnapshot() {
		return activiySnapshot;
	}

	public void setActiviySnapshot(ActiviySnapshot activiySnapshot) {
		this.activiySnapshot = activiySnapshot;
	}

	public Date getFirstSnapshot() {
		return firstSnapshot;
	}

	public void setFirstSnapshot(Date firstSnapshot) {
		this.firstSnapshot = firstSnapshot;
	}

	public Date getLastSnapshot() {
		return lastSnapshot;
	}

	public void setLastSnapshot(Date lastSnapshot) {
		this.lastSnapshot = lastSnapshot;
	}

}
