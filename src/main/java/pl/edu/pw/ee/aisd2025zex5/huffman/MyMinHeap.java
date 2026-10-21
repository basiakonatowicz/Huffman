package pl.edu.pw.ee.aisd2025zex5.huffman;

import java.util.ArrayList;
import java.util.List;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

/*
Kopiec minimalny sluzący jako kolejka priorytetowa
Przechowuje elementy T które są Comparable
Wydajność O(log N) dla operacji wstawiania i pobierania(usuwania) minimum
 */
public class MyMinHeap<T extends Comparable<T>> {

    private final List<T> heap;

    public MyMinHeap() {
        this.heap = new ArrayList<>();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void insert(T item) {
        heap.add(item);
        int index = heap.size() - 1;
        heapifyUp(index);
    }

    public T extractMin() throws CustomArgumentException {
        if (isEmpty()) {
            throw new CustomArgumentException("Kopiec jest pusty.");
        }

        T min = heap.get(0);

        if (heap.size() > 1) {
            T last = heap.remove(heap.size() - 1);
            heap.set(0, last);
            heapifyDown(0);
        } else {
            heap.remove(0);
        }

        return min;
    }


    private int getParentIndex(int i) {
        return (i - 1) >> 1;
    }

    private int getLeftChildIndex(int i) {
        return (i << 1) + 1;
    }

    private int getRightChildIndex(int i) {
        return (i << 1) + 2;
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    //naprawa wlasności kopca w gore
    private void heapifyUp(int index) {
        int parentIndex = getParentIndex(index);

        while (index > 0 && heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    //naprawa wlasności kopca w dol
    private void heapifyDown(int index) {
        int left = getLeftChildIndex(index);
        int right = getRightChildIndex(index);
        int smallest = index;

        if (left < heap.size() && heap.get(left).compareTo(heap.get(smallest)) < 0) {
            smallest = left;
        }

        if (right < heap.size() && heap.get(right).compareTo(heap.get(smallest)) < 0) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }
}