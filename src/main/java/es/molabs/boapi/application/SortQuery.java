package es.molabs.boapi.application;

import java.util.List;

public class SortQuery {

    public enum SortType {
        ASCENDENT,
        DESCENDENT
    }

    private final List<Field> fields;

    public SortQuery(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public static class Field {

        private final String field;
        private final SortType type;

        public Field(String field, SortType type) {
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
