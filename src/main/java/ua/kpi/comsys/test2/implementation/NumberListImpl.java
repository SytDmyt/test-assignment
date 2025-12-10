/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

import ua.kpi.comsys.test2.NumberList;

/**
 * Custom implementation of INumberList interface.
 * Has to be implemented by each student independently.
 *
 * <p>Task done by: <br>
 * Sytnyk Dmytro <br>
 * group IC-34 <br>
 * 20 taken as the position in group list(no grade book)</p>
 *
 * @author Alexander Podrubailo
 *
 */
public class NumberListImpl implements NumberList {

    /**
     * Internal double-linked node storing a reference to a
     * {@link Byte} element and links to previous and next nodes.
     */
    private static class Node {
        /** The reference to the {@link Byte} element */
        byte value;
        /** Link to the next {@link Node} in the sequence */
        Node next;
        /** Link to the previous {@link Node} in the sequence */
        Node prev;

        /**
         * Creates a new node storing the specified value.
         *
         * @param value the {@link Byte} class object
         */
        Node(byte value) {
            this.value = value;
        }
    }

    /** The first {@link Node} of the List */
    private Node head;
    /** The amount of {@link Node} elements in the List */
    private int size;
    /** The base of values, stored in the List(by variant, its 2)*/
    private int base = 2;

    /**
     * Default constructor. Returns empty <tt>NumberListImpl</tt>
     */
    public NumberListImpl() {
        head = null;
        size = 0;
    }

    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * from file, defined in string format.
     *
     * @param file - file where number is stored.
     */
    public NumberListImpl(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            line = br.readLine();
            while (line != null) {
                sb.append(line.trim());
                line = br.readLine();
            }
            String number = sb.toString();
            if (!number.isEmpty()) {

                BigInteger n = new BigInteger(number);
                String binary = n.toString(base);
                for (char c : binary.toCharArray()) {
                    this.add((byte) (c - '0'));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * in string notation.
     *
     * @param value - number in string notation.
     */
    public NumberListImpl(String value) {
        BigInteger n = new BigInteger(value);
        String binary = n.toString(base);
        for (char c : binary.toCharArray()) {
            this.add((byte) (c - '0'));
        }
    }


    /**
     * Saves the number, stored in the list, into specified file
     * in <b>decimal</b> scale of notation.
     *
     * @param file - file where number has to be stored.
     */
    public void saveList(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(toDecimalString());
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing file", e);
        }
    }


    /**
     * Returns student's record book number, which has 4 decimal digits.
     *
     * @return student's record book number.
     */
    public static int getRecordBookNumber() {
        // I do not have my record book number
        // Therefore, I use my position number in group list
        return 20;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the same number
     * in other scale of notation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @return <tt>NumberListImpl</tt> in other scale of notation.
     */
    public NumberListImpl changeScale() {
        String decimal = toDecimalString();
        BigInteger n = new BigInteger(decimal);

        int new_base = 3;
        String ternary = n.toString(new_base);

        NumberListImpl result = new NumberListImpl();
        result.setBase(new_base);

        for (char c : ternary.toCharArray()) {
            result.add((byte)(c - '0'));
        }

        return result;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the result of
     * additional operation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @param arg - second argument of additional operation
     *
     * @return result of additional operation.
     */
    public NumberListImpl additionalOperation(NumberList arg) {
        long value1 = 0;
        long value2 = 0;

        Node cur1 = head;
        for (int i = 0; i < size; i++) {
            value1 = value1 * base + cur1.value;
            cur1 = cur1.next;
        }

        // We assume that both lists had the same base
        for (int i = 0; i < arg.size(); i++) {
            value2 = value2 * base + arg.get(i);
        }

        long result = value1 | value2;
        return new NumberListImpl(String.valueOf(result));
    }


    /**
     * Returns string representation of number, stored in the list
     * in <b>decimal</b> scale of notation.
     *
     * @return string representation in <b>decimal</b> scale.
     */
    public String toDecimalString() {
        long value = 0;
        Node cur = head;

        for (int i = 0; i < size; i++) {
            value = value * base + cur.value;
            cur = cur.next;
        }

        return Long.toString(value);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node cur = head;

        for (int i = 0; i < size; i++) {
            sb.append(cur.value);
            cur = cur.next;
        }

        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberListImpl other)) return false;
        if (size != other.size || base != other.base) return false;

        Node a = head;
        Node b = other.head;
        for (int i = 0; i < size; i++) {
            if (a.value != b.value) return false;
            a = a.next;
            b = b.next;
        }

        return true;
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public boolean contains(Object o) {
        Node cur = head;

        for (int i = 0; i < size; i++) {
            if (cur.value == (byte) o) return true;
            cur = cur.next;
        }
        return false;
    }


    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<>() {
            int i = 0;
            Node cur = head;
            public boolean hasNext() { return i < size; }
            public Byte next() {
                byte v = cur.value;
                cur = cur.next;
                i++;
                return v;
            }
        };
    }


    @Override
    public Object[] toArray() {
        Byte[] arr = new Byte[size];
        Node cur = head;
        for (int i = 0; i < size; i++) {
            arr[i] = cur.value;
            cur = cur.next;
        }
        return arr;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }


    @Override
    public boolean add(Byte e) {
        add(0, e);
        return true;
    }


    @Override
    public boolean remove(Object o) {
        Node cur = head;

        if (cur == null) return false;
        for (int i = 0; i < size; i++) {
            if (cur.value == (byte)o) {
                this.remove(i);
                return true;
            }
            cur = cur.next;
        }

        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);

        for (Object o: c) {
            if (!this.contains(o)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        Objects.requireNonNull(c);

        for (Byte b: c) {
            this.add(b);
        }

        return true;
    }


    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        Objects.requireNonNull(c);

        for (Byte b: c) {
            this.add(index, b);
        }

        return true;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);

        boolean changed = false;
        for (Object o : c)
            while (this.remove(o))
                changed = true;
        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);

        boolean changed = false;
        for (Object o : c)
            if (!this.contains(o)) {
                changed = true;
                this.remove(o);
            }
        return changed;
    }


