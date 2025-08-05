package ryu.meo.mapper;

import org.mapstruct.Mapper;

import ryu.meo.dto.response.NoteResponseDTO;
import ryu.meo.model.Note;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteResponseDTO toResponseDTO(Note note);

}
