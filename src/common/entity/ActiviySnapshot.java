package common.entity;

import java.io.Serializable;
import java.sql.Date;
/**
 * ActiviySnapshot class present the status of the subscribers at specific time
 */
public class ActiviySnapshot implements Serializable {
	private Date aDate;
	private int aLockSub;
	private int aHoldSub;
	private int aActiveSub;
	private int aCopies;
	private int aLates;

	public ActiviySnapshot(Date aDate, int aLockSub, int aHoldSub, int aActiveSub, int aCopies, int aLates) {
		this.aDate = aDate;
		this.aLockSub = aLockSub;
		this.aHoldSub = aHoldSub;
		this.aActiveSub = aActiveSub;
		this.aCopies = aCopies;
		this.aLates = aLates;
	}

	public Date getaDate() {
		return aDate;
	}

	public void setaDate(Date aDate) {
		this.aDate = aDate;
	}

	public int getLock() {
		return aLockSub;
	}

	public void setaLockSub(int aLockSub) {
		this.aLockSub = aLockSub;
	}

	public int getHold() {
		return aHoldSub;
	}

	public void setaHoldSub(int aHoldSub) {
		this.aHoldSub = aHoldSub;
	}

	public int getActive() {
		return aActiveSub;
	}

	public void setaActiveSub(int aActiveSub) {
		this.aActiveSub = aActiveSub;
	}

	public int getCopies() {
		return aCopies;
	}

	public void setaCopies(int aCopies) {
		this.aCopies = aCopies;
	}

	public int getLates() {
		return aLates;
	}

	public void setaLates(int aLates) {
		this.aLates = aLates;
	}
	
	

}