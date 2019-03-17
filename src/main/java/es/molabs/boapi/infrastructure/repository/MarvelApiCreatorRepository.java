package es.molabs.boapi.infrastructure.repository;

import es.molabs.boapi.application.FindCreatorQuery;
import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;

import java.util.List;

public class MarvelApiCreatorRepository implements CreatorRepository {

    @Override
    public List<Creator> find(FindCreatorQuery query) {
        return null;
    }
}
