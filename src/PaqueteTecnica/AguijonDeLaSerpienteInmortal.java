package PaqueteTecnica;

import PaqueteCombatiente.Combatiente;
import PaqueteEfectos.Veneno;

public class AguijonDeLaSerpienteInmortal extends Tecnica{
	
	public AguijonDeLaSerpienteInmortal() {
		super("AguijonDeLaSerpiente", 30);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
	    validarObjetivo(ejecutor, objetivo, false);

	    objetivo.recibirDanio(ejecutor.modificarDanioProvocado(3));
	    objetivo.agregarEfecto(new Veneno(30, 3));
	}

}