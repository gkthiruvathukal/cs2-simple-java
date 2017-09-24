package lab1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Iterator;

public class BinarySearch implements Iterable<SearchState> {

    private int[] array;
    private int item;

    public BinarySearch(int[] array, int item) {
        this.array = array;
        this.item = item;
    }

    public Iterator<SearchState> iterator() {
        return new SearchStateIterator(array, item);
    }

    public Optional<Integer> search(boolean showProgress) {
        BinarySearch search = new BinarySearch(array, item);

        final SearchStateIterator searchState = new SearchStateIterator(array, item);
        for (SearchState result : search) {
            if (showProgress)
                System.out.println(result);
            if (result.found)
                return Optional.of(result.pos);
        }
        return Optional.empty();
    }

    public static void main(final String[] args) {
        final int[] array = new int[]{2, 3, 5, 7, 11, 13, 17};
        for (int i = 0; i < 20; i++) {
            System.out.printf(">>> Finding %d\n", i);
            BinarySearch search = new BinarySearch(array, i);
            System.out.println("\t" + search.search(true));
        }
    }
}


class SearchState {
    public final int pos;
    public final boolean found;

    public SearchState(boolean found, int pos) {
        this.pos = pos;
        this.found = found;
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter w = new PrintWriter(sw);
        w.printf("pos = %d; found = %b", pos, found);
        w.flush();
        return sw.toString();
    }

}

/* could be an inner class - but avoiding this for 271 pedagogy's sake */

class SearchStateIterator implements Iterator<SearchState> {

    private int[] array;
    private int low;
    private int high;
    private int item;
    private boolean done = false;

    public SearchStateIterator(final int[] array, int item) {
        this.array = array;
        this.low = 0;
        this.high = array.length - 1;
        this.item = item;
        this.done = false;
    }

    public boolean hasNext() {
        return !done;
    }

    public SearchState next() throws NoSuchElementException {
        if (this.done)
            throw new NoSuchElementException();
        if (low <= high) {
            final int mid = (low + high) / 2;
            if (item == array[mid]) {
                this.done = true;
                return new SearchState(true, mid);
            } else if (item < array[mid]) {
                high = mid - 1;
                return new SearchState(false, high);
            } else {
                low = mid + 1;
                return new SearchState(false, low);
            }
        } else {
            this.done = true;
            return new SearchState(false, low);
        }
    }

}