    @Override
    public void clear() {
        head = null;
        size = 0;
    }


    @Override
    public Byte get(int index) {
        return node(index).value;
    }


    @Override
    public Byte set(int index, Byte element) {
        Node cur = node(index);
        byte old_value = cur.value;
        cur.value = element;
        return old_value;
    }


    @Override
    public void add(int index, Byte element) {
        if (element < 0 || element >= base)
            throw new IllegalArgumentException("Digit out of base range");

        Node n = new Node(element);

        if (head == null) {
            head = n;
            n.next = n.prev = n;
        } else if (index == 0) {
            Node tail = head.prev;
            n.next = head;
            n.prev = tail;
            tail.next = n;
            head.prev = n;
            head = n;
        } else if (index == size) {
            Node tail = head.prev;
            n.prev = tail;
            n.next = head;
            tail.next = n;
            head.prev = n;
        } else {
            Node cur = node(index);
            Node p = cur.prev;
            p.next = n;
            n.prev = p;
            n.next = cur;
            cur.prev = n;
        }

        size++;
    }


    @Override
    public Byte remove(int index) {
        Node n = node(index);

        if (size == 1) {
            head = null;
        } else {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            if (n == head) head = n.next;
        }

        size--;
        return n.value;
    }


    @Override
    public int indexOf(Object o) {
        Node c = head;
        for (int i = 0; i < size; i++) {
            if (c.value == (byte) o) {
                return i;
            }
            c = c.next;
        }
        return 0;
    }


    @Override
    public int lastIndexOf(Object o) {
        Node c = head.prev;
        for (int i = size - 1; i >= 0; i--) {
            if (c.value == (byte) o) {
                return i;
            }
            c = c.prev;
        }
        return 0;
    }

    /**
     * A list iterator for the NumberListImpl collection,
     * allowing traversal in both forward and backward directions
     * and supporting modification operations during iteration.
     */
    private class NumberListIterator implements ListIterator<Byte> {
        /** Last {@link Node}, returned either by {@code next()}
         * or {@code previous()} functions */
        private Node last_returned;
        /** The {@link Node}, which is located after the
         * position of the cursor(iterator) */
        private Node next;
        /** The index of the {@link Node}, which is located
         * after the position of the cursor(iterator) **/
        private int nextIndex;

