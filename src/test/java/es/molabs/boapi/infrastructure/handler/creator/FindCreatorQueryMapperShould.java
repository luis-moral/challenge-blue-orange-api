package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creator.SortQuery;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

public class FindCreatorQueryMapperShould {

    private static final String QUERY_ID = "id";
    private static final String QUERY_FULL_NAME = "fullName";
    private static final String QUERY_MODIFIED = "modified";
    private static final String QUERY_COMICS = "comics";
    private static final String QUERY_SERIES = "series";
    private static final String QUERY_NOTES = "notes";
    private static final String QUERY_ORDER_BY = "orderBy";

    private FindCreatorQueryMapper mapper;

    @Before
    public void setUp() {
        mapper = new FindCreatorQueryMapper();
    }

    @Test public void
    map_from_multi_value_map_without_filters_or_sorting() {
        FindCreatorQuery query = mapper.from(new LinkedMultiValueMap<>());

        assertFiltersNull(query);

        Assertions
            .assertThat(query.getSortQuery())
            .isNull();
    }

    @Test public void
    map_from_multi_value_map_with_filters_and_without_sorting() {
        MultiValueMap<String, String> map = buildQueryMap();

        FindCreatorQuery query = mapper.from(map);

        assertQueryFilters(query, map);

        Assertions
            .assertThat(query.getSortQuery())
            .isNull();
    }

    @Test public void
    map_from_multi_value_map_without_filters_and_with_sorting() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(QUERY_ORDER_BY, Arrays.asList(QUERY_ID, "-" + QUERY_COMICS, QUERY_SERIES));

        FindCreatorQuery query = mapper.from(map);

