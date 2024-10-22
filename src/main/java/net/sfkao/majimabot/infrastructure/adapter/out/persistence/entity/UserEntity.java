package net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity;


import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Document("usuario")
public class UserEntity {

    @Id
    private long id;

    private long puntos = 0;

    private long minasExplotadas = 0;

}
