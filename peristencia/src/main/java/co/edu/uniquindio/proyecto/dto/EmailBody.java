package co.edu.uniquindio.proyecto.dto;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class EmailBody {

    @EqualsAndHashCode.Include
    private String email,mensaje,asunto;


}
