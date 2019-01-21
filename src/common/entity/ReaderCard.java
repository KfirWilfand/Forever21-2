package common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;

public class ReaderCard implements Serializable {
	ReaderCardStatus status;
	Integer lateReturnsBookCounter;
	private Map<SubscriberHistoryType, List<HistoryItem>> history;
	
	@Override
	public String toString() {
		return "ReaderCards [status=" +status + ", lateReturnsBookCounter=" + lateReturnsBookCounter + ", history=" + history; 	
	}


	//Create ReaderCard with given history
	public ReaderCard(ReaderCardStatus status, int lateReturnsBookCounter,
			Map<SubscriberHistoryType, List<HistoryItem>> history) {
		this.status = status;
		this.lateReturnsBookCounter = lateReturnsBookCounter;
		this.history = history;
	}
	
	//Create ReaderCard init() history and wait for other query to add his history
	public ReaderCard(ReaderCardStatus status, int lateReturnsBookCounter) {
		this.status = status;
		this.lateReturnsBookCounter = lateReturnsBookCounter;
		this.init();
	}

	public void init() {
		history = new HashMap<SubscriberHistoryType, List<HistoryItem>>();

		for (SubscriberHistoryType historyType : SubscriberHistoryType.getEnums()) {
			history.put(historyType, new ArrayList<HistoryItem>());
		}
	}
	
	public ReaderCardStatus getStatus() {
		return status;
	}

	public void setStatus(ReaderCardStatus status) {
		this.status = status;
	}

	public Integer getLateReturnsBookCounter() {
		return lateReturnsBookCounter;
	}

	public void setLateReturnsBookCounter(int lateReturnsBookCounter) {
		this.lateReturnsBookCounter = lateReturnsBookCounter;
	}

	public void setHistory(Map<SubscriberHistoryType, List<HistoryItem>> history) {
		this.history = history;
	}


	public Map<SubscriberHistoryType, List<HistoryItem>> getHistory() {
		return history;
	}

}
