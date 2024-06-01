package com.yrl;

/**
 * A node to represent a link in a linked list.
 */
public class Node<T> {

	private T element;
	private Node<T> next;
	
	public Node(T element) {
		this.element = element;
		this.next = null;
	}
	
	public T getElement() {
		return this.element;
	}
	
	public Node<T> getNext() {
		return this.next;
	}
	
	public void setNext(Node<T> next) {
		this.next = next;
	}
	
	public void setElement(T element) {
		this.element = element;
	}
	
	public T replaceElement(T element) {
		T temp = this.element;
		this.element = element;
		return temp;
	}
	
}
