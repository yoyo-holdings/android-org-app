package com.giaquino.todo.model.entity;

import com.google.auto.value.AutoValue;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/28/16
 */
@AutoValue public abstract class Note implements NoteModel {

    public static final Factory<Note> FACTORY = new Factory<>(AutoValue_Note::new);

    public static final Mapper<Note> MAPPER = FACTORY.select_allMapper();
}
