package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.domain.Creator;

public class CreatorMapper {

    public CreatorDTO toCreatorDTO(Creator creator) {
        return
            new CreatorDTO(
                creator.getId(),
                creator.getFullName(),
                creator.getModified(),
                creator.getComics(),
                creator.getSeries(),
                creator.getNotes()
            );
    }
}
