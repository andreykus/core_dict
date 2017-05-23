package com.bivgroup.common.orm.cash.kv;

import com.google.common.base.Objects;

/**
 * Created by bush on 19.08.2016.
 */
public class Response<T> {
    private final T response;


    public Response(T response) {
        this.response = response;

    }

    public T getResponse() {
        return this.response;
    }


    public String toString() {
        return "Response{response=" + this.response + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Response that = (Response) o;

        return (Objects.equal(this.response, that.response));
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.response});
    }
}
