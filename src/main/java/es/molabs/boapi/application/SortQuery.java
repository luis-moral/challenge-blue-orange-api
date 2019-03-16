package es.molabs.boapi.application;

import java.util.List;

public class SortQuery {

    public enum SortType {
        Ascending,
        Descending
    }

    private final List<SortQueryField> fields;

    public SortQuery(List<SortQueryField> fields) {
        this.fields = fields;
    }

    public List<SortQueryField> getFields() {
        return fields;
    }

    public static class SortQueryField {

        private final String field;
        private final SortType type;

        public SortQueryField(String field, SortType type) {
            this.field = field;
            this.type = type;
        }

        public String getField() {
            return field;
        }

        public SortType getType() {
            return type;
        }
    }
}
