package br.edu.ufersa.cc.sd.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import br.edu.ufersa.cc.sd.utils.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order implements Serializable {

    private Long code;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public String toString() {
        return JsonUtils.toJson(this);
    }

}
