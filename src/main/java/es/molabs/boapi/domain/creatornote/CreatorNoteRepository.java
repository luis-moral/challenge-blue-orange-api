package es.molabs.boapi.domain.creatornote;

import java.util.List;

public interface CreatorNoteRepository {

    List<CreatorNote> findByCreatorId(List<Integer> ids);
}
