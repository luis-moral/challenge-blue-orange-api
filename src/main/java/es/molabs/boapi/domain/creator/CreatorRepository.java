package es.molabs.boapi.domain.creator;

import es.molabs.boapi.application.FindCreatorQuery;

import java.util.List;

public interface CreatorRepository {

    List<Creator> find(FindCreatorQuery query);
}
