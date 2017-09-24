package lab1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Iterator;

public class Binsearch implements Iterable<SearchState> {

    private int[] array;
    private int item;
    private SearchStateListener listener = new ShowNoProgress();

    public Binsearch(int[] array, int item) {
        this.array = array;
        this.item = item;
    }

    public static Optional<Integer> search(int[] array, int item) {
        Binsearch binSearch = new Binsearch(array, item);
        return binSearch.go();
    }

    public Iterator<SearchState> iterator() {
        return new SearchStateIterator(array, item);
    }

    public Optional<Integer> go() {
        final SearchStateIterator searchState = new SearchStateIterator(array, item);
        for (SearchState state : this) {
            notifyListener(state);
            if (state.found)
                return Optional.of(state.pos);
        }
        return Optional.empty();
    }

    public void addProgressListener(SearchStateListener listener) {
        this.listener = listener;
    }

    public void notifyListener(SearchState state) {
        listener.showProgress(state);
    }

    public static void main(final String[] args) {

        int[] array;
        if (args.length > 0) {
            System.out.println("Command line");
            array = new int[args.length];
            for (int i=0; i < args.length; i++)
                array[i] = Integer.parseInt(args[i]);
        } else {
            array = new int[]{2, 3, 5, 7, 11, 13, 17};
        }

        /* with progress */
        for (int i = 0; i < 20; i++) {
            System.out.printf(">>> Finding %d\n", i);
            Binsearch search = new Binsearch(array, i);
            search.addProgressListener(new ShowProgress());
            System.out.println("\t" + search.go());
        }

        /* without progress */
        for (int i = 0; i < 20; i++) {
            System.out.printf(">>> Finding %d\n", i);
            Binsearch search = new Binsearch(array, i);
            System.out.println("\t" + search.go());
        }

    }
}

interface SearchStateListener {
    void showProgress(SearchState state);
}

class ShowNoProgress implements SearchStateListener {
    public void showProgress(SearchState state) { }
}

class ShowProgress implements SearchStateListener {
    public void showProgress(SearchState state) {
        System.out.println(state);
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
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        printer.printf("pos = %d; found = %b", pos, found);
        return writer.toString();
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

