package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.Creator;

public class CreatorMapper {

    public CreatorDTO toCreatorDTO(Creator creator) {
        return
            new CreatorDTO(
                creator.getId(),
                creator.getFullName(),
                creator.getModified(),
                creator.getComics(),
                creator.getSeries(),
                creator.getNote() != null ? creator.getNote().getText() : null
            );
    }
}