        /**
         * Creates a list iterator starting at the specified index
         *
         * @param index position at which the iterator should start
         * @throws IndexOutOfBoundsException if index is out of valid range
         */
        NumberListIterator(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        /**
         * Returns {@code true} if there is a next element
         */
        public boolean hasNext() {
            return nextIndex < size;
        }

        /**
         * Returns the next node and advances the iterator
         *
         * @return the next node
         * @throws NoSuchElementException if no next element exists
         */
        public Byte next() {
            if (!hasNext())
                throw new NoSuchElementException();

            last_returned = next;
            next = next.next;
            nextIndex++;
            return last_returned.value;
        }

        /**
         * Returns {@code true} if there is a previous element.
         */
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        /**
         * Returns the previous node and moves the iterator backward.
         *
         * @return the previous node
         * @throws NoSuchElementException if no previous element exists
         */
        public Byte previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            next = next.prev;
            last_returned = next;
            nextIndex--;
            return last_returned.value;
        }

        /**
         * Returns an index of the next element(after the cursor)
         *
         * @return the index of the next element
         */
        public int nextIndex() {
            return nextIndex;
        }

        /**
         * Returns an index of the previous element(before the cursor)
         *
         * @return the index of the previous element
         */
        public int previousIndex() {
            return nextIndex - 1;
        }

        /**
         * Removes the last returned element
         *
         * @throws IllegalStateException if no element can be removed
         */
        public void remove() {
            if (last_returned == null)
                throw new IllegalStateException();

            Node last_next = last_returned.next;
            last_returned.prev.next = last_returned.next;
            last_returned.next.prev = last_returned.prev;
            size--;

            if (next == last_returned)
                next = last_next;
            else
                nextIndex--;

            last_returned = null;
        }

        /**
         * Replaces the last returned element with the new value
         *
         * @param e the new value
         * @throws IllegalStateException if no element can be replaced
         */
        public void set(Byte e) {
            if (last_returned == null)
                throw new IllegalStateException();

            last_returned.value = e;
        }

        /**
         * Inserts a node at the iterator’s current position
         *
         * @param e the carriage to insert
         */
        public void add(Byte e) {
            Node prev = next.prev;
            Node new_node = new Node(e);

            if (head == null) {
                head = new_node;
                new_node.prev = new_node.next = new_node;
            } else {
                prev.next = new_node;
                next.prev = new_node;
                new_node.prev = prev;
                new_node.next = next;
            }

            size++;
            nextIndex++;
            last_returned = null;
        }
    }


    @Override
    public ListIterator<Byte> listIterator() {
        return new NumberListIterator(0);
    }


    @Override
    public ListIterator<Byte> listIterator(int index) {
        checkIndex(index);
        return new NumberListIterator(index);
    }


    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex);

        NumberListImpl new_list = new NumberListImpl();

        Node cur = node(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            new_list.add(cur.value);
            cur = cur.next;
        }

        return new_list;
    }


    @Override
    public boolean swap(int index1, int index2) {
        checkIndex(index1);
        checkIndex(index2);

        byte a = this.get(index1);
        byte b = this.get(index2);
        this.set(index1, b);
        this.set(index2, a);
        return true;
    }


    @Override
    public void sortAscending() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (this.get(i) > this.get(j)) {
                    swap(i, j);
                }
            }
        }
    }


    @Override
    public void sortDescending() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (this.get(i) < this.get(j)) {
                    swap(i, j);
                }
            }
        }
    }


    @Override
    public void shiftLeft() {
        if (!isEmpty()) {
            this.add(this.remove(0));
        }
    }


    @Override
    public void shiftRight() {
        if (!isEmpty()) {
            this.add(0, this.remove(size - 1));
        }
    }

    /** Sets the base of the list. Therefore, the list is not for binary values only
     *
     * @param new_base new base of the list*/
    public void setBase(int new_base) {
        if (new_base <= 1) throw new IllegalArgumentException();
        this.base = new_base;
    }



    /** Iterates through the linked list and returns a node at an index
     *
     * @param index the required index
     * @return the node at the index
     * */
    private Node node(int index) {
        checkIndex(index);
        Node cur;
        cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    /**
     * Throws an exception in case of invalid index
     * <p>To account for the cyclical nature of the list,
     * the index is limited at {@code size * 2}</p>
     *
     * @throws IndexOutOfBoundsException if index is not valid
     */
    private void checkIndex(int index) {
        if (index < 0 || index > size * 2) throw new IndexOutOfBoundsException("index: " + index);
    }
}
