package common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;

/**
 * The ReaderCard class implements Serializable represent the Reader Card entity
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ReaderCard implements Serializable {
	/** status is the status of the subscriber */
	ReaderCardStatus status;
	/** lateReturnsBookCounter is the late in returns counter */
	Integer lateReturnsBookCounter;
	private Map<SubscriberHistoryType, List<HistoryItem>> history;
	
	/** toString override method from Object class */
	@Override
	public String toString() {
		return "ReaderCards [status=" +status + ", lateReturnsBookCounter=" + lateReturnsBookCounter + ", history=" + history; 	
	}

	/**
	 * ReaderCard constructor
	 * @param status
	 * @param lateReturnsBookCounter
	 * @param history
	 */
	//Create ReaderCard with given history
	public ReaderCard(ReaderCardStatus status, int lateReturnsBookCounter,
			Map<SubscriberHistoryType, List<HistoryItem>> history) {
		this.status = status;
		this.lateReturnsBookCounter = lateReturnsBookCounter;
		this.history = history;
	}
	/**
	 * ReaderCard constructor
	 * @param status
	 * @param lateReturnsBookCounter
	 */
	//Create ReaderCard init() history and wait for other query to add his history
	public ReaderCard(ReaderCardStatus status, int lateReturnsBookCounter) {
		this.status = status;
		this.lateReturnsBookCounter = lateReturnsBookCounter;
		this.init();
	}
	/**
	 * init
	 */
	public void init() {
		history = new HashMap<SubscriberHistoryType, List<HistoryItem>>();

		for (SubscriberHistoryType historyType : SubscriberHistoryType.getEnums()) {
			history.put(historyType, new ArrayList<HistoryItem>());
		}
	}
	/**
	 * getStatus getter of getting subscriber status
	 * @return status
	 */
	public ReaderCardStatus getStatus() {
		return status;
	}
	/**
	 * setStatus setter of getting subscriber status
	 * @param status
	 */
	public void setStatus(ReaderCardStatus status) {
		this.status = status;
	}
	/**
	 * getLateReturnsBookCounter getter of the lates in retrn counter
	 * @return lateReturnsBookCounter
	 */
	public Integer getLateReturnsBookCounter() {
		return lateReturnsBookCounter;
	}
	/**
	 * setLateReturnsBookCounter setter of the lates in retrn counter
	 * @param lateReturnsBookCounter
	 */
	public void setLateReturnsBookCounter(int lateReturnsBookCounter) {
		this.lateReturnsBookCounter = lateReturnsBookCounter;
	}
	/**
	 * setHistory setter of history
	 * @param history         history of subscriber
	 */
	public void setHistory(Map<SubscriberHistoryType, List<HistoryItem>> history) {
		this.history = history;
	}

	/**
	 * getHistory getter of history
	 * @return history

	 */
	public Map<SubscriberHistoryType, List<HistoryItem>> getHistory() {
		return history;
	}

}
