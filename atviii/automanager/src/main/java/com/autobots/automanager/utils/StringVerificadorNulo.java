package com.autobots.automanager.utils;

public class StringVerificadorNulo {
    public boolean verificar(String dado) {
		boolean nulo = true;
		if (!(dado == null)) {
			if (!dado.isBlank()) {
				nulo = false;
			}
		}
		return nulo;
	}
}
