package ryu.meo.service;

import ryu.meo.dto.request.NoteRequestDTO;
import ryu.meo.dto.response.NoteResponseDTO;

public interface NoteService {
    NoteResponseDTO createNewNote(String accountId);

    NoteResponseDTO getNote(long noteId);

    NoteResponseDTO updateNote(NoteRequestDTO noteUpdateRequest);
}
