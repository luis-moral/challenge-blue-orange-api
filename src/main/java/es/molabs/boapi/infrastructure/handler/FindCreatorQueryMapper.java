package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.application.FindCreatorQuery;
import es.molabs.boapi.application.SortQuery;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class FindCreatorQueryMapper {

    private final static String FIELD_ID = "id";
    private final static String FIELD_FULL_NAME = "fullName";
    private final static String FIELD_MODIFIED = "modified";
    private final static String FIELD_COMICS = "comics";
    private final static String FIELD_SERIES = "series";
    private final static String FIELD_NOTES = "notes";
    private final static String FIELD_SORT_BY = "orderBy";

    public FindCreatorQuery from(MultiValueMap<String, String> queryParams) {
        return
            new FindCreatorQuery(
                queryParams.getFirst(FIELD_ID),
                queryParams.getFirst(FIELD_FULL_NAME),
                queryParams.getFirst(FIELD_MODIFIED),
                queryParams.getFirst(FIELD_COMICS),
                queryParams.getFirst(FIELD_SERIES),
                queryParams.getFirst(FIELD_NOTES),
                getSortQuery(queryParams)
            );
    }

    private SortQuery getSortQuery(MultiValueMap<String, String> queryParams) {
        SortQuery query = null;
        List<String> values = queryParams.get(FIELD_SORT_BY);

        if (!values.isEmpty()) {
            query = new SortQuery(
                values
                    .stream()
                    .map(this::toSortQueryField)
                    .collect(Collectors.toList())
            );
        }

        return query;
    }

    private SortQuery.SortQueryField toSortQueryField(String value) {
        return
            value.startsWith("-")
            ? new SortQuery.SortQueryField(value.substring(1), SortQuery.SortType.Descending)
            : new SortQuery.SortQueryField(value, SortQuery.SortType.Ascending);
    }
}
