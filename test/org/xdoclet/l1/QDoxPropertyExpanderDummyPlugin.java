package org.xdoclet.l1;

import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

public  class QDoxPropertyExpanderDummyPlugin extends QDoxPlugin {

    public QDoxPropertyExpanderDummyPlugin(JellyTemplateEngine jellyTemplateEngine,
        QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        setMultioutput(false);
    }
    
}