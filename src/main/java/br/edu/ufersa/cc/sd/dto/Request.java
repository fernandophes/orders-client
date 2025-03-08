package br.edu.ufersa.cc.sd.dto;

import java.io.Serializable;

import br.edu.ufersa.cc.sd.enums.Operation;
import lombok.Data;

@Data
public class Request<T extends Serializable> implements Serializable {

    private final Operation operation;
    private final T item;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public Request(final Operation operation, final T item) {
        this.operation = operation;
        this.item = item;
        this.type = (Class<T>) item.getClass();
    }

    public Request(final Operation operation, final Class<T> type) {
        this.operation = operation;
        this.type = type;
        this.item = null;
    }

}
