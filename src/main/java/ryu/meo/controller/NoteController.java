package ryu.meo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ryu.meo.dto.request.NoteRequestDTO;
import ryu.meo.dto.response.NoteResponseDTO;
import ryu.meo.model.Note;
import ryu.meo.model.NoteRepository;
import ryu.meo.service.NoteService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Modification Logs:
 * DATE | AUTHOR | DESCRIPTION
 * -------------------------------------
 * 01-08-2025 | Ryu | Create
 */
@RestController
@RequestMapping("/note")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NoteController {

    final NoteService noteService;
    final NoteRepository noteRepository;

    @GetMapping("get-all")
    public Page<Note> getAllNotes(@AuthenticationPrincipal Jwt jwt,
            @PageableDefault(sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
        return noteRepository.findByAccountId(jwt.getSubject(), pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<NoteResponseDTO> createNewNote(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.createNewNote(jwt.getSubject()));
    }

    @GetMapping("/get/{noteId}")
    public ResponseEntity<NoteResponseDTO> getNote(@PathVariable long noteId) {
        return ResponseEntity.ok(noteService.getNote(noteId));
    }

    @PutMapping("/update")
    public ResponseEntity<NoteResponseDTO> updateNote(@RequestBody NoteRequestDTO noteUpdateRequest) {
        return ResponseEntity.ok(noteService.updateNote(noteUpdateRequest));
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable long noteId) {
        noteRepository.deleteById(noteId);
        return ResponseEntity.noContent().build();
    }
}
