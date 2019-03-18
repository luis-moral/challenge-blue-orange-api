package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.Creator;

public class MarvelCreatorMapper {

    public Creator toCreator(MarvelCreatorDTO creatorDTO) {
        return
            new Creator(
                creatorDTO.getId(),
                creatorDTO.getFullName(),
                creatorDTO.getModified(),
                creatorDTO.getComics(),
                creatorDTO.getSeries()
            );
    }
}
