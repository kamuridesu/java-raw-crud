package jsonparser;

import java.util.Map;

public final class JSONData {
    private Map<Object, Object> data;
    private long index;

    JSONData(Map<Object, Object> data, long index) {
        this.data = data;
        this.index = index;
    }

    public Map<Object, Object> getData() {
        return this.data;
    }

    public long getIndex() {
        return this.index;
    }

    public void setData(Map<Object, Object> data) {
        this.data = data;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
