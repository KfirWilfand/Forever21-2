package common.entity;

import common.entity.enums.ReaderCardStatus;

public class ReaderCard {
	ReaderCardStatus status;
	int lateReturnsBookCounter;

	ReaderCard() {
		this.lateReturnsBookCounter = 0;
	}
}
