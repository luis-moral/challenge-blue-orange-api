package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creator.SortQuery;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class FindCreatorQueryMapper {

    public static final String FIELD_ID = "id";
    public static final String FIELD_FULL_NAME = "fullName";
    public static final String FIELD_NAME_STARTS_WITH = "nameStartsWith";
    public static final String FIELD_MODIFIED = "modified";
    public static final String FIELD_COMICS = "comics";
    public static final String FIELD_SERIES = "series";
    public static final String FIELD_NOTES = "notes";
    public static final String FIELD_SORT_BY = "orderBy";

    public FindCreatorQuery fromQuery(MultiValueMap<String, String> queryParams) {
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

    public MultiValueMap<String, String> toMarvelApiQuery(FindCreatorQuery query) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        addFieldToMap(map, FIELD_ID, query.getId());
        addFieldToMap(map, FIELD_NAME_STARTS_WITH, query.getFullName());
        addFieldToMap(map, FIELD_MODIFIED, query.getModified());
        addFieldToMap(map, FIELD_COMICS, query.getComics());
        addFieldToMap(map, FIELD_SERIES, query.getSeries());
        addFieldToMap(map, FIELD_NOTES, query.getNotes());
        addSortingToMap(map, query.getSortQuery());

        return map;
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

    private void addFieldToMap(MultiValueMap<String, String> map, String field, String value) {
        if (value != null) {
            map.set(field, value);
        }
    }

    private void addSortingToMap(MultiValueMap<String, String> map, SortQuery sortQuery) {
        if (sortQuery != null && sortQuery.getFields() != null) {
            map
                .add(
                    FIELD_SORT_BY,
                    sortQuery
                        .getFields()
                        .stream()
                        .map(field -> field.getType() == SortQuery.SortType.Ascending ? field.getField() : "-" + field.getField())
                        .collect(Collectors.joining(","))
            );
        }
    }
}
