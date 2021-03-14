package com.plumpnation.graphexperiment;

public class MyNode {
    private Integer id;
    private String hash;

    public MyNode(Integer id, String hash) {
        this.id = id;
        this.hash = hash;
    }

    public Integer getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }
}
