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
	ErrorWhileTyping,
	ClientIsAlreadyLogin,
	SubscriberIsLocked, 
	CopyNotExist,
	HoldOrLockStatus, 
	CopyIsNotAvailable, 
	SubscriberAlreadyInOrderList
}
