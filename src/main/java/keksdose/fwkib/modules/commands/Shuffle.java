package keksdose.fwkib.modules.commands;

import java.util.Collections;
import java.util.List;

import com.google.common.primitives.Chars;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;

public class Shuffle implements Command {

    @Override
    public String apply(String message) {
        List<Character> chars = Chars.asList(message.toCharArray());
        Collections.shuffle(chars);
        return StringUtils.join(chars.stream().toArray());
    }
}
