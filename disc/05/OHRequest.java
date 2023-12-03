import java.util.Iterator;
import java.util.NoSuchElementException;

public class OHRequest {
    public String description;
    public String name;
    public OHRequest next;
    public OHRequest(String description, String name, OHRequest next) {
        this.description = description;
        this.name = name;
        this.next = next;
    }
    public static class OHIterator implements Iterator<OHRequest> {
        OHRequest curr;
        public OHIterator(OHRequest queue) {
            curr = queue;
        }
        public boolean isGood(String description) {
            return description != null && description.length() > 5;
        }

        @Override
        public boolean hasNext() {
            while (curr != null && !isGood(curr.description)) {
                curr = curr.next;
            }
            if (curr == null) {
                return false;
            }
            return true;
        }

        @Override
        public OHRequest next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            OHRequest currRequest = curr;
            curr = curr.next;
            return currRequest;
        }
    }
}
