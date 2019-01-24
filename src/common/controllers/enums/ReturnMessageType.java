package common.controllers.enums;

public enum ReturnMessageType {
	UserIsNotExist, 
	UserSuccessLogin, 
	UserFailedLogin,
	BooksFound,
	BooksNotFound,
	SubsciberExist,
	SubsciberNotExist,
	EmailOrPhoneAreAlreadyExists,
	BookWasNotFoundOnManageStock,
	BooksFoundOnManageStock, 
	Successful,
	Unsuccessful, 
	ErrorWhileTyping,
	ClientIsAlreadyLogin,
	SubscriberIsLocked
}
