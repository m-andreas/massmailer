package com.markus.MassMailer.model.mail;

import com.markus.MassMailer.model.user.User;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Template {
    private PebbleEngine engine;
    private PebbleTemplate template;

    public Template(String templateText) {
        engine = new PebbleEngine
                    .Builder()
                    .strictVariables(true)
                    .loader(new StringLoader()).build();
        this.template = engine.getTemplate(templateText);
    }

    public String parse(User user) throws IOException {
        Writer writer = new StringWriter();
        template.evaluate(writer, user.getData());
        return writer.toString();
    }
}
