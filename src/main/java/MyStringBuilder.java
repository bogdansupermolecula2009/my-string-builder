import java.util.Stack;

public class MyStringBuilder {

    private char[] value;
    private int length;
    private Stack<Snapshot> states;


    public MyStringBuilder() {
        this.value = new char[16];
        this.length = 0;
        this.states = new Stack<>();
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > value.length) {
            int newCapacity = Math.max(value.length * 2, minCapacity);
            char[] newValue = new char[newCapacity];
            System.arraycopy(value, 0, newValue, 0, length);
            value = newValue;
        }
    }

    public void undo() {
        if (!states.isEmpty()) {
            Snapshot snapshot = states.pop();
            setState(snapshot);
        } else {
            System.out.println("Нет состояний для отката!");
        }
    }


    public MyStringBuilder append(String str) {
        saveSnapshot();
        if (str == null) return this;

        ensureCapacity(length + str.length());
        for (int i = 0; i < str.length(); i++) {
            value[length++] = str.charAt(i);
        }
        return this;
    }

    public MyStringBuilder delete(int start, int end) {
        if (start < 0 || end > length || start > end) {
            throw new StringIndexOutOfBoundsException("Invalid range");
        }

        saveSnapshot();
        int shift = end - start;
        System.arraycopy(value, end, value, start, length - end);
        length -= shift;
        return this;
    }

    public MyStringBuilder insert(int offset, String str) {
        if (offset < 0 || offset > length) {
            throw new StringIndexOutOfBoundsException("Invalid offset");
        }

        saveSnapshot();
        if (str == null) return this;

        ensureCapacity(length + str.length());
        System.arraycopy(value, offset, value, offset + str.length(), length - offset);
        for (int i = 0; i < str.length(); i++) {
            value[offset + i] = str.charAt(i);
        }
        length += str.length();
        return this;
    }


    public MyStringBuilder replace(int start, int end, String str) {
        delete(start, end);
        insert(start, str);
        return this;
    }


    public String toString() {
        return new String(value, 0, length);
    }


    private static class Snapshot {

        private final char[] value;
        private final int length;

        public char[] getValue() {
            return value;
        }

        public int getLength() {
            return length;
        }

        public Snapshot(char[] value, int length) {
            this.value = value.clone();
            this.length = length;
        }
    }


    private void saveSnapshot() {
        states.push(new Snapshot(value, length));
    }


    private void setState(Snapshot snapshot) {
        this.value = snapshot.getValue();
        this.length = snapshot.getLength();
    }

}

