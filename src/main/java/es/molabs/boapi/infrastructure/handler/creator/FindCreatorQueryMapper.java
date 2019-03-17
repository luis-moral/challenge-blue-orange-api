package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creator.SortQuery;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class FindCreatorQueryMapper {

    private static final String FIELD_ID = "id";
    private static final String FIELD_FULL_NAME = "fullName";
    private static final String FIELD_MODIFIED = "modified";
    private static final String FIELD_COMICS = "comics";
    private static final String FIELD_SERIES = "series";
    private static final String FIELD_NOTES = "notes";
    private static final String FIELD_SORT_BY = "orderBy";

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

        if (values != null && !values.isEmpty()) {
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