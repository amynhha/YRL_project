package com.yrl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class to represent the LinkedList.
 */
public class LinkedList<T> implements Iterable<T> {

	private Node<T> head;
	private int size;
	private Comparator<T> comparator;

	public LinkedList() {
		super();
		this.head = null;
		this.size = 0;
	}

	public LinkedList(Comparator<T> c) {
		this.comparator = c;
		this.head = null;
		this.size = 0;
	}

	/**
	 * Add the given value {@code x} to the end of the list.
	 * 
	 * @param x
	 */
	public void add(T x) {
		Node<T> newNode = new Node<>(x);
		if (this.isEmpty()) {
			this.head = newNode;
			this.size++;
			return;
		} else {
			if (comparator != null) {
				Node<T> curr = this.head;
				Node<T> prev = null;
				while (curr != null && comparator.compare(x, curr.getElement()) > 0) {
					prev = curr;
					curr = curr.getNext();
				}
				if (prev == null) {
					newNode.setNext(this.head);
					this.head = newNode;
				} else {
					newNode.setNext(curr);
					prev.setNext(newNode);
				}
			} else {
				Node<T> curr = this.head;
				while (curr.getNext() != null) {
					curr = curr.getNext();
				}
				curr.setNext(newNode);
			}
		}
		this.size++;
	}

	/**
	 * Add the given value {@code x} at the given index {@code index}.
	 * 
	 * @param x
	 */
	public void add(int index, T x) {
		if (index < 0 || index > this.size) {
			throw new IllegalArgumentException("Invalid index: " + index);
		}
		if (this.isEmpty()) {
			this.add(x);
			return;
		}
		Node<T> newNode = new Node<>(x);
		Node<T> previous = this.getNode(index - 1);
		Node<T> indexNode = previous.getNext();
		previous.setNext(newNode);
		newNode.setNext(indexNode);
		this.size++;
	}

	/**
	 * Get the value at the given index {@code index}.
	 * 
	 * @param x
	 */
	public T get(int index) {
		this.boundsCheck(index);
		return this.getNode(index).getElement();
	}

	/**
	 * Remove the value at the given index {@code index}. Returns element that was
	 * removed.
	 * 
	 * @param x
	 */
	public T remove(int index) {
		this.boundsCheck(index);
		if (index == 0) {
			Node<T> oldHead = this.head;
			this.head = this.head.getNext();
			this.size--;
			return oldHead.getElement();
		}
		Node<T> previous = this.getNode(index - 1);
		Node<T> curr = previous.getNext();
		previous.setNext(curr.getNext());
		this.size--;
		return curr.getElement();
	}

	/**
	 * Replace the given value {@code x} at the given index {@code index}.
	 * 
	 * @param x
	 */
	public T replace(int index, T x) {
		this.boundsCheck(index);
		return this.getNode(index).replaceElement(x);
	}

	/**
	 * Check the validity of the bounds by {@code index}.
	 * 
	 * @param index
	 */
	private void boundsCheck(int index) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + index);
		}
	}

	/**
	 * This function returns the size of the list, the number of elements currently
	 * stored in it.
	 * 
	 * @return
	 */
	public int size() {
		return this.size;
	}

	/**
	 * This function clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		this.size = 0;
	}

	/**
	 * Return {@code true} if the list has no element, {@code false} otherwise.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (this.size == 0);
	}

	/**
	 * Prints this list to the standard output.
	 */
	public void print() {
		if (this.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		Node<T> current = this.head;
		while (current != null) {
			sb.append(current.getElement() + ", ");
			current = current.getNext();
		}
		return;
	}

	/**
	 * Returns the node {@code Node} at the given index {@code index}.
	 * 
	 * @param index
	 * @return
	 */
	public Node<T> getNode(int index) {
		Node<T> currNode = this.head;
		for (int i = 0; i < index; i++) {
			currNode = currNode.getNext();
		}
		return currNode;
	}

	/**
	 * This is the iterator in order to implement the enhanced for loop.
	 * @return x
	 */
	@Override
	public Iterator<T> iterator() {
		Iterator<T> x = new Iterator<T>() {

			private int currIndex;

			@Override
			public boolean hasNext() {
				if (currIndex > size()) {
					return false;
				} else {
					return true;
				}
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return get(currIndex + 1);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
		return x;
	}

}
