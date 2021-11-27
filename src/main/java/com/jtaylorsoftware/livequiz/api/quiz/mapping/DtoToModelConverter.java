package com.jtaylorsoftware.livequiz.api.quiz.mapping;

/**
 * Utility for converting from data transfer objects (DTO) to model objects.
 */
public interface DtoToModelConverter<Dto, Model> {
    /**
     * Converts (fills in) a new {@code Model} instance with data from a {@code Dto} instance.
     * @param object Instance to convert.
     * @return A newly created {@code Model} with any matching properties initialized to values from the {@code Dto}.
     */
    Model toModel(Dto object);
}
