package cn.snow.completablefuture;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ValueBean {
    private int id;
    private String name;
    private LocalDate bod;

    public ValueBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
