package common.controllers;

import java.io.Serializable;

import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
/**
 * The Message class represent message that send from the client to the server and from the server to the client 
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Message implements Serializable {
	// Type of the operation we want from the server to make.
	private OperationType operationType;

	// If the operation that returns from the server succeeded\failed.
	private ReturnMessageType returnMessageType;

	private Object obj;

	// from the CLIENT to the SERVER.
	public Message(OperationType operationType, Object obj) {
		this.operationType = operationType;
		this.obj = obj;
	}

	// from the SERVER to the CLIENT.
	public Message(OperationType operationType, Object obj, ReturnMessageType returnMessageType) {
		this.operationType = operationType;
		this.obj = obj;
		this.returnMessageType = returnMessageType;
	}
	
	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public ReturnMessageType getReturnMessageType() {
		return returnMessageType;
	}

	public void setReturnMessageType(ReturnMessageType returnMessageType) {
		this.returnMessageType = returnMessageType;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return "Object: " +obj.toString();
	}
	

}
