package lab1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Iterator;

public class Binsearch implements Iterable<SearchResult> {

    private int[] array;
    private int item;

    public Binsearch(int[] array, int item) {
        this.array = array;
        this.item = item;
    }

    public Iterator<SearchResult> iterator() {
        return new SearchIterator(array, item);
    }

    public Optional<Integer> search(boolean showProgress) {
        Binsearch search = new Binsearch(array, item);

        final SearchIterator searchState = new SearchIterator(array, item);
        for (SearchResult result : search) {
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
            Binsearch search = new Binsearch(array, i);
            System.out.println("\t" + search.search(true));
        }
    }
}


class SearchResult {
    public final int pos;
    public final boolean found;

    public SearchResult(boolean found, int pos) {
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

/* could be an inner class - but avoiding this for pedagogy's sake */

class SearchIterator implements Iterator<SearchResult> {

    private int[] array;
    private int low;
    private int high;
    private int item;
    private boolean done = false;

    public SearchIterator(final int[] array, int item) {
        this.array = array;
        this.low = 0;
        this.high = array.length - 1;
        this.item = item;
        this.done = false;
    }

    public boolean hasNext() {
        return !done;
    }

    public SearchResult next() throws NoSuchElementException {
        if (this.done)
            throw new NoSuchElementException();
        if (low <= high) {
            final int mid = (low + high) / 2;
            if (item == array[mid]) {
                this.done = true;
                return new SearchResult(true, mid);
            } else if (item < array[mid]) {
                high = mid - 1;
                return new SearchResult(false, high);
            } else {
                low = mid + 1;
                return new SearchResult(false, low);
            }
        } else {
            this.done = true;
            return new SearchResult(false, low);
        }
    }

}

