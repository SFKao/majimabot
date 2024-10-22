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

@Document("word")
public class WordEntity {

    @Id
    private String id;

    @EqualsAndHashCode.Include
    private String palabra;
    private long userId;

}
