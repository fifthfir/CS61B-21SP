public class TYIterator extends OHRequest.OHIterator {
    public TYIterator(OHRequest queue) {
        super(queue);
    }
    @Override
    public OHRequest next() {
        OHRequest result = super.next();
        while (result != null && result.description.contains("thank u")) {
            result = super.next();
        }
        return result;
    }
}
