
package keksdose.fwkib.modules;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class Pwgen implements Command {

	@Override
	public String apply(String message) {
		char[] possibleCharacters = (new String(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?"))
						.toCharArray();
		return RandomStringUtils.random(50, 20, possibleCharacters.length - 1, false, false, possibleCharacters,
				new SecureRandom());

	}

}