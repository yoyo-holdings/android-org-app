package com.giaquino.todo.model.entity;

import com.google.auto.value.AutoValue;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/28/16
 */
@AutoValue public abstract class Checklist implements ChecklistModel {

    public static final Factory<Checklist> FACTORY = new Factory<>(AutoValue_Checklist::new);

    public static final Mapper<Checklist> MAPPER = FACTORY.select_allMapper();
}
