package net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository;

import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, Long> {



}
