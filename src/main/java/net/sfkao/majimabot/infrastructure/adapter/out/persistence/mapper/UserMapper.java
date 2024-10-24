package net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper;

import net.sfkao.majimabot.domain.User;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mapear de entidad a dominio
    User toDomain(UserEntity userEntity);

    // Mapear de dominio a entidad
    UserEntity toEntity(User user);
}