package jpabook.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorColumn(name="A")
@Getter
@Setter
public class Album extends Item {

    private String artist;
    private String etc;

}
