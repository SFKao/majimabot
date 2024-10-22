package net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper;

import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.WordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WordMapper {

    WordMapper INSTANCE = Mappers.getMapper(WordMapper.class);

    // Mapear de entidad a dominio
    Word toDomain(WordEntity wordEntity);

    // Mapear de dominio a entidad
    @Mapping(target = "id", ignore = true)  // Si quieres que el ID sea generado por la base de datos
    WordEntity toEntity(Word word);
}