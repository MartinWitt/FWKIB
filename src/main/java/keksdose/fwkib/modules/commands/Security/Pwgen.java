
package keksdose.fwkib.modules.commands.Security;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

import keksdose.fwkib.modules.Command;

public class Pwgen implements Command {

	@Override
	public String apply(String message) {
		char[] possibleCharacters = (new String(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?"))
						.toCharArray();
		return RandomStringUtils.random(50, 20, possibleCharacters.length - 1, false, false,
				possibleCharacters, new SecureRandom());

	}

	@Override
	public String help(String message) {
		return "Schon wieder keine Ahnung ob dein Passwort 123456 gut ist? Dann nimm doch einfach ein sicheres von FWKIB, was nur DU siehst";
	}

}
