package com.example.itera.domain.etapa;

public enum Etapa {

    R("Requisitos"),
    P("Projeto"),
    B("Desenvolvimento Back-end"),
    F("Desenvolvimento Front-end"),
    T("Teste"),
    ;
private String etapa;

Etapa(String etapa){
    this.etapa = etapa;
}

public String getEtapa() {
    return etapa;
}
}