        assertFiltersNull(query);
        assertOrder(QUERY_ID, SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(0));
        assertOrder(QUERY_COMICS, SortQuery.SortType.Descending, query.getSortQuery().getFields().get(1));
        assertOrder(QUERY_SERIES, SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(2));
    }

    @Test public void
    map_from_multi_value_map_with_filters_and_sorting() {
        MultiValueMap<String, String> map = buildQueryMap(Arrays.asList(QUERY_ID, "-" + QUERY_COMICS, QUERY_SERIES));

        FindCreatorQuery query = mapper.from(map);

        assertQueryFilters(query, map);
        assertOrder(QUERY_ID, SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(0));
        assertOrder(QUERY_COMICS, SortQuery.SortType.Descending, query.getSortQuery().getFields().get(1));
        assertOrder(QUERY_SERIES, SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(2));
    }

    @Test public void
    map_to_multi_value_map_without_filters_or_sorting() {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        MultiValueMap<String, String> map = mapper.toMap(query);

        assertMapFilters(map, query);

        Assertions
            .assertThat(map.get(QUERY_ORDER_BY))
            .isNull();
    }

    @Test public void
    map_to_multi_value_map_with_filters_and_without_sorting() {
        FindCreatorQuery query = buildQuery();

        MultiValueMap<String, String> map = mapper.toMap(query);

        assertMapFilters(map, query);

        Assertions
            .assertThat(map.get(QUERY_ORDER_BY))
            .isNull();
    }

    @Test public void
    map_to_multi_value_map_without_filters_and_with_sorting() {
        FindCreatorQuery query =
            new FindCreatorQuery(
                new SortQuery(
                    Arrays
                        .asList(
                            new SortQuery.SortQueryField(QUERY_COMICS, SortQuery.SortType.Ascending),
                            new SortQuery.SortQueryField(QUERY_SERIES, SortQuery.SortType.Descending)
                        )
                )
            );

        MultiValueMap<String, String> map = mapper.toMap(query);

        assertMapFilters(map, query);

        Assertions
            .assertThat(map.get(QUERY_ORDER_BY))
            .containsExactly(QUERY_COMICS, "-" + QUERY_SERIES);
    }

    @Test public void
    map_to_multi_value_map_with_filters_and_sorting() {
        FindCreatorQuery query =
            buildQuery(
                new SortQuery.SortQueryField(QUERY_COMICS, SortQuery.SortType.Ascending),
                new SortQuery.SortQueryField(QUERY_SERIES, SortQuery.SortType.Descending)
            );

        MultiValueMap<String, String> map = mapper.toMap(query);

        assertMapFilters(map, query);

        Assertions
            .assertThat(map.get(QUERY_ORDER_BY))
            .containsExactly(QUERY_COMICS, "-" + QUERY_SERIES);
    }

    private FindCreatorQuery buildQuery() {
        return buildQuery(null);
    }

    private FindCreatorQuery buildQuery(SortQuery.SortQueryField...fields) {
        String id = "1";
        String fullName = "Some Name";
        String modified = "12445458745";
        String comics = "5";
        String series = "3";
        String notes = "Some Notes";

        return
            new FindCreatorQuery(
                id,
                fullName,
                modified,
                comics,
                series,
                notes,
                fields != null ? new SortQuery(Arrays.asList(fields)) : null
            );
    }

    private MultiValueMap<String, String> buildQueryMap() {
        return buildQueryMap(null);
    }

    private MultiValueMap<String, String> buildQueryMap(List<String> sorting) {
        String id = "1";
        String fullName = "Some Name";
        String modified = "12445458745";
        String comics = "5";
        String series = "3";
        String notes = "Some Notes";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set(QUERY_ID, id);
        map.set(QUERY_FULL_NAME, fullName);
        map.set(QUERY_MODIFIED, modified);
        map.set(QUERY_COMICS, comics);
        map.set(QUERY_SERIES, series);
        map.set(QUERY_NOTES, notes);

        if (sorting != null) {
            map.put(QUERY_ORDER_BY, sorting);
        }

        return map;
    }

    private void assertQueryFilters(FindCreatorQuery query, MultiValueMap<String, String> map) {
        Assertions
            .assertThat(query.getId())
            .isEqualTo(map.getFirst(QUERY_ID));

        Assertions
            .assertThat(query.getFullName())
            .isEqualTo(map.getFirst(QUERY_FULL_NAME));

        Assertions
            .assertThat(query.getModified())
            .isEqualTo(map.getFirst(QUERY_MODIFIED));

        Assertions
            .assertThat(query.getComics())
            .isEqualTo(map.getFirst(QUERY_COMICS));

        Assertions
            .assertThat(query.getSeries())
            .isEqualTo(map.getFirst(QUERY_SERIES));

        Assertions
            .assertThat(query.getNotes())
            .isEqualTo(map.getFirst(QUERY_NOTES));
    }

    private void assertFiltersNull(FindCreatorQuery query) {
        Assertions
            .assertThat(query.getId())
            .isNull();

        Assertions
            .assertThat(query.getFullName())
            .isNull();

        Assertions
            .assertThat(query.getModified())
            .isNull();

        Assertions
            .assertThat(query.getComics())
            .isNull();

        Assertions
            .assertThat(query.getSeries())
            .isNull();

        Assertions
            .assertThat(query.getNotes())
            .isNull();
    }

    private void assertOrder(String expectedField, SortQuery.SortType expectedType, SortQuery.SortQueryField field) {
        Assertions
            .assertThat(field.getField())
            .isEqualTo(expectedField);

        Assertions
            .assertThat(field.getType())
            .isEqualTo(expectedType);
    }

    private void assertMapFilters(MultiValueMap<String, String> map, FindCreatorQuery query) {
        Assertions
            .assertThat(map.getFirst(QUERY_ID))
            .isEqualTo(query.getId());

        Assertions
            .assertThat(map.getFirst(QUERY_FULL_NAME))
            .isEqualTo(query.getFullName());

        Assertions
            .assertThat(map.getFirst(QUERY_MODIFIED))
            .isEqualTo(query.getModified());

        Assertions
            .assertThat(map.getFirst(QUERY_COMICS))
            .isEqualTo(query.getComics());

        Assertions
            .assertThat(map.getFirst(QUERY_SERIES))
            .isEqualTo(query.getSeries());

        Assertions
            .assertThat(map.getFirst(QUERY_NOTES))
            .isEqualTo(query.getNotes());
    }
}