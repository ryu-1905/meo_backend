package ryu.meo.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class NoteResponseDTO {
    Long id;

    String title;
    String content;
    LocalDateTime updatedAt;
}
