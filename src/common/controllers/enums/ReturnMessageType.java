package common.controllers.enums;

public enum ReturnMessageType {
	UserIsNotExist, 
	UserSuccessLogin, 
	UserFailedLogin,
	BooksFound,
	BooksNotFound,
	SubscriberExist,
	SubscriberNotExist,
	EmailOrPhoneAreAlreadyExists,
	BookWasNotFoundOnManageStock,
	BooksFoundOnManageStock, 
	Successful,
	Unsuccessful, 
	SuccessfulWithLastSnapshotDate,


	ErrorWhileTyping,
	ClientIsAlreadyLogin,
	SubscriberIsLocked, 
	CopyNotExist,
	HoldOrLockStatus, 
	CopyIsNotAvailable, 
	SubscriberAlreadyInOrderList, wrongBorrowDetails, FullOrderList,
	ChangeStatusToLock, ChangeStatusToActive, subscriberInWaitingList, GetStatstic
}
