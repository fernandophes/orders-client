package br.edu.ufersa.cc.sd.dto;

import java.io.Serializable;
import java.util.List;

import br.edu.ufersa.cc.sd.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T extends Serializable> implements Serializable {

    private final ResponseStatus status;
    private final String message;
    private final List<T> items;
    private final Class<T> type;

}
