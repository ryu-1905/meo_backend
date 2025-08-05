package ryu.meo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ryu.meo.dto.request.NoteRequestDTO;
import ryu.meo.dto.response.NoteResponseDTO;
import ryu.meo.mapper.NoteMapper;
import ryu.meo.model.Note;
import ryu.meo.model.NoteRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteServiceImp implements NoteService {

    final NoteRepository noteRepository;
    final NoteMapper noteMapper;

    @Override
    public NoteResponseDTO createNewNote(String accountId) {
        return noteMapper.toResponseDTO(noteRepository.save(Note.builder()
                .title("New Note")
                .content("")
                .updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
                .accountId(accountId)
                .build()));
    }

    @Override
    public NoteResponseDTO updateNote(NoteRequestDTO noteUpdateRequest) {
        Note note = noteRepository.findById(noteUpdateRequest.getId()).get();
        note.setTitle(noteUpdateRequest.getTitle());
        note.setContent(noteUpdateRequest.getContent());
        note.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        return noteMapper.toResponseDTO(noteRepository.save(note));
    }

    @Override
    public NoteResponseDTO getNote(long noteId) {
        return noteMapper.toResponseDTO(noteRepository.findById(noteId).get());
    }

}
