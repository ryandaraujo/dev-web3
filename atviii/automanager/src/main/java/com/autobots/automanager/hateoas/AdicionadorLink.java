package com.autobots.automanager.hateoas;

import java.util.List;

public interface AdicionadorLink<T> {
    public void adicionarLink(List<T> lista);
    public void adicionarLink(T objeto);
}
