package common.entity;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class BookInOrder 
{
private int bCatalogNum;
private Queue<Integer> subscribersQueue;

public BookInOrder(int bCatalogNum) {
	super();
	this.bCatalogNum = bCatalogNum;
	this.subscribersQueue = new SynchronousQueue<Integer>();
}

}

