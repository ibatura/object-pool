package org.example.smartpool.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Pair model.
 *
 * @author ibatura
 */
public class Pair<Left, Right> implements Serializable {
	private static final long serialVersionUID = -6306601878470533131L;
	/**
	 * First from the pair, or left.
	 */
	private final Left left;
	/**
	 * Second from the pair - or  right.
	 */
	private final Right right;

	/**
	 * Constructor.
	 *
	 * @param left  first bean
	 * @param right second bean
	 */
	public Pair(final Left left, final Right right) {
		this.left = left;
		this.right = right;
	}

	public Left getKey() {
		return left;
	}

	public Right getValue() {
		return right;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(left, pair.left) &&
				Objects.equals(right, pair.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
		sb.append("[left=").append(left);
		sb.append(", right=").append(right);
		sb.append(']');
		return sb.toString();
	}
}
