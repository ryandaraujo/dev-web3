package com.autobots.automanager.utils;

import org.springframework.stereotype.Component;

@Component
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
