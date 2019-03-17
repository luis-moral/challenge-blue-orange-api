package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creator.SortQuery;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

public class FindCreatorQueryMapperShould {

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
        String id = "1";
        String fullName = "Some Name";
        String modified = "12445458745";
        String comics = "5";
        String series = "3";
        String notes = "Some Notes";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("id", "1");
        map.set("fullName", fullName);
        map.set("modified", modified);
        map.set("comics", comics);
        map.set("series", series);
        map.set("notes", notes);

        FindCreatorQuery query = mapper.from(map);

        Assertions
            .assertThat(query.getId())
            .isEqualTo(id);

        Assertions
            .assertThat(query.getFullName())
            .isEqualTo(fullName);

        Assertions
            .assertThat(query.getModified())
            .isEqualTo(modified);

        Assertions
            .assertThat(query.getComics())
            .isEqualTo(comics);

        Assertions
            .assertThat(query.getSeries())
            .isEqualTo(series);

        Assertions
            .assertThat(query.getNotes())
            .isEqualTo(notes);

        Assertions
            .assertThat(query.getSortQuery())
            .isNull();
    }

    @Test public void
    map_from_multi_value_map_without_filters_and_with_sorting() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("orderBy", Arrays.asList("id", "-comics", "series"));

        FindCreatorQuery query = mapper.from(map);

        assertFiltersNull(query);

        assertOrder("id", SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(0));
        assertOrder("comics", SortQuery.SortType.Descending, query.getSortQuery().getFields().get(1));
        assertOrder("series", SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(2));
    }

    @Test public void
    map_from_multi_value_map_with_filters_and_sorting() {
        String id = "1";
        String fullName = "Some Name";
        String modified = "12445458745";
        String comics = "5";
        String series = "3";
        String notes = "Some Notes";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("id", "1");
        map.set("fullName", fullName);
        map.set("modified", modified);
        map.set("comics", comics);
        map.set("series", series);
        map.set("notes", notes);
        map.put("orderBy", Arrays.asList("id", "-comics", "series"));

        FindCreatorQuery query = mapper.from(map);

        Assertions
            .assertThat(query.getId())
            .isEqualTo(id);

        Assertions
            .assertThat(query.getFullName())
            .isEqualTo(fullName);

        Assertions
            .assertThat(query.getModified())
            .isEqualTo(modified);

        Assertions
            .assertThat(query.getComics())
            .isEqualTo(comics);

        Assertions
            .assertThat(query.getSeries())
            .isEqualTo(series);

        Assertions
            .assertThat(query.getNotes())
            .isEqualTo(notes);

        assertOrder("id", SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(0));
        assertOrder("comics", SortQuery.SortType.Descending, query.getSortQuery().getFields().get(1));
        assertOrder("series", SortQuery.SortType.Ascending, query.getSortQuery().getFields().get(2));
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
}